package com.wgu_android.studentprogresstracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wgu_android.studentprogresstracker.Adapters.CourseAdapter;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;
import com.wgu_android.studentprogresstracker.Entities.TermEntity;
import com.wgu_android.studentprogresstracker.ViewModels.TermDetailViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_NEW;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_TERM_ID;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.NEW_COURSE_ACTIVITY_REQUEST_CODE;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.TERM_KEY_ID;

public class TermDetailActivity extends AppCompatActivity {

    //*************************************************
    //Term Variables
    @BindView(R.id.editTextTermName)
    EditText mEditTextTermName;

    @BindView(R.id.editTextStartDate)
    EditText mEditTextStartDate;

    @BindView(R.id.editTextEndDate)
    EditText mEditTextEndDate;

    @BindView(R.id.textViewTermId_TermDetail)
    TextView mTextViewTermId;


    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    private EditText mTermName;
    private Boolean mNewTerm;
    private TermDetailViewModel mViewModel;
    private int termId;

    //*************************************************
    //Course Variables
    @BindView(R.id.recyclerView_AssociatedCourses)
    RecyclerView mRecyclerView;

    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseAdapter mAdapter;


    //*************************************************
    //Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();


        //New Course
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndReturn();
                termId = mViewModel.getLiveTermId();

                Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
                intent.putExtra(COURSE_TERM_ID, termId);
                intent.putExtra(COURSE_NEW, true);
                startActivityForResult(intent, NEW_COURSE_ACTIVITY_REQUEST_CODE);
            }
        });


        //*****************************************************
        //Make Start and End Date fields have a calendar date picker

        //Start Date - Date Picker
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        mEditTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermDetailActivity.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //End Date - Date Picker
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };

        mEditTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermDetailActivity.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);


    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(TermDetailViewModel.class);

        //Observe the Term that is being edited or added
        mViewModel.mLiveTerm.observe(this, new Observer<TermEntity>() {
            @Override
            public void onChanged(TermEntity termEntity) {
                mEditTextTermName.setText(termEntity.getTermName());
                setLabelStart(termEntity);
                setLabelEnd(termEntity);
                mTextViewTermId.setText(Integer.toString(termEntity.getTermID()));
            }
        });

        //Check to see if a term was passed, and if so, display it
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New Term");
            mNewTerm = true;
        } else {

            setTitle("Edit Term");
            termId = extras.getInt(TERM_KEY_ID);
            mViewModel.loadData(termId);
        }

        //filter to only the courses associated with the Term, based on term id foreign key
        final Observer<List<CourseEntity>> courseObserver = new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {

                courseData.clear();
                for(CourseEntity c: courseEntities)
                    if(c.getFkTermId() == getIntent().getIntExtra(TERM_KEY_ID, 0))
                        courseData.add(c);

                if (mAdapter == null) {
                    mAdapter = new CourseAdapter(courseData, TermDetailActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        mViewModel.mCourse.observe(this, courseObserver);

    }


    //*****************************************************************************************
    //menu methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_termdetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Save Button
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete_term) {
            mViewModel.deleteTerm();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Back Button
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveTerm(mEditTextTermName.getText().toString(), myCalendarStart.getTime(), myCalendarEnd.getTime()); //To send Name and Dates
        finish();
    }

    //**************************************************************************************
    //Date Picker Methods

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void setLabelStart(TermEntity termsEntity) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextStartDate.setText(sdf.format(termsEntity.getTermStart()));
        myCalendarStart.setTime(termsEntity.getTermStart());
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    private void setLabelEnd(TermEntity termsEntity) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextEndDate.setText(sdf.format(termsEntity.getTermEnd()));
        myCalendarEnd.setTime(termsEntity.getTermEnd());
    }

}
