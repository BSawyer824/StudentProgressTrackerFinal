package com.wgu_android.studentprogresstracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;
import com.wgu_android.studentprogresstracker.ViewModels.CourseNoteViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.PrimaryKey;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.view.MenuItemCompat.*;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_KEY_ID;

public class CourseNoteActivity extends AppCompatActivity {

    @BindView(R.id.editTextCourseNote)
    EditText mCourseNote;


    private CourseNoteViewModel mViewModel;
    private int courseId;
    private Boolean mNewNote;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(CourseNoteViewModel.class);

        mViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {
            @Override
            public void onChanged(CourseEntity courseEntity) {
                mCourseNote.setText(courseEntity.getCourseNote());
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle("New Note");
            mNewNote = true;
        } else {
            setTitle("Edit Note");
            courseId = extras.getInt(COURSE_KEY_ID);
            mViewModel.loadData(courseId);
        }

    }


    //*****************************************************************************************
    //menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_note, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Save Button
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_course_note_delete) {
            mViewModel.deleteCourseNote();
            finish();
        } else if (item.getItemId() == R.id.action_course_send_notes) {
            saveAndReturn();
            CourseEntity course = mViewModel.mLiveCourse.getValue();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, course.getCourseNote());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, course.getCourseName() + "Notes: ");

            startActivity(Intent.createChooser(shareIntent, "Share Via:"));
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        //Back Button
        saveAndReturn();
    }

    private void saveAndReturn() {
        //Save Course Note to DB
        mViewModel.saveCourseNote(mCourseNote.getText().toString());
        finish();
    }

}
