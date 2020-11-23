package com.example.mapapplication.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapplication.R;
import com.example.mapapplication.data.place.PlaceEntity;
import com.example.mapapplication.activities.MapsActivity;
import com.example.mapapplication.data.place.PlaceViewModel;
import com.example.mapapplication.adapters.PlaceItemAdapter;
import com.example.mapapplication.data.relation_ships.UserWithPlaces;

import java.util.List;

public class PlacesUserFragment extends Fragment {

    private PlaceViewModel mPlaceViewModel;
    private PlaceItemAdapter placeItemAdapter;

    public static PlacesUserFragment newInstance() {
        PlacesUserFragment fragment = new PlacesUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placeItemAdapter = new PlaceItemAdapter();
        mPlaceViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_placesuser, container, false);
        final RecyclerView mrelativeLayout = root.findViewById(R.id.place_recyclerview_list);

        mrelativeLayout.setAdapter(placeItemAdapter);

        MapsActivity activity = (MapsActivity) getActivity();
        // Get the user Id from MapsActivity to get it's places
        Long user_id = activity.getMyData();

        // To get places of user and working as livedata
        mPlaceViewModel.getUserWithPlaces(user_id).observe(getViewLifecycleOwner(), new Observer<List<UserWithPlaces>>() {
            @Override
            public void onChanged(List<UserWithPlaces> userWithPlaces) {
                placeItemAdapter.PlaceItemAdapter(userWithPlaces.get(0).placeEntities);
            }
        });

        // When add to any item in list where will show to "Detayi Goster and Konum Gster" buttons
        placeItemAdapter.OnItemClickListener(new PlaceItemAdapter.OnItemCliclListener() {
            @Override
            public void onItemClick(PlaceEntity placeEntity, View view) {
                LinearLayout linearLayout = view.findViewById(R.id.linearlayout_detayikonum_buttons);
                ImageView down_icon = view.findViewById(R.id.down_icon);
                ImageView up_icon = view.findViewById(R.id.up_icon);

                Button detayi_button = view.findViewById(R.id.detayi_button);
                Button konum_button = view.findViewById(R.id.konum_button);

                linearLayout.setVisibility(linearLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

                down_icon.setVisibility(down_icon.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                up_icon.setVisibility(up_icon.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

                detayi_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.Detayi_ButtonOnClick(placeEntity);
                    }
                });

                konum_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.Konum_ButtonOnClick(placeEntity);
                    }
                });
            }
        });
        return root;
    }
}