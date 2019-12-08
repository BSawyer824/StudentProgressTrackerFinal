package com.wgu_android.studentprogresstracker.ViewModels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.wgu_android.studentprogresstracker.Database.AppRepository;
import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentDetailViewModel extends AndroidViewModel {

    public MutableLiveData<AssessmentEntity> mLiveAssessment = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();



    public AssessmentDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance((getApplication()));
    }

    public void loadData(final int assessmentId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AssessmentEntity assessment = mRepository.getAssessmentById(assessmentId);
                mLiveAssessment.postValue(assessment);
            }
        });
    }

    public void saveAssessment(String assessmentName, Date goalDate, Date dueDate, String spinnerSelectedItem, int fkCourseId) {
        AssessmentEntity assessment = mLiveAssessment.getValue();

        if (assessment == null) {

            if (TextUtils.isEmpty((assessmentName.trim()))) {
                return;
            } else {
                assessment = new AssessmentEntity(assessmentName, spinnerSelectedItem, goalDate, dueDate, fkCourseId);
            }

        } else {
            assessment.setAssessmentName(assessmentName);
            assessment.setAssessmentGoalDate(goalDate);
            assessment.setAssessmentDueDate(dueDate);
            assessment.setAssessmentType(spinnerSelectedItem);
            assessment.setFkCourseId(fkCourseId);
        }
        mRepository.insertAssessment(assessment);

    }

    public void deleteAssessment() {
        mRepository.deleteAssessment(mLiveAssessment.getValue());
    }
}
