package com.wgu_android.studentprogresstracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu_android.studentprogresstracker.Database.AppRepository;
import com.wgu_android.studentprogresstracker.Entities.TermEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    //TODO notes for paper
    //technical challenges - spinner working, not correctly looking up position, solution, chnage to a loop

    //********************************************
    //Variable Declarations
    private AppRepository mRepository;


    //********************************************
    //Constructors
    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }


    //********************************************
    //Methods
    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAllData() {

        //deletes all data in all tables
        mRepository.deleteAllAssessments();
        mRepository.deleteAllCourses();
        mRepository.deleteAllTerms();


    }
}
