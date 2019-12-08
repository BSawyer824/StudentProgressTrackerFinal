package com.wgu_android.studentprogresstracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wgu_android.studentprogresstracker.ViewModels.MainViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgu_android.studentprogresstracker.Utilities.Constants.LIST_ASSESSMENT_ACTIVITY_REQUEST_CODE;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.LIST_COURSE_ACTIVITY_REQUEST_CODE;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.LIST_TERM_ACTIVITY_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    //WHERE WE ARE NOW:
    //TERMS ARE WORKING
    //NEED TO CREATE NEW OR EDIT EXISTING COURSES, BOTH FROM THE COURSE MENU AND THE TERM MENU


    @BindView(R.id.btnViewTerms)
    Button mBtnTerms;

    @BindView(R.id.btnCourses)
    Button mBtnCourses;

    @BindView(R.id.btnAssessments)
    Button mBtnAssessments;

    @BindView(R.id.buttonTestNotes)
    Button mBtnTestNotes;

    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initViewModel();

        //Button to open up a list of All Terms
        final Button buttonTerms = mBtnTerms;
        buttonTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTerm = new Intent(MainActivity.this, TermSummaryActivity.class);
                startActivityForResult(intentTerm, LIST_TERM_ACTIVITY_REQUEST_CODE);

            }
        });

        //Button to open up a list of All Courses
        final Button buttonCourses = mBtnCourses;
        buttonCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCourse = new Intent(MainActivity.this, CourseSummaryActivity.class);
                startActivityForResult(intentCourse, LIST_COURSE_ACTIVITY_REQUEST_CODE);

            }
        });

        //Button to open up a list of All Courses
        final Button buttonAssessment = mBtnAssessments;
        buttonAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAssessment = new Intent(MainActivity.this, AssessmentSummaryActivity.class);
                startActivityForResult(intentAssessment, LIST_ASSESSMENT_ACTIVITY_REQUEST_CODE);

            }
        });

        //Button to open course notes
        final Button buttonNote = mBtnTestNotes;
        buttonNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNote = new Intent(MainActivity.this, CourseNoteActivity.class);
                startActivityForResult(intentNote, LIST_ASSESSMENT_ACTIVITY_REQUEST_CODE);

            }
        });
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // used to add or remove sample data
        int id = item.getItemId();

        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all_data) {
            deleteAllData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        mViewModel.deleteAllData();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }
}
