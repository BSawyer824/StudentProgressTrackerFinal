package com.wgu_android.studentprogresstracker.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu_android.studentprogresstracker.Entities.TermEntity;
import com.wgu_android.studentprogresstracker.R;
import com.wgu_android.studentprogresstracker.TermDetailActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wgu_android.studentprogresstracker.Utilities.Constants.TERM_KEY_ID;
import static com.wgu_android.studentprogresstracker.Utilities.Constants.TERM_NAME;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {

    private final List<TermEntity> mTerms;
    private final Context mContext;



    public class ViewHolder extends RecyclerView.ViewHolder {
        //manages the view, references any views used to display data
        @BindView(R.id.textView_Term_Name)
        TextView mTextView_TermName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public TermAdapter(List<TermEntity> mTerms, Context mContext) {
        this.mTerms = mTerms;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.term_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //update display of a list item
        final TermEntity term = mTerms.get(position);

        //assign start and end dates to a string
        Date startDate = term.getTermStart();
        Date endDate = term.getTermEnd();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = dateFormat.format(startDate);
        String strDateEnd = dateFormat.format(endDate);
        String label = term.getTermName() + "  \n   " + strDate + "  to  " + strDateEnd;

        //Display Term name and Dates in Recycler View
        holder.mTextView_TermName.setText(label);


        //When a Term is clicked, send selected term to the next activity
        holder.mTextView_TermName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TermDetailActivity.class);
                intent.putExtra(TERM_KEY_ID, term.getTermID());
                intent.putExtra(TERM_NAME, term.getTermName());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

}

