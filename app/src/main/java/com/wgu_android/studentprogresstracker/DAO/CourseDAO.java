package com.wgu_android.studentprogresstracker.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wgu_android.studentprogresstracker.Entities.CourseEntity;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseEntity course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CourseEntity> courses);

    @Query("DELETE FROM course_table")
    void deleteAll();

    @Delete
    void deleteCourse(CourseEntity courseEntity);

    @Query("SELECT * FROM course_table ORDER BY course_id ASC")
    LiveData<List<CourseEntity>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE course_id = :id")
    CourseEntity getCourseById(int id);


}

