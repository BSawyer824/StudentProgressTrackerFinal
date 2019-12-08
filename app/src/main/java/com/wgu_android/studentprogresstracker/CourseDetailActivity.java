package com.wgu_android.studentprogresstracker;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wgu_android.studentprogresstracker.Adapters.AssessmentsAdapter;
import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;
import com.wgu_android.studentprogresstracker.ViewModels.CourseDetailViewModel;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_KEY_ID;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_NEW;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_STATUS;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_TERM_ID;

public class CourseDetailActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    //**************************************************
    //Course Variables
    @BindView(R.id.spinner_status)
    Spinner spinner;

    @BindView(R.id.editTextCourseName)
    EditText mEditTextCourseName;

    @BindView(R.id.editTextCourseStart)
    EditText mEditTextStartDate;

    @BindView(R.id.editTextCourseEnd)
    EditText mEditTextEndDate;

    @BindView(R.id.editTextMentor)
    EditText mEditTextMentor;

    @BindView(R.id.editTextPhone)
    EditText mEditTextPhone;

    @BindView(R.id.editTextEmail)
    EditText mEditTextEmail;

    @BindView(R.id.btnCourseNotes)
    Button mBtnCourseNotes;

    @BindView(R.id.textViewTestStatus)
    TextView mTestStatus;

    @BindView(R.id.textView_CourseTermId)
    TextView mTextViewCourseTermId;

    @BindView(R.id.textView_CourseTermName)
    TextView mTextViewTermName;

    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    private CourseDetailViewModel mViewModel;
    private String spinnerSelectedItem;
    private boolean mNewCourse;
    private int courseTermId;

    //****************************************************************************
    //Assessment Variables
    @BindView(R.id.recyclerViewAssessments)
    RecyclerView mRecyclerView;

    private List<AssessmentEntity> assessmentData = new ArrayList<>();
    private AssessmentsAdapter mAdapter;

    //****************************************************************************
    //Main Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        initSpinner();
        initRecyclerView();
        initViewModel();

        //TODO notes should be on an additional screen, with buttons to add or view optional note
        //TODO optional note should be able to be shared via eitehr email or sms


        //Add a new Assessment to the Course
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                new DatePickerDialog(CourseDetailActivity.this, startDate, myCalendarStart
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
                new DatePickerDialog(CourseDetailActivity.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    //*****************************************************************************
    //View Model and Recycler View Methods
    private void initViewModel() {

        mViewModel = ViewModelProviders.of(this).get(CourseDetailViewModel.class);

        //Observe the Course being created/edited
        mViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {
            @Override
            public void onChanged(CourseEntity courseEntity) {
                mEditTextCourseName.setText(courseEntity.getCourseName());
                setLabelStart(courseEntity);
                setLabelEnd(courseEntity);
                mEditTextMentor.setText(courseEntity.getCourseMentorName());
                mEditTextPhone.setText(courseEntity.getCourseMentorPhone());
                mEditTextEmail.setText(courseEntity.getCourseMentorEmail());
                mTestStatus.setText(courseEntity.getCourseStatus());
                spinnerSelectedItem = courseEntity.getCourseStatus();
                mTextViewCourseTermId.setText(Integer.toString(courseEntity.getFkTermId()));
                mTextViewTermName.setText(courseEntity.getFkTermName());
                //TODO how do I pull in the course status and assign it to the spinner
            }
        });

        //Check to see if a course was passed, and if so, display it
        final Bundle extras = getIntent().getExtras();
        if (extras.getBoolean(COURSE_NEW) == true) {
            setTitle("New Course");
            mNewCourse = true;
            courseTermId = extras.getInt(COURSE_TERM_ID);
            mTextViewCourseTermId.setText(Integer.toString(courseTermId));
        } else {
            setTitle("Edit Course");
            int courseId = extras.getInt(COURSE_KEY_ID);
            courseTermId = extras.getInt(COURSE_TERM_ID);
            spinnerSelectedItem = extras.getString(COURSE_STATUS);
            mViewModel.loadData(courseId);
        }

        //Observe Assessment Recycler View for changes - show only Associated Assessments
        final Observer<List<AssessmentEntity>> assessmentObserver = new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                assessmentData.clear();

                for(AssessmentEntity a: assessmentEntities)
                    if(a.getFkCourseId() == extras.getInt(COURSE_KEY_ID, 0))
                        assessmentData.add(a);

                if (mAdapter == null) {
                    mAdapter = new AssessmentsAdapter(assessmentData, CourseDetailActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        mViewModel.mAssessment.observe(this, assessmentObserver);

    }


    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }

    //*****************************************************************************************
    //menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursedetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Save Button
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete_course) {
            mViewModel.deleteCourse();
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

        //Save Course to DB
        spinnerSelectedItem = spinner.getSelectedItem().toString();
        int termId = Integer.parseInt(mTextViewCourseTermId.getText().toString());
        mViewModel.saveCourse(mEditTextCourseName.getText().toString(), myCalendarStart.getTime(),
                myCalendarEnd.getTime(), mEditTextMentor.getText().toString(), mEditTextPhone.getText().toString(),
                mEditTextEmail.getText().toString(), spinnerSelectedItem, termId, mTextViewTermName.getText().toString());

        finish();
    }



    //***************************************************************************************
    //spinner methods
    private void initSpinner() {

        ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_status, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mSpinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        //TODO not correctly displaying the status in the spinner from the DB
        if (spinnerSelectedItem != null) {
            int spinnerPosition = mSpinnerAdapter.getPosition(spinnerSelectedItem);
            spinner.setSelection(spinnerPosition);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String mSelectedItem = parent.getItemAtPosition(position).toString();
        spinnerSelectedItem = mSelectedItem;

        //Toast.makeText(parent.getContext(), spinnerSelectedItem, Toast.LENGTH_SHORT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //**************************************************************************************
    //Date Picker Methods
    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void setLabelStart(CourseEntity coursesEntity) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextStartDate.setText(sdf.format(coursesEntity.getCourseStart()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    private void setLabelEnd(CourseEntity coursesEntity) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextEndDate.setText(sdf.format(coursesEntity.getCourseEnd()));
    }
}
