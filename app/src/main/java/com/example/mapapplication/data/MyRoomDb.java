package com.example.mapapplication.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mapapplication.data.place.PlaceDao;
import com.example.mapapplication.data.place.PlaceEntity;
import com.example.mapapplication.data.user.UserDao;
import com.example.mapapplication.data.user.UserEntity;

@Database(entities = {UserEntity.class, PlaceEntity.class}, version = 1, exportSchema = false)
public abstract class MyRoomDb extends RoomDatabase {

    private static MyRoomDb instance;

    public abstract UserDao userDao();
    public abstract PlaceDao placeDao();

    // Singlton
    public static synchronized MyRoomDb getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyRoomDb.class, "user-database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao mUserDao;
        private PlaceDao mPlaceDao;

        PopulateDataAsyncTask(MyRoomDb db) {
            mUserDao = db.userDao();
            mPlaceDao = db.placeDao();
        }

        @Override
        protected Void doInBackground(Void... Voids) {

            mUserDao.insert(new UserEntity("admin@gmail.com", "admin", "123"));

            PlaceEntity placeEntity = new PlaceEntity((long)1, "Ankara", "My Place", "39.9286","32.8547");
            mPlaceDao.insert(placeEntity);
            return null;
        }
    }
}
