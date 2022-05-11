package com.example.nationalparksmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nationalparksmap.Adapter.ViewPagerAdapter;
import com.example.nationalparksmap.model.Park;
import com.example.nationalparksmap.model.ParkViewModel;
import com.google.android.material.tabs.TabLayout;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Observable;

public class DetailsFragment extends Fragment {
    private ParkViewModel parkViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager2;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkViewModel = new ViewModelProvider(requireActivity())
                .get(ParkViewModel.class);

        TextView parkName = view.findViewById(R.id.detailsParkName);
        TextView parkDes = view.findViewById(R.id.detailsParkDesignation);

        viewPager2 = view.findViewById(R.id.detailsViewPager);

        TextView description = view.findViewById(R.id.detailsDescription);
        TextView activities = view.findViewById(R.id.detailsActivities);
        TextView entranceFees = view.findViewById(R.id.detailsEntranceFess);
        TextView opHours = view.findViewById(R.id.detailsOPHours);
        TextView detailsTopics = view.findViewById(R.id.detailsTopics);
        TextView directions = view.findViewById(R.id.detailsDirections);

        parkViewModel.getSelectedPark().observe(getViewLifecycleOwner(), park -> {
            parkName.setText(park.getName());
            parkDes.setText(park.getDesignation());
            description.setText(park.getDescription());

            StringBuilder activityString = new StringBuilder();
            for (int i=0;i<park.getActivities().size();i++){
                activityString.append(park.getActivities().get(i).getName())
                        .append(" || ");
            }
            activities.setText(activityString);

            if(park.getEntranceFees().size() >0){
                entranceFees.setText(MessageFormat.format("{0}{1}", getString(R.string.cost), park.getEntranceFees().get(0).getCost()));
            }
            else{
                entranceFees.setText(R.string.infoNotAvailable);
            }
            StringBuilder opHString = new StringBuilder();
            opHString.append("Monday: ").append(park.getOperatingHours().get(0).getStandardHours().getMonday()).append("\n");
            opHString.append("Tuesday: ").append(park.getOperatingHours().get(0).getStandardHours().getTuesday()).append("\n");
            opHString.append("Wednesday: ").append(park.getOperatingHours().get(0).getStandardHours().getWednesday()).append("\n");
            opHString.append("Thursday: ").append(park.getOperatingHours().get(0).getStandardHours().getThursday()).append("\n");
            opHString.append("Friday: ").append(park.getOperatingHours().get(0).getStandardHours().getFriday()).append("\n");
            opHString.append("Saturday: ").append(park.getOperatingHours().get(0).getStandardHours().getSaturday()).append("\n");
            opHString.append("Sunday: ").append(park.getOperatingHours().get(0).getStandardHours().getSunday()).append("\n");

            opHours.setText(opHString);

            StringBuilder topicBuilder = new StringBuilder();
            for (int i = 0; i < park.getTopics().size(); i++) {
                topicBuilder.append(park.getTopics().get(i).getName()).append(" || ");
            }
            detailsTopics.setText(topicBuilder);

            if(!TextUtils.isEmpty(park.getDirectionsInfo())) {
                directions.setText(park.getDirectionsInfo());
            }
            else{
                directions.setText(R.string.no_directions);
            }

            viewPagerAdapter = new ViewPagerAdapter(park.getImages());
            viewPager2.setAdapter(viewPagerAdapter);
        });
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }
}