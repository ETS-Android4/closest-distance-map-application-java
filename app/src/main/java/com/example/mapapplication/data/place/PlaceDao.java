package com.example.mapapplication.data.place;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mapapplication.data.relation_ships.UserWithPlaces;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert
    void insert(PlaceEntity placeEntity);

    @Delete
    void delete(PlaceEntity placeEntity);

    @Query("SELECT * FROM place_table WHERE id = :id")
    PlaceEntity findPlaceById(long id);

    @Query("SELECT * FROM place_table WHERE place_name = :placename")
    PlaceEntity findPlaceByUsername(String placename);

    @Query("SELECT * FROM user_table WHERE id = :user_id")
    List<UserWithPlaces> getUserWithPlacesRoad(Long user_id);

    @Transaction
    @Query("SELECT * FROM user_table WHERE id = :user_id")
    LiveData<List<UserWithPlaces>> getUserWithPlaces(Long user_id);
}
