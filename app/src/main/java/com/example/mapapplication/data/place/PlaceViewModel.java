package com.example.mapapplication.data.place;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mapapplication.data.relation_ships.UserWithPlaces;

import java.util.List;

public class PlaceViewModel extends AndroidViewModel {

    private PlaceRepository mPlaceRepository;

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        mPlaceRepository = new PlaceRepository(application);
    }

    public void insert(PlaceEntity placeEntity){
        mPlaceRepository.insert(placeEntity);
    }

    public void delete(PlaceEntity placeEntity){
        mPlaceRepository.delete(placeEntity);
    }

    public PlaceEntity findPlaceById(Long id){
        return mPlaceRepository.findPlaceById(id);
    }

    public PlaceEntity findPlaceByUsername(String s){
        return mPlaceRepository.findPlaceByUsername(s);
    }

    public List<UserWithPlaces> getUserWithPlacesRoad(Long user_id){
        return mPlaceRepository.getUserWithPlacesRoad(user_id);
    }

    public LiveData<List<UserWithPlaces>> getUserWithPlaces(Long user_id){
        return mPlaceRepository.getUserWithPlaces(user_id);
    }
}
