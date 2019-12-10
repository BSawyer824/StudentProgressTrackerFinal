package com.wgu_android.studentprogresstracker.ViewModels;

import android.app.Application;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.wgu_android.studentprogresstracker.Database.AppRepository;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseNoteViewModel extends AndroidViewModel {

    public MutableLiveData<CourseEntity> mLiveCourse = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseNoteViewModel(@NonNull Application application) {

        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int courseId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CourseEntity course = mRepository.getCourseById(courseId);
                mLiveCourse.postValue(course);
            }
        });

    }

    public void saveCourseNote(String courseNote) {
        CourseEntity course = mLiveCourse.getValue();
        course.setCourseNote(courseNote);
        mRepository.insertCourse(course);
    }

    public void deleteCourseNote() {
        CourseEntity course = mLiveCourse.getValue();
        course.setCourseNote("");
        mRepository.insertCourse(course);
    }
}
