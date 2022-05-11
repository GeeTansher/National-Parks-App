package com.example.nationalparksmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nationalparksmap.Adapter.OnParkClickListener;
import com.example.nationalparksmap.Adapter.ParkRecyclerViewAdapter;
import com.example.nationalparksmap.data.AsyncResponse;
import com.example.nationalparksmap.data.Repository;
import com.example.nationalparksmap.model.Park;
import com.example.nationalparksmap.model.ParkViewModel;

import java.util.ArrayList;
import java.util.List;

public class ParksFragment extends Fragment implements OnParkClickListener {
    private RecyclerView recyclerView;
    private ParkRecyclerViewAdapter parkRecyclerViewAdapter;
    private List<Park> parkList;
    private ParkViewModel parkViewModel;

    public ParksFragment() {
        // Required empty public constructor
    }


    public static ParksFragment newInstance() {
        return new ParksFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parkViewModel = new ViewModelProvider(requireActivity())
                .get(ParkViewModel.class);

        if(parkViewModel.getParks().getValue() != null){
            parkList = parkViewModel.getParks().getValue();
            parkRecyclerViewAdapter = new ParkRecyclerViewAdapter(parkList,this);
            recyclerView.setAdapter(parkRecyclerViewAdapter);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parkList =new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parks, container, false);
        recyclerView = view.findViewById(R.id.park_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onParkClicked(Park park) {
        parkViewModel.setSelectedPark(park);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.parkFragment,DetailsFragment.newInstance())
                .commit();
    }
}