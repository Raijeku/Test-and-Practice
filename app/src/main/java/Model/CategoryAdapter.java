package Model;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import raistudio.testandpractice.R;

/**
 * Created by Davquiroga on 4/04/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private static OnRecyclerItemClickListener listener;
    private List<Category> categories;
    private Context context;

    public CategoryAdapter(List<Category> categories, Context context, OnRecyclerItemClickListener onRecyclerItemClickListener){
        this.categories=categories;
        this.listener=onRecyclerItemClickListener;
        this.context=context;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.titleView.setText(category.getName());
        holder.descriptionView.setText(category.getDescription());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface OnRecyclerItemClickListener{
        void onCategoryItemClicked(View v, int position);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView titleView;
        protected TextView descriptionView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            titleView=(TextView) itemView.findViewById(R.id.card_title_view);
            descriptionView=(TextView) itemView.findViewById(R.id.card_description_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //view.setBackgroundColor(Color.argb(100,50,50,50));
            listener.onCategoryItemClicked(view,this.getLayoutPosition());
        }
    }
}
