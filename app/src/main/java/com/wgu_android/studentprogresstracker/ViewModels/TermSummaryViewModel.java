package com.wgu_android.studentprogresstracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu_android.studentprogresstracker.Database.AppRepository;
import com.wgu_android.studentprogresstracker.Entities.TermEntity;

import java.util.List;

public class TermSummaryViewModel extends AndroidViewModel {

    //********************************************
    //Variable Declarations
    public LiveData<List<TermEntity>> mTerms;
    private AppRepository mRepository;


    //********************************************
    //Constructors
    public TermSummaryViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTerms = mRepository.mTerms;
    }


    //********************************************
    //Methods
}
