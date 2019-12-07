package com.wgu_android.studentprogresstracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu_android.studentprogresstracker.Database.AppRepository;
import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;

import java.util.List;

public class AssessmentSummaryViewModel  extends AndroidViewModel {

    //********************************************
    //Variable Declarations
    public LiveData<List<AssessmentEntity>> mAssessment;
    private AppRepository mRepository;


    //********************************************
    //Constructors
    public AssessmentSummaryViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mAssessment = mRepository.mAssessments;
    }


    //********************************************
    //Methods
}