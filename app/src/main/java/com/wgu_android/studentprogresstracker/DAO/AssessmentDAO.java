package com.wgu_android.studentprogresstracker.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AssessmentEntity assessment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AssessmentEntity> assessments);

    @Query("DELETE FROM assessment_table")
    void deleteAll();

    @Delete
    void deleteAssessment(AssessmentEntity assessmentEntity);

    @Query("SELECT * FROM assessment_table ORDER BY assessment_id ASC")
    LiveData<List<AssessmentEntity>> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE assessment_id = :id")
    AssessmentEntity getAssessmentById(int id);

}

