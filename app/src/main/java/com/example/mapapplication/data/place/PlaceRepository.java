package com.example.mapapplication.data.place;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mapapplication.data.relation_ships.UserWithPlaces;
import com.example.mapapplication.data.MyRoomDb;

import java.util.List;

public class PlaceRepository {

    private PlaceDao placeDao;

    private PlaceEntity placeEntity;

    public LiveData<List<UserWithPlaces>> getUserWithPlaces;

    public PlaceRepository (Application app){
        MyRoomDb db = MyRoomDb.getInstance(app);
        placeDao = db.placeDao();
//        getUserWithPlaces = placeDao.getUserWithPlaces();
    }

    public void insert(PlaceEntity placeEntity){
        placeDao.insert(placeEntity);
//        new InsertAsyncTask(mUserDao).execute(userEntity);
    }

    public void delete(PlaceEntity placeEntity){
        placeDao.delete(placeEntity);
    }

    public PlaceEntity findPlaceById(Long id){
        return placeDao.findPlaceById(id);
//        new GetUserAsyncTask(mUserDao).execute(id);
    }

    public PlaceEntity findPlaceByUsername(String placename){
        return placeDao.findPlaceByUsername(placename);
//        new GetUserAsyncTask(mUserDao).execute(id);
    }

    public List<UserWithPlaces> getUserWithPlacesRoad(Long user_id){
        return placeDao.getUserWithPlacesRoad(user_id);
    }

    public LiveData<List<UserWithPlaces>> getUserWithPlaces(Long user_id){
        return placeDao.getUserWithPlaces(user_id);
    }
}
