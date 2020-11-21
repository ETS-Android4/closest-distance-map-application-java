package com.example.mapapplication.data.road_diraction;

import com.example.mapapplication.data.place.PlaceEntity;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class RoadDiraction {

    public PlaceEntity Road_placeEntity;

    public float distance;

    public JsonArray road_Points;

    public RoadDiraction(){

    }

    public RoadDiraction(PlaceEntity road_placeEntity, float distance, JsonArray road_Points) {
        Road_placeEntity = road_placeEntity;
        this.distance = distance;
        this.road_Points = road_Points;
    }

    public PlaceEntity getRoad_placeEntity() {
        return Road_placeEntity;
    }

    public void setRoad_placeEntity(PlaceEntity road_placeEntity) {
        Road_placeEntity = road_placeEntity;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public JsonArray getRoad_Points() {
        return road_Points;
    }

    public void setRoad_Points(JsonArray road_Points) {
        this.road_Points = road_Points;
    }
}
