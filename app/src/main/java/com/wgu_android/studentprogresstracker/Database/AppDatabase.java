package com.wgu_android.studentprogresstracker.Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wgu_android.studentprogresstracker.DAO.AssessmentDAO;
import com.wgu_android.studentprogresstracker.DAO.CourseDAO;
import com.wgu_android.studentprogresstracker.DAO.TermDAO;
import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;
import com.wgu_android.studentprogresstracker.Entities.TermEntity;
import com.wgu_android.studentprogresstracker.Utilities.ConvertTypes;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class}, version = 1, exportSchema = false)
@TypeConverters({ConvertTypes.class})
public abstract class AppDatabase extends RoomDatabase {

    //***************************************************************
    //Variable Declaration
    private static final String DATABASE_NAME = "StudentTrackerDB.db";
    private static volatile AppDatabase INSTANCE;
    private static final Object LOCK = new Object();

    public abstract TermDAO termDao();
    public abstract CourseDAO courseDao();
    public abstract AssessmentDAO assessmentDao();



    //***************************************************************
    //Constructor
    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //***************************************************************
    //This callback method will run when the DB is opened
    //it runs the method to populate the db asynchronously
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    //***************************************************************
    //This method populates the database asynchronously
    //also currently deletes all data at start up and populates a default value
    //will need to change once more screens are built
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TermDAO mTermDao;
        private final CourseDAO mCourseDao;
        private final AssessmentDAO mAssessmentDao;



        PopulateDbAsync(AppDatabase db) {
            mTermDao = db.termDao();
            mCourseDao = db.courseDao();
            mAssessmentDao = db.assessmentDao();


        }

        @Override
        protected Void doInBackground(final Void... params) {

            return null;
        }
    }


}

