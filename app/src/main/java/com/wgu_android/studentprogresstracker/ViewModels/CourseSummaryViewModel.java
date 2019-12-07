package com.wgu_android.studentprogresstracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu_android.studentprogresstracker.Database.AppRepository;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;

import java.util.List;

public class CourseSummaryViewModel  extends AndroidViewModel {

    //********************************************
    //Variable Declarations
    public LiveData<List<CourseEntity>> mCourses;
    private AppRepository mRepository;


    //********************************************
    //Constructors
    public CourseSummaryViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mCourses = mRepository.mCourses;
    }


    //********************************************
    //Methods
}
