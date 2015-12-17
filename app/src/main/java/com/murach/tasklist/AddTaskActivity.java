package com.murach.tasklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by soukaina salihi
 */
public class AddTaskActivity extends Activity implements OnKeyListener {

    private EditText etName;
    private EditText etNotes;
    private TaskListDB db;
    private Spinner spinList;
    private boolean edit;
    private Task task;
    private String currTabName = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        etName = (EditText) findViewById(R.id.etName);
        etNotes = (EditText) findViewById(R.id.etNotes);
        spinList = (Spinner) findViewById(R.id.spinList);

        etName.setOnKeyListener(this);
        etNotes.setOnKeyListener(this);

        db = new TaskListDB(this);

        ArrayList<List> lists = db.getLists();
        if(lists==null){
            Log.i("lists", lists.toString());
        }
        else
            Log.i("lists", lists.toString());
        ArrayAdapter<List> adapter = new ArrayAdapter<List>(this, R.layout.spin_list, lists);
        spinList.setAdapter(adapter);

        Intent intent = getIntent();

        edit = intent.getBooleanExtra("editMode", false);

        if (edit) {
            long taskId = intent.getLongExtra("taskId", -1);
            task = db.getTask(taskId);

            etName.setText(task.getName());
            etNotes.setText(task.getNotes());
        }

        long listID;

        if (edit) {
            listID = (int) task.getListId();
        } else {
            currTabName = intent.getStringExtra("tab");
            listID = (int) db.getList(currTabName).getId();
        }

        int listPos = (int) listID - 1;
        spinList.setSelection(listPos);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSave:
                saveToDB();
                this.finish();
                break;
            case R.id.menuCancel:
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDB() {
        int listId = spinList.getSelectedItemPosition() + 1;
        String name = etName.getText().toString();
        String notes = etNotes.getText().toString();

        if (name.equals(null) || name.equals("")) {
            return;
        }

        if (!edit) {
            task = new Task();
        }
        task.setListId(listId);
        task.setName(name);
        task.setNotes(notes);

        if (edit) {
            db.updateTask(task);
        } else {
            db.insertTask(task);
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            // hide the soft Keyboard
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveToDB();
            return false;
        }
        return false;
    }
}
