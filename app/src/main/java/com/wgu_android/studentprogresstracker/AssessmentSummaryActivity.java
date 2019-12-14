package com.wgu_android.studentprogresstracker;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wgu_android.studentprogresstracker.Adapters.AssessmentsAdapter;
import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;
import com.wgu_android.studentprogresstracker.ViewModels.AssessmentSummaryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssessmentSummaryActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView_AllAssessments)
    RecyclerView mRecyclerView;

    private List<AssessmentEntity> assessmentData = new ArrayList<>();
    private AssessmentsAdapter mAdapter;
    private AssessmentSummaryViewModel mViewModel;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {
        final Observer<List<AssessmentEntity>> assessmentObserver = new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                assessmentData.clear();
                assessmentData.addAll(assessmentEntities);

                if (mAdapter == null) {
                    mAdapter = new AssessmentsAdapter(assessmentData, AssessmentSummaryActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    //refreshes from adapter when data changes
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        mViewModel = ViewModelProviders.of(this)
                .get(AssessmentSummaryViewModel.class);
        mViewModel.mAssessment.observe(this, assessmentObserver); //subscribed to the data
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManagerTerms = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManagerTerms);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), layoutManagerTerms.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }


}
