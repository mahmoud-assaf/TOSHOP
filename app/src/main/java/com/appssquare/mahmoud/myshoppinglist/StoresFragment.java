package com.appssquare.mahmoud.myshoppinglist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment {
    List<Shop> shopsList;
    DatabaseHelper db;
    Preferences preferences;
    ListView shopsListView;
    ShopsListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Log.e("stores fragment :" ,"onCreat");
        db=new DatabaseHelper(getActivity());
        preferences=Preferences.getInstance(getActivity());
        /*shopsList=db.getUserShops(preferences.getKey("userId"));
       // Log.e("size of shops",String.valueOf(shopsList.size()));
        adapter=new ShopsListAdapter(getActivity(),shopsList);*/



    }

    public StoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Log.e("stores fragment :" ,"onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopsListView=(ListView)view.findViewById(R.id.shopsListView);

        shopsListView.setAdapter(adapter);


    }


    @Override
    public void onResume() {
        super.onResume();
       // Toast.makeText(getActivity(),"shown 2",Toast.LENGTH_LONG).show();
        shopsList=db.getUserShops(preferences.getKey("userId"));
        adapter=new ShopsListAdapter(getActivity(),shopsList);
        shopsListView.setAdapter(adapter);
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
            //  Log.e( "groups count  :",String.valueOf(listAdapter.getGroupCount()));

        }
    }

}
