package com.feedapp.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "like")
public class Like {


    @ColumnInfo(name = "like_to")
    @PrimaryKey
    @NonNull
    private String name;

    public Like(String name){
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
