package com.example.nationalparksmap;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.nationalparksmap.Adapter.CustomInfoWindowAdapter;
import com.example.nationalparksmap.data.AsyncResponse;
import com.example.nationalparksmap.data.Repository;
import com.example.nationalparksmap.model.Park;
import com.example.nationalparksmap.model.ParkViewModel;
import com.example.nationalparksmap.utils.util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.nationalparksmap.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ParkViewModel parkViewModel;
    private List<Park> parkList;
    private CardView cardView;
    private EditText stateCodeEt;
    private ImageButton searchButton;
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        parkViewModel = new ViewModelProvider(this)
                .get(ParkViewModel.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        cardView = findViewById(R.id.cardViewStates);
        stateCodeEt = findViewById(R.id.floatingStateValueET);
        searchButton = findViewById(R.id.floatingSearch);


        BottomNavigationView bottomNavigationView =
                findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            int id = item.getItemId();
            switch (id) {
                case R.id.btnMapsNav:
                    if(cardView.getVisibility()==View.GONE ||
                            cardView.getVisibility()==View.INVISIBLE) {
                        cardView.setVisibility(View.VISIBLE);
                    }
                    parkList.clear();

                    mMap.clear();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map, mapFragment)
                            .commit();
                    mapFragment.getMapAsync(this);
                    return true;
                case R.id.btnMapParks:
                    cardView.setVisibility(View.GONE);
                    selectedFragment = ParksFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map, selectedFragment)
                            .commit();
                    return true;

            }
            return false;
        });

        searchButton.setOnClickListener(v -> {
            parkList.clear();
            util.hideSoftKeyboard(v);
            String stateCode = stateCodeEt.getText().toString().trim();
            if(!TextUtils.isEmpty(stateCode)){
                code = stateCode;
                parkViewModel.selectCode(code);
                onMapReady(mMap);  // refresh map
                stateCodeEt.setText("");
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);

        parkList = new ArrayList<>();

        populateMap();

    }

    private void populateMap() {
        mMap.clear();  // IMP it clears tha map before populating.
        Repository.getParks(parks -> {
            parkList = parks;

            for (Park park : parks) {
                LatLng location = new LatLng(Double.parseDouble(park.getLatitude()),
                        Double.parseDouble(park.getLongitude()));

                MarkerOptions markerOptions = new MarkerOptions().position(location)
                        .title(park.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_ROSE
                        ))
                        .snippet(park.getImages().get(0).getUrl()+"#"+park.getStates());
                Marker marker = mMap.addMarker(markerOptions);
                assert marker != null;
                marker.setTag(park);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
            }
            parkViewModel.setSelectedParks(parkList);
        } , code);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        cardView.setVisibility(View.GONE);

        // go to details fragment
        parkViewModel.setSelectedPark((Park) marker.getTag());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map,DetailsFragment.newInstance())
                .commit();
    }
}