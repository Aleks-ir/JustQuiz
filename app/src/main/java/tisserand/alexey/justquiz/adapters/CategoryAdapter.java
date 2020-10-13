package tisserand.alexey.justquiz.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.listeners.ListItemClickListener;
import tisserand.alexey.justquiz.models.quiz.CategoryModel;


 public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<CategoryModel> categoryList;
    private ListItemClickListener itemClickListener;

    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    public void setItemClickListener(ListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_recycler, parent, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        final CategoryModel model = categoryList.get(position);
        String categoryName = model.getCategoryName();
        holder.tvCategoryTitle.setText(Html.fromHtml(categoryName));

        switch (position) {
            case 0:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_space));
                break;
            case 1:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_animals));
                break;
            case 2:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_sport));
                break;
            case 3:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_equipment));
                break;
            case 4:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_mythology));
                break;
            case 5:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_travels));
                break;
            case 6:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_puzzles));
                break;
            case 7:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_medicine));
                break;
            case 8:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_religion));
                break;
            case 9:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_flags));
                break;
            case 10:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_plants));
                break;
            case 11:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_society));
                break;
            case 12:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_art));
                break;
            case 13:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_history));
                break;
            case 14:
                holder.lytContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_planet_earth));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return (null != categoryList ? categoryList.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout lytContainer;
        private TextView tvCategoryTitle;
        private ListItemClickListener itemClickListener;

        private ViewHolder(View itemView, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            lytContainer = itemView.findViewById(R.id.lytContainer);
            tvCategoryTitle = itemView.findViewById(R.id.titleText);

            lytContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }

        }
    }
}
