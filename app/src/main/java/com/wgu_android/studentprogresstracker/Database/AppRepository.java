package com.wgu_android.studentprogresstracker.Database;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;
import com.wgu_android.studentprogresstracker.Entities.TermEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<TermEntity>> mTerms;
    public LiveData<List<CourseEntity>> mCourses;
    public LiveData<List<AssessmentEntity>> mAssessments;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {

        mDb = AppDatabase.getDatabase(context);
        mTerms = getAllTerms();
        mCourses = getAllCourses();
        mAssessments = getAllAssessments();

    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().insertAll(SampleData.getTerm());
                mDb.courseDao().insertAll(SampleData.getCourse());
                mDb.assessmentDao().insertAll(SampleData.getAssessment());
            }
        });

    }

    //*************************************************************
    //Term Methods
    private LiveData<List<TermEntity>> getAllTerms() {
        return mDb.termDao().getAllTerm();
    }

    public void deleteAllTerms() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().deleteAll();
            }
        });
    }

    public TermEntity getTermById(int termId) {
        return mDb.termDao().getTermById(termId);
    }

    public void insertTerm(final TermEntity term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().insert(term);
            }
        });
    }

    public void deleteTerm(final TermEntity term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDao().deleteTerm(term);
            }
        });
    }

    public int generateNewTermId() {
        return mDb.termDao().getNextTermId();
    }

    //*************************************************************
    //Course Methods
    private LiveData<List<CourseEntity>> getAllCourses() {
        return mDb.courseDao().getAllCourses();
    }

    public void deleteAllCourses() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDao().deleteAll();
            }
        });
    }

    public CourseEntity getCourseById(int courseId) {
        return mDb.courseDao().getCourseById(courseId);
    }

    public void insertCourse(final CourseEntity course) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDao().insert(course);
            }
        });
    }

    public void deleteCourse(final CourseEntity course) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDao().deleteCourse(course);
            }
        });
    }

    //*************************************************************
    //Assessment Methods
    private LiveData<List<AssessmentEntity>> getAllAssessments() {
        return mDb.assessmentDao().getAllAssessments();
    }

    public void deleteAllAssessments() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDao().deleteAll();
            }
        });
    }

    public AssessmentEntity getAssessmentById(int assessmentId) {
        return mDb.assessmentDao().getAssessmentById(assessmentId);
    }

    public void insertAssessment(final AssessmentEntity assessment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDao().insert(assessment);
            }
        });
    }

    public void deleteAssessment(final AssessmentEntity assessment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDao().deleteAssessment(assessment);
            }
        });
    }



}

