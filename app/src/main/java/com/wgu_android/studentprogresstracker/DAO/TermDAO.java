package com.wgu_android.studentprogresstracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wgu_android.studentprogresstracker.Entities.TermEntity;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TermEntity term);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TermEntity> term);

    @Query("DELETE FROM term_table")
    void deleteAll();

    @Delete
    void deleteTerm(TermEntity termEntity);

    @Query("SELECT * FROM term_table ORDER BY term_id ASC")
    LiveData<List<TermEntity>> getAllTerm();

    @Query("SELECT * FROM term_table WHERE term_id = :id")
    TermEntity getTermById(int id);

    @Query("SELECT MAX(term_id)+1 FROM term_table")
    int getNextTermId();
}

