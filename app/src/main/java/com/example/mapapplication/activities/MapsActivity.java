package com.example.mapapplication.activities;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.view.Gravity;
import android.widget.EditText;
import android.location.Location;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.mapapplication.R;
import com.example.mapapplication.data.road_diraction.RoadDiraction;
import com.example.mapapplication.retrofit.Api;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.example.mapapplication.data.place.PlaceEntity;
import com.example.mapapplication.adapters.SectionsPagerAdapter;
import com.example.mapapplication.data.place.PlaceViewModel;
import com.example.mapapplication.fragments.MessageFormFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_CODE = 101;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LatLng position;
    private Marker current_Marker;

    private FloatingActionButton add_icon;
    private FloatingActionButton check_icon;

    private boolean move_marker = false;
    private boolean un_updated = false;

    private FragmentTransaction transaction;
    private Fragment fragment;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private PlaceViewModel mPlaceViewModel;

    private Long user_id;
    private ProgressDialog loadingBar;

    private static List<RoadDiraction> list_roadDiractions;
    private static RoadDiraction nearest_Place = new RoadDiraction();

    private PlaceEntity current_placeEntity;
    private List<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get Current Position
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        // Get User id that was logined
        user_id = getIntent().getExtras().getLong("user_id");

        add_icon = findViewById(R.id.add_icon);
        check_icon = findViewById(R.id.check_icon);

        loadingBar = new ProgressDialog(this);

        // Add the swipe menu
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Add the fragments of menu Gorever and Adres Arar
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Get ViewMode of PlaceViewModel where used to insert new place
        mPlaceViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);


        // FloatingActionButton when want to add new place + button
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_icon.setVisibility(View.GONE);
                check_icon.setVisibility(View.VISIBLE);

                Clear_All();
//                if (current_Marker != null){
//                    current_Marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    current_Marker.remove();
//                }

                position = mMap.getCameraPosition().target;
                current_Marker = mMap.addMarker(new MarkerOptions().draggable(true).position(position).title("New Place"));
//                move_marker = false;
//                un_updated = true;
            }
        });

        // FloatingActionButton when want to completely add the new place
        check_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Open_PlaceFragment(null, null);
                un_updated = false;
            }
        });

        // If can't get current position, at that time will set the Ankara position
        if (position == null) {
            position = new LatLng(39.9286, 32.8547);
        }
    }

    // Send the userId to fragment to get it's places
    public Long getMyData() {
        return user_id;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside     the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        // Check the Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Move the camera to position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
//                ltlng, 16f);
//        mMap.animateCamera(cameraUpdate);

        // Move Maker Simultaneously with screen movement
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (current_Marker != null && !move_marker) {
                    current_Marker.setPosition(mMap.getCameraPosition().target);
                }
            }
        });

        // Process marker when clicking if clicked on marker or new marker or moving marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (un_updated){
