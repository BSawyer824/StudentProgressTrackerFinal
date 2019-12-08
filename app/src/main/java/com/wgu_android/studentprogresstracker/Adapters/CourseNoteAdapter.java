package com.wgu_android.studentprogresstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu_android.studentprogresstracker.CourseNoteActivity;
import com.wgu_android.studentprogresstracker.Entities.CourseEntity;
import com.wgu_android.studentprogresstracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgu_android.studentprogresstracker.Utilities.Constants.COURSE_KEY_ID;

public class CourseNoteAdapter  extends RecyclerView.Adapter<CourseNoteAdapter.ViewHolder> {

    private List<CourseEntity> mCourses;
    private final Context mContext;

    public CourseNoteAdapter(List<CourseEntity> mCourses, Context mContext) {
        this.mCourses = mCourses;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CourseNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_item_list, parent, false);
        return new CourseNoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseNoteAdapter.ViewHolder holder, int position) {
        final CourseEntity course = mCourses.get(position);


        //When course note button is clicked, send course id to next screen
        holder.btnCourseNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseNoteActivity.class);
                intent.putExtra(COURSE_KEY_ID, course.getCourseID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView (R.id.btnCourseNotes)
        Button btnCourseNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
