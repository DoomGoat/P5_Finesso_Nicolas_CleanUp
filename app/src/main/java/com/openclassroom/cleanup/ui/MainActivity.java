package com.openclassroom.cleanup.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.cleanup.R;
import com.openclassroom.cleanup.injection.Injection;
import com.openclassroom.cleanup.injection.ViewModelFactory;
import com.openclassroom.cleanup.model.Project;
import com.openclassroom.cleanup.model.Task;
import com.openclassroom.cleanup.views.TaskAdapter;
import com.openclassroom.cleanup.views.TaskViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */

public class MainActivity extends AppCompatActivity implements TaskAdapter.Listener {

    // FOR DESIGN
    @Nullable
    public AlertDialog dialog = null;
    @Nullable
    private EditText dialogEditText = null;
    @Nullable
    private Spinner dialogSpinner = null;
    private RecyclerView taskListRecyclerView;
    private TextView lblNoTasks;

    // FOR DATA
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    private List<Task> tasksList = new ArrayList<>();
    private List<Project> projectsList = new ArrayList<>();

    private SortMethod sortMethod = SortMethod.NONE;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        taskListRecyclerView = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);

        configureRecyclerView();
        configureViewModel();

        this.getTasks();
        this.getProjects();
        this.updateTasks();



    // -------------------
    // ACTIONS
    // -------------------

        //SHOW ADD TASK DIALOG
        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
    }

    @Override
    public void onClickDeleteButton(int position) {
        this.deleteTask(this.adapter.getTask(position).getId());
    }


    // -------------------
    // DATA
    // -------------------

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.taskViewModel = ViewModelProviders.of(this, mViewModelFactory).get(TaskViewModel.class);
    }

    // ---

    private void getProjects(){
        this.taskViewModel.getAllProjects().observe(this, this::updateProjectsList);
    }

    private void getTasks(){
        this.taskViewModel.getAllTasks().observe(this, this::updateTasksList);
    }

    private void addTask(@NonNull Task task) {
        this.taskViewModel.createTask(task);
    }

    private void deleteTask(long taskId) {
        this.taskViewModel.deleteTask(taskId);
    }

    private void updateTasks() {
        if (tasksList.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            taskListRecyclerView.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            taskListRecyclerView.setVisibility(View.VISIBLE);
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasksList, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasksList, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasksList, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasksList, new Task.TaskOldComparator());
                    break;
            }
            this.adapter.updateData(tasksList, projectsList);
        }
    }

    // -------------------
    // UI
    // -------------------

    private void configureRecyclerView(){
        this.adapter = new TaskAdapter(this);
        this.taskListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.taskListRecyclerView.setAdapter(this.adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }
        return super.onOptionsItemSelected(item);
    }


    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                // TODO: Replace this by id of persisted task
                long id = (long) (Math.random() * 50000);

                Task task = new Task(
                        id,
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );
                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });
        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });
        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {

        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projectsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    private void updateTasksList(List<Task> tasks){
        tasksList = tasks;
    }

    private void updateProjectsList(List<Project> projects){
        projectsList = projects;
    }


    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        ALPHABETICAL,
        ALPHABETICAL_INVERTED,
        RECENT_FIRST,
        OLD_FIRST,
        NONE
    }
}