//                    add_icon.setVisibility(View.GONE);
//                    check_icon.setVisibility(View.VISIBLE);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    move_marker = true;

                    if (current_Marker == null){
                        current_Marker = marker;
                    }
                    else {
                        if (!current_Marker.getId().equals(marker.getId())) {
                            current_Marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            current_Marker = marker;
                        }
                    }
                }
                return false;
            }
        });

        // Process marker while dragging marker
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
//                add_icon.setVisibility(View.GONE);
//                check_icon.setVisibility(View.VISIBLE);
                if (current_Marker != null && !current_Marker.getId().equals(marker.getId())) {
                    current_Marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                current_Marker = marker;
            }
        });
    }

    // Using to get the diration beteen tow points use "Open route service"
    private void getRoadDirection(){
        loadingBar.setTitle("Hazırlınıyor");
        loadingBar.setMessage("Bekle Lüften... Rota Oluşuyor");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        list_roadDiractions = new ArrayList<>();
        nearest_Place = new RoadDiraction();

        List<PlaceEntity> _places = mPlaceViewModel.getUserWithPlacesRoad(user_id).get(0).placeEntities;

        if (_places.size() == 0) {
            loadingBar.dismiss();
            return;
        }

        String pos1 = "" + position.longitude + "," + position.latitude;

        for (int i = 0 ; i < _places.size(); i++) {
            String pos2 =  "" + _places.get(i).position2.toString() + "," + _places.get(i).position1.toString();
            Call<JsonObject> call = Api.getInstance().getApi().getRoadDuration(Api.getApi_key(), pos1, pos2);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()){

                        JsonObject jsonObject = response.body();
                        JsonArray features = (JsonArray) jsonObject.get("features");

                        RoadDiraction roadDiraction = new RoadDiraction();
                        roadDiraction.setRoad_placeEntity(_places.get(0));
                        _places.remove(0);
                        try {
                            roadDiraction.setDistance(Float.parseFloat(String.valueOf(((JsonObject)((JsonArray)((JsonObject)((JsonObject)features.get(0)).get("properties")).get("segments")).get(0)).getAsJsonPrimitive("distance"))));  // ['features'][0]['geometry']['coordinates']
                        } catch (Exception e){
                            roadDiraction.setDistance((float) 0.0);
                            System.out.println("Error " + e);
                        }
                        try {
                            roadDiraction.setRoad_Points((JsonArray)(((JsonObject)((JsonObject)features.get(0)).get("geometry")).get("coordinates")));
                        } catch (Exception e){
                            System.out.println("Error " + e);
                        }

                        list_roadDiractions.add(roadDiraction);

                        if (nearest_Place.getRoad_placeEntity() == null || nearest_Place.distance > list_roadDiractions.get(list_roadDiractions.size() - 1).distance){
                            nearest_Place = list_roadDiractions.get(list_roadDiractions.size() - 1);
                        }
                    } else {
                        // Check url address
                        Toast.makeText(getApplicationContext(),"Teknik Hatasi 121", Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    // Check the coming data or ssl
                    Toast.makeText(getApplicationContext(), "Road bulunamadı 102 ", Toast.LENGTH_LONG).show();
                    call.cancel();
                }
            });
        }
    }

    public void RoadDirection_Button(View view){
        loadingBar.setTitle("Çiziyor");
        loadingBar.setMessage("Bekle Lüften... Rotayı çiziyor");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if (polylines != null){
            for(Polyline line : polylines)
            {
                line.remove();
            }
            polylines.clear();

            mMap.clear();
        }

        if (list_roadDiractions.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No Address for route", Toast.LENGTH_LONG);
            loadingBar.dismiss();
            return;
        }

        //polyline object
        polylines = null;
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;

        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < nearest_Place.getRoad_Points().size(); i++) {
            polyOptions.color(getResources().getColor(R.color.colorPrimary));
            polyOptions.width(7);

//                List<LatLng> points = new ArrayList<LatLng>();
//                for (int j = 0; j < nearest_Place.getRoad_Points().size(); j++) {
            JsonArray json = (JsonArray) nearest_Place.getRoad_Points().get(i);
            LatLng l = new LatLng(json.get(1).getAsDouble(), json.get(0).getAsDouble());
//                    points.add(l);
//                }

            polyOptions.addAll(Collections.singleton(l));
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylineStartLatLng = polyline.getPoints().get(0);
            int k = polyline.getPoints().size();
            polylineEndLatLng = polyline.getPoints().get(k-1);
            polylines.add(polyline);
        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title(nearest_Place.Road_placeEntity.place_name);
        mMap.addMarker(endMarker);

        Close_Drawer();

        loadingBar.dismiss();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    // Get local Position
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    position = new LatLng(location.getLatitude(), location.getLongitude());
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);

                    getRoadDirection();
                }
            }
        });
    }

    // Open the place fragment to add new place or to show place's details
    private void Open_PlaceFragment(String place_name, String place_description){
        fragment = new MessageFormFragment(place_name, place_description);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragment_form, fragment);
        transaction.commit();

        if (current_Marker != null){
            current_Marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        move_marker = true;

        Close_Drawer();
    }

    // Close menu navication where will check phone language
    private void Close_Drawer(){
        if (Locale.getDefault().getLanguage().contentEquals("ar")){
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.closeDrawer(Gravity.LEFT);
        }
    }

    // Add new place when click on kaydet button in place fragment
    public void AddNewPlace_KaydetButton(View view){
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setUser_id(user_id);

        EditText t = findViewById(R.id.is_adi_edittext);
        placeEntity.setPlace_name(t.getText().toString());
        t = findViewById(R.id.is_aciklama_edittext);
        placeEntity.setPlace_description(t.getText().toString());

        placeEntity.setPosition1(String.valueOf(current_Marker.getPosition().latitude));
        placeEntity.setPosition2(String.valueOf(current_Marker.getPosition().longitude));

        mPlaceViewModel.insert(placeEntity);

        CloseThePlaceFragment_IptalButton(view);

        drawer.openDrawer(GravityCompat.START);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    // Delete the place used with sil button
    public void DeletePlace_DeleteButton(View view){
        mPlaceViewModel.delete(current_placeEntity);
        PlaceEntity s = mPlaceViewModel.findPlaceById(current_placeEntity.getId());

        CloseThePlaceFragment_IptalButton(view);

        drawer.openDrawer(GravityCompat.START);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    // Close the Place Fragment when click on Iptal button
    public void CloseThePlaceFragment_IptalButton(View view){
        add_icon.setVisibility(View.VISIBLE);
        check_icon.setVisibility(View.GONE);

        Clear_All();
    }

    private void Clear_All(){
        if (current_Marker != null){
            current_Marker.remove();
        }
        current_Marker = null;
        move_marker = false;

        if (polylines != null){
            for(Polyline line : polylines)
            {
                line.remove();
            }
            polylines.clear();

            mMap.clear();
        }

        hideSoftKeyboard();
        if (fragment != null && fragment.getFragmentManager() != null) {
            fragment.getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    // When click on Detayi Goster button for show place detail and place position as marker in list places menu
    public void Detayi_ButtonOnClick(PlaceEntity placeEntity){
        Konum_ButtonOnClick(placeEntity);
        Open_PlaceFragment(placeEntity.place_name, placeEntity.place_description);

        current_placeEntity = placeEntity;
    }

    // When click on Konum Goster button for place detail in list places menu
    public void Konum_ButtonOnClick(PlaceEntity placeEntity){
        if (current_Marker != null){
            current_Marker.remove();
        }

        position = new LatLng(Double.parseDouble(placeEntity.position1), Double.parseDouble(placeEntity.position2));
        current_Marker = mMap.addMarker(new MarkerOptions().position(position).title(placeEntity.place_name));
        current_Marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        move_marker = true;
        un_updated = false;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        if (Locale.getDefault().getLanguage().contentEquals("ar")){
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.closeDrawer(Gravity.LEFT);
        }
    }

    // Close the keyboard used to close it after add new place...
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }
}