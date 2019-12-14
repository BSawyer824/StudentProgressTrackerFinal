package com.wgu_android.studentprogresstracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;
import com.wgu_android.studentprogresstracker.Utilities.AssessmentDueReceiver;
import com.wgu_android.studentprogresstracker.Utilities.AssessmentGoalReceiver;
import com.wgu_android.studentprogresstracker.Utilities.CourseStartReceiver;
import com.wgu_android.studentprogresstracker.ViewModels.AssessmentDetailViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgu_android.studentprogresstracker.Utilities.Constants.ASSESMENT_KEY_ID;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.ASSESSMENT_COURSE_ID;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.ASSESSMENT_TYPE;

public class AssessmentDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    //**************************************************
    //Assessment Variables
    @BindView(R.id.spinnerAssessType)
    Spinner spinnerAssessment;

    @BindView(R.id.editTextAssessmentName)
    EditText mEditTextAssessmentName;

    @BindView(R.id.editTextGoalDate)
    EditText mEditTextGoalDate;

    @BindView(R.id.editTextDueDate)
    EditText mEditTextDueDate;

    @BindView(R.id.textViewCourseId_Assessments)
    TextView mTextViewCourseId;

    final Calendar myCalendarGoal = Calendar.getInstance();
    final Calendar myCalendarDue = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener goalDate;
    DatePickerDialog.OnDateSetListener dueDate;
    private String spinnerSelectedItem;
    private AssessmentDetailViewModel mViewModel;
    private Boolean mNewAssessment;
    private int courseId;
    private int assessmentId;

    //**************************************************
    //Main Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_save);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        initViewModel();
        initSpinner();

        //*****************************************************
        //Make Goal and Due Date fields have a calendar date picker

        //Goal Date - Date Picker
        goalDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarGoal.set(Calendar.YEAR, year);
                myCalendarGoal.set(Calendar.MONTH, monthOfYear);
                myCalendarGoal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelGoal();
            }
        };

        mEditTextGoalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AssessmentDetailActivity.this, goalDate, myCalendarGoal
                        .get(Calendar.YEAR), myCalendarGoal.get(Calendar.MONTH),
                        myCalendarGoal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Due Date - Date Picker
        dueDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarDue.set(Calendar.YEAR, year);
                myCalendarDue.set(Calendar.MONTH, monthOfYear);
                myCalendarDue.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDue();
            }
        };

        mEditTextDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AssessmentDetailActivity.this, dueDate, myCalendarDue
                        .get(Calendar.YEAR), myCalendarDue.get(Calendar.MONTH),
                        myCalendarDue.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    //*****************************************************************************************
    //recycler view methods

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AssessmentDetailViewModel.class);

        mViewModel.mLiveAssessment.observe(this, new Observer<AssessmentEntity>() {
            @Override
            public void onChanged(AssessmentEntity assessmentEntity) {
                if (assessmentEntity != null) {
                    mEditTextAssessmentName.setText(assessmentEntity.getAssessmentName());
                    setLabelGoal(assessmentEntity);
                    setLabelDue(assessmentEntity);
                    mTextViewCourseId.setText(Integer.toString(assessmentEntity.getFkCourseId()));
                    spinnerSelectedItem = assessmentEntity.getAssessmentType();
                }
            }
        });

        //Check to see if a term was passed, and if so, display it
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New Assessment");
            mNewAssessment = true;
        } else {
            setTitle("Edit Assessment");
            assessmentId = extras.getInt(ASSESMENT_KEY_ID);
            courseId = extras.getInt(ASSESSMENT_COURSE_ID);
            spinnerSelectedItem = extras.getString(ASSESSMENT_TYPE);
            mViewModel.loadData(assessmentId);
        }
    }



    //*****************************************************************************************
    //menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessmentdetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Save Button
        int id = item.getItemId();
        if (id == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (id == R.id.action_delete_assessment) {
            mViewModel.deleteAssessment();
            finish();
        } else if (id == R.id.action_assessment_alarm_due) {
            setAssessmentAlarm(myCalendarDue, "Due");
        } else if (id == R.id.action_assessment_alarm_goal) {
            setAssessmentAlarm(myCalendarGoal, "Goal");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAssessmentAlarm(Calendar mCalendarAlarmDate, String title) {


        mCalendarAlarmDate.set(Calendar.HOUR_OF_DAY, 0);
        mCalendarAlarmDate.set(Calendar.MINUTE, 0);
        mCalendarAlarmDate.set(Calendar.SECOND, 0);
        mCalendarAlarmDate.set(Calendar.MILLISECOND, 0);

        long alarmDelay = (mCalendarAlarmDate.getTimeInMillis() + 28800000) - System.currentTimeMillis(); //will go off at 8am
        //long alarmDelay = (mCalendarAlarmDate.getTimeInMillis() + 77760000) - System.currentTimeMillis(); //test alarm 9:30 pm

        Intent intent;
        if (title == "Due") {
            intent = new Intent(AssessmentDetailActivity.this, AssessmentDueReceiver.class);
            Toast toast=Toast.makeText(getApplicationContext(),"Alarm set for 8am the day the Assessment is Due ",Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
            toast.show();
        } else {
            intent = new Intent(AssessmentDetailActivity.this, AssessmentGoalReceiver.class);
            Toast toast=Toast.makeText(getApplicationContext(),"Alarm set for 8am the day of the Assessment Goal ",Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
            toast.show();
        }
        PendingIntent sender= PendingIntent.getBroadcast(AssessmentDetailActivity.this,0,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+alarmDelay, sender);


    }


    @Override
    public void onBackPressed() {
        //Back Button
        saveAndReturn();
    }

    private void saveAndReturn() {

        spinnerSelectedItem = spinnerAssessment.getSelectedItem().toString();
        mViewModel.saveAssessment(mEditTextAssessmentName.getText().toString(), myCalendarGoal.getTime(),
                myCalendarDue.getTime(), spinnerSelectedItem, courseId);

        finish();
    }

    //**********************************************************************
    //Spinner Methods
    private void initSpinner() {
        ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_types, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssessment.setAdapter(mSpinnerAdapter);
        spinnerAssessment.setOnItemSelectedListener(this);

        if (spinnerSelectedItem != null) {
            spinnerAssessment.setSelection(getSpinnerPosition(spinnerAssessment, spinnerSelectedItem));
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String mSelectedItem = parent.getItemAtPosition(position).toString();
        spinnerSelectedItem = mSelectedItem;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public int getSpinnerPosition(Spinner spinner, String mSpinnerSelection) {
        int position = 0;

        for (int i=0; i<spinner.getCount(); i++)
            if(spinner.getItemAtPosition(i).equals(mSpinnerSelection))
                position = i;

        return position;
    }

    //**************************************************************************************
    //Date Picker Methods
    private void updateLabelGoal() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextGoalDate.setText(sdf.format(myCalendarGoal.getTime()));
    }

    private void setLabelGoal(AssessmentEntity assessmentEntity) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextGoalDate.setText(sdf.format(assessmentEntity.getAssessmentGoalDate().getTime()));
        myCalendarGoal.setTime(assessmentEntity.getAssessmentGoalDate());
    }

    private void updateLabelDue() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextDueDate.setText(sdf.format(myCalendarDue.getTime()));
    }

    private void setLabelDue(AssessmentEntity assessmentEntity) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditTextDueDate.setText(sdf.format(assessmentEntity.getAssessmentDueDate().getTime()));
        myCalendarDue.setTime(assessmentEntity.getAssessmentDueDate());
    }

}
