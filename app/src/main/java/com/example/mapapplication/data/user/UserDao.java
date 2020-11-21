package com.example.mapapplication.data.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(UserEntity userEntity);

    @Query("SELECT * FROM user_table WHERE username = :username and password = :password")
    UserEntity findUserByUserNamePassword(String username, String password);

    @Query("SELECT * FROM user_table WHERE id = :id")
    UserEntity findUserById(long id);

    @Query("SELECT * FROM user_table WHERE username = :username")
    UserEntity findUserByUsername(String username);

//    @Query("SELECT * FROM user_table")
//    List<UserEntity> findAllUsers();
}
