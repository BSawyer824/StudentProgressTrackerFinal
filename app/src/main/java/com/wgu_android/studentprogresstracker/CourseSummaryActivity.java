package com.wgu_android.studentprogresstracker;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wgu_android.studentprogresstracker.Adapters.CourseAdapter;
import com.wgu_android.studentprogresstracker.Adapters.TermAdapter;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;
import com.wgu_android.studentprogresstracker.Entities.TermEntity;
import com.wgu_android.studentprogresstracker.ViewModels.CourseSummaryViewModel;
import com.wgu_android.studentprogresstracker.ViewModels.TermSummaryViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseSummaryActivity extends AppCompatActivity {

    //TODO set up notifications for course start and end dates


    @BindView(R.id.recyclerView_AllCourses)
    RecyclerView mRecyclerView;

    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseAdapter mAdapter;
    private CourseSummaryViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {
        final Observer<List<CourseEntity>> courseObserver = new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                courseData.clear();
                courseData.addAll(courseEntities);

                if (mAdapter == null) {
                    mAdapter = new CourseAdapter(courseData, CourseSummaryActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    //refreshes from adapter when data changes
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        mViewModel = ViewModelProviders.of(this)
                .get(CourseSummaryViewModel.class);
        mViewModel.mCourses.observe(this, courseObserver); //subscribed to the data
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManagerTerms = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManagerTerms);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), layoutManagerTerms.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }

}
