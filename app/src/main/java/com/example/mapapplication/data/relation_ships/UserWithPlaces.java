package com.example.mapapplication.data.relation_ships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mapapplication.data.place.PlaceEntity;
import com.example.mapapplication.data.user.UserEntity;

import java.util.List;

public class UserWithPlaces {
    @Embedded
    public UserEntity userEntity;
    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public List<PlaceEntity> placeEntities;

    public UserWithPlaces(UserEntity userEntity, List<PlaceEntity> placeEntities) {
        this.userEntity = userEntity;
        this.placeEntities = placeEntities;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public List<PlaceEntity> getPlaceEntities() {
        return placeEntities;
    }

    public void setPlaceEntities(List<PlaceEntity> placeEntities) {
        this.placeEntities = placeEntities;
    }
}
