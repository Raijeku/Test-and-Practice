package Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import raistudio.testandpractice.R;

/**
 * Created by Davquiroga on 9/05/2018.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private static OnRecyclerItemClickListener listener;
    private List<Record> records;
    private Context context;

    public RecordAdapter(List<Record> records, Context context, OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.records = records;
        this.context = context;
        this.listener=onRecyclerItemClickListener;
    }

    public static OnRecyclerItemClickListener getListener() {
        return listener;
    }

    public static void setListener(OnRecyclerItemClickListener listener) {
        RecordAdapter.listener = listener;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_record,parent,false);
        return new RecordAdapter.RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.RecordViewHolder holder, int position) {
        Record record = records.get(position);
        holder.usernameView.setText(record.getUsername());
        int correctQuestions=record.getCorrectQuestions();
        int amountQuesitons=record.getAmountQuestions();
        double proportion = (double)correctQuestions/amountQuesitons;
        double percentage=proportion*100;
        holder.scoreView.setText(String.valueOf(percentage).concat("%"));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public interface OnRecyclerItemClickListener{
        void onRecordItemClicked(View v, int position);
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView usernameView;
        protected TextView scoreView;

        @Override
        public void onClick(View view) {

        }

        public RecordViewHolder(View itemView){
            super(itemView);
            usernameView=itemView.findViewById(R.id.card_record_username_view);
            scoreView=itemView.findViewById(R.id.card_record_score_view);
            itemView.setOnClickListener(this);
        }
    }
}
