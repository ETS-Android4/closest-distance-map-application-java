package com.example.mapapplication.data.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mUserRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application);
    }

    public void insert(UserEntity userEntity){
        mUserRepository.insert(userEntity);
    }

    public UserEntity findUserById(Long lon){
        return mUserRepository.findUserById(lon);
    }

    public UserEntity findUserByUsernameAndPassword(String username, String password){
        return mUserRepository.findUserByUserNamePassword(username, password);
    }

    public UserEntity findUserByUsername(String username){
        return mUserRepository.findUserByUsername(username);
    }

//    public List<UserEntity> findAllUsers(){
//        return mUserRepository.findAllUsers();
//    }
}
