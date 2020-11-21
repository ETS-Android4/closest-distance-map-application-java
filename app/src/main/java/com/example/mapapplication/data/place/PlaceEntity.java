package com.example.mapapplication.data.place;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "place_table")
public class PlaceEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    public Long user_id;

    public String position1;
    public String position2;

    public String place_name;
    public String place_description;

    public PlaceEntity() {

    }

    public PlaceEntity(Long user_id, String position1, String position2, String place_name, String place_description) {
        this.user_id = user_id;
        this.position1 = position1;
        this.position2 = position2;
        this.place_name = place_name;
        this.place_description = place_description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public String getPosition2() {
        return position2;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_description() {
        return place_description;
    }

    public void setPlace_description(String place_description) {
        this.place_description = place_description;
    }
}
