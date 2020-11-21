package com.example.mapapplication.data.user;

import android.app.Application;
import android.os.AsyncTask;

import com.example.mapapplication.data.MyRoomDb;

import java.util.List;

public class UserRepository {

    private UserDao mUserDao;

    private UserEntity userEntity;

    private UserEntity findUserById;

    private List<UserEntity> findAllUsers;

    public UserRepository (Application app){
        MyRoomDb db = MyRoomDb.getInstance(app);
        mUserDao = db.userDao();
//        findAllUsers = mUserDao.findAllUsers();
    }

    public void insert(UserEntity userEntity){
        mUserDao.insert(userEntity);
//        new InsertAsyncTask(mUserDao).execute(userEntity);
    }

    public UserEntity findUserById(Long id){
        return mUserDao.findUserById(id);
//        new GetUserAsyncTask(mUserDao).execute(id);
    }

    public UserEntity findUserByUserNamePassword(String username, String password){
        return mUserDao.findUserByUserNamePassword(username, password);
    }

    public UserEntity findUserByUsername(String username){
        return mUserDao.findUserByUsername(username);
    }

//    public List<UserEntity> findAllUsers(){
//            return findAllUsers;
//    }

    private static class InsertAsyncTask extends AsyncTask<UserEntity, Void, Void>{
        private UserDao mUserDao;

        public InsertAsyncTask(UserDao userDao)
        {
            mUserDao = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            mUserDao.insert(userEntities[0]);
            return null;
        }
    }

//    private static class GetUserAsyncTask extends AsyncTask<Long, Void, UserEntity> {
//        private UserDao mUserDao;
//
//        public GetUserAsyncTask(UserDao userDao) {
//            mUserDao = userDao;
//        }
//
//        @Override
//        protected UserEntity doInBackground(Long... longs) {
//            return mUserDao.findUserById(longs[0]);
//        }
//    }
}
