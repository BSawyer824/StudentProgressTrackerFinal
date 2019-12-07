package com.wgu_android.studentprogresstracker;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wgu_android.studentprogresstracker.Adapters.TermAdapter;
import com.wgu_android.studentprogresstracker.Entities.TermEntity;
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

public class TermSummaryActivity extends AppCompatActivity {



    @BindView(R.id.recyclerView_Terms)
    RecyclerView mRecyclerView;

    private List<TermEntity> termData = new ArrayList<>();
    private TermAdapter mAdapter;
    private TermSummaryViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewModel() {
        final Observer<List<TermEntity>> termObserver = new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                termData.clear();
                termData.addAll(termEntities);

                if (mAdapter == null) {
                    mAdapter = new TermAdapter(termData, TermSummaryActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    //refreshes from adapter when data changes
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        mViewModel = ViewModelProviders.of(this)
                .get(TermSummaryViewModel.class);
        mViewModel.mTerms.observe(this, termObserver); //subscribed to the data
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManagerTerms = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManagerTerms);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), layoutManagerTerms.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }

}
