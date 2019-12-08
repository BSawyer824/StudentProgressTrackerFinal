package com.wgu_android.studentprogresstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu_android.studentprogresstracker.AssessmentDetailActivity;
import com.wgu_android.studentprogresstracker.Entities.AssessmentEntity;
import com.wgu_android.studentprogresstracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgu_android.studentprogresstracker.Utilities.Constants.ASSESMENT_KEY_ID;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.ASSESSMENT_COURSE_ID;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.ASSESSMENT_NAME;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.ASSESSMENT_NEW;

public class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.ViewHolder> {

    private final List<AssessmentEntity> mAssessments;
    private final Context mContext;

    public AssessmentsAdapter(List<AssessmentEntity> mAssessments, Context mContext) {
        this.mAssessments = mAssessments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assessment_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AssessmentEntity assessment = mAssessments.get(position);

        //assign start and end dates to a string
        Date goalDate = assessment.getAssessmentGoalDate();
        Date dueDate = assessment.getAssessmentDueDate();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String strDateGoal = dateFormat.format(goalDate);
        String strDateDue = dateFormat.format(dueDate);
        String label = assessment.getAssessmentName() + "\n   Goal Date: " + strDateGoal + ".\n   Due Date: " + strDateDue;


        holder.mTextViewAssessmentName.setText(label);

        //When a Course is clicked in the recycler view, send selected course to the next activity
        holder.mTextViewAssessmentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AssessmentDetailActivity.class);
                intent.putExtra(ASSESMENT_KEY_ID, assessment.getAssessmentID());
                intent.putExtra(ASSESSMENT_NAME, assessment.getAssessmentName());
                intent.putExtra(ASSESSMENT_COURSE_ID, assessment.getFkCourseId());
                intent.putExtra(ASSESSMENT_NEW, false);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_Assessment_Name)
        TextView mTextViewAssessmentName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
