package com.example.appthilaixe.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryEntity category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categories);

    @Query("SELECT * FROM categories ORDER BY id ASC")
    List<CategoryEntity> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    CategoryEntity getById(int id);

    @Query("DELETE FROM categories")
    void clear();
}
