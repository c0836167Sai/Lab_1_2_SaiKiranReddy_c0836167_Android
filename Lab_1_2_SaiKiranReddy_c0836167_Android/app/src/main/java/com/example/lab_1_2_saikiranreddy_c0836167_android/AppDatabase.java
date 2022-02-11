package com.example.lab_1_2_saikiranreddy_c0836167_android;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao taskDao();
}

