package com.appssquare.mahmoud.myshoppinglist;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    Preferences preferences;
    //  TextView testview;
    DatabaseHelper db;
    List<Item> remindersList;
    RecyclerView remindersrecyclerview;
    AlertDialog b;
    ReminderRecyclerViewAdapter reminderRecyclerViewAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DatabaseHelper(getActivity());
        preferences=Preferences.getInstance(getActivity());
        /*remindersList=db.getUserRemindingItems(preferences.getKey("userId"));
        Log.e("reminders size",String.valueOf(remindersList.size()));
        reminderRecyclerViewAdapter=new ReminderRecyclerViewAdapter(getActivity(),remindersList);*/


    }
    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        remindersrecyclerview=view.findViewById(R.id.reminder_recyclerview);
        remindersrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        remindersrecyclerview.setAdapter(reminderRecyclerViewAdapter);
        remindersrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), remindersrecyclerview, new RecyclerClickInterface() {

            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(getActivity(), "Long Click for Options",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                openReminderDialog(remindersList.get(position),position);
            }


        }));


    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private RecyclerClickInterface clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final RecyclerClickInterface clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    private void openReminderDialog(final Item item, final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reminder_menu, null);

        dialogBuilder.setView(dialogView);
        //dialogBuilder.setTitle("Item ...");
      Button  deleteReminder = (Button) dialogView.findViewById(R.id.btn_delete_reminder);

        deleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindersList.remove(position);
                Log.e("reminder deleted :" ,item.getName());
                b.cancel();
                db.deleteItem(item);
                Toast.makeText(getActivity().getBaseContext(), R.string.reminder_deleted, Toast.LENGTH_SHORT).show();


                reminderRecyclerViewAdapter.notifyDataSetChanged();

            }
        });

        b = dialogBuilder.create();
        b.show();

    }
    @Override
    public void onResume() {
        super.onResume();
        remindersList=db.getUserRemindingItems(preferences.getKey("userId"));
        reminderRecyclerViewAdapter=new ReminderRecyclerViewAdapter(getActivity(),remindersList);
        remindersrecyclerview.setAdapter(reminderRecyclerViewAdapter);
        if (reminderRecyclerViewAdapter!=null) {
            reminderRecyclerViewAdapter.notifyDataSetChanged();


        }
        // Log.e("on resume","on resume");

    }


}
