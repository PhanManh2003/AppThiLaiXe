package com.example.appthilaixe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.appthilaixe.models.User;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertAll(List<User> users);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);
}
