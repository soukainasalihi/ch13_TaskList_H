package com.murach.tasklist;

/**
 * Created by soukaina salihi
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskLayout extends RelativeLayout implements OnClickListener {

    private CheckBox chbxCompleted;
    private TextView tvName;
    private TextView tvNotes;

    private Task task;
    private TaskListDB db;
    private Context contextHere;

    public TaskLayout(Context context){
        super(context);
    }
    public TaskLayout(Context context, Task task){
        super(context);
        this.contextHere = context;
        db = new TaskListDB(context);

        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflate.inflate(R.layout.entry_task, this, true);

        chbxCompleted = (CheckBox) findViewById(R.id.chbxCompleted);
        tvName = (TextView)findViewById(R.id.tvName);
        tvNotes = (TextView)findViewById(R.id.tvNotes);

        chbxCompleted.setOnClickListener(this);
        this.setOnClickListener(this);

        setTask(task);
    }

    public void setTask(Task taskCon){
        task = taskCon;
        tvName.setText(task.getName());

        if(task.getNotes().equalsIgnoreCase("")){
            tvNotes.setVisibility(GONE);
        }
        else{
            tvNotes.setText(task.getNotes());
        }

        if(task.getCompletedDateMillis() > 0){
            chbxCompleted.setChecked(true);
        }
        else{
            chbxCompleted.setChecked(false);
        }
    }

    @Override
    public void onClick(View viewAct) {
        switch(viewAct.getId()){
            case R.id.chbxCompleted:
                if(chbxCompleted.isChecked()){
                    task.setCompletedDate(System.currentTimeMillis());
                }
                else{
                    task.setCompletedDate(0);
                }
                db.updateTask(task);
                break;
            default:
                Intent intent = new Intent(contextHere, AddTaskActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("taskId", task.getId());
                intent.putExtra("editMode", true);
                contextHere.startActivity(intent);
                break;
        }

    }
}
