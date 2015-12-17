package com.murach.tasklist;

/**
 * Created by soukaina salihi
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class TaskListAdapt extends BaseAdapter {

    private Context contextAct;
    private ArrayList<Task> tasksList;

    public TaskListAdapt(Context context, ArrayList<Task> tasks){
        this.contextAct = context;
        this.tasksList = tasks;
    }

    @Override
    public int getCount(){
        return tasksList.size();
    }

    @Override
    public Object getItem(int pos){
        return tasksList.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return pos;
    }

    @Override
    public View getView(int pos, View conView, ViewGroup par){
        TaskLayout taskLay = null;
        Task taskV = tasksList.get(pos);

        if(conView == null){
            taskLay = new TaskLayout(contextAct, taskV);
        }
        else{
            taskLay = (TaskLayout) conView;
            taskLay.setTask(taskV);
        }
        return taskLay;
    }
}
