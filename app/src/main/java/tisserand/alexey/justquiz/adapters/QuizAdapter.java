package tisserand.alexey.justquiz.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.constants.AppConstants;
import tisserand.alexey.justquiz.data.preference.AppPreference;
import tisserand.alexey.justquiz.listeners.ListItemClickListener;


public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {


    private Activity mActivity;

    private ArrayList<String> mItemList;
    private ArrayList<String> mColorList;
    private ListItemClickListener mItemClickListener;

    public QuizAdapter(Context mContext, Activity mActivity, ArrayList<String> mItemList, ArrayList<String> mColorList) {

        this.mActivity = mActivity;
        this.mItemList = mItemList;
        this.mColorList = mColorList;
    }

    public void setItemClickListener(ListItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizAdapter.ViewHolder(view, mActivity, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvItemTitle;
        private CardView lytContainer;
        private ListItemClickListener itemClickListener;
        private int sizeTitle;

        private ViewHolder(View itemView, Activity mActivity, ListItemClickListener itemClickListener) {
            super(itemView);
            sizeTitle = AppPreference.getInstance(mActivity).getInt(AppConstants.KEY_SA, AppConstants.BUNDLE_KEY_DEFAULT_SA);
            this.itemClickListener = itemClickListener;
            tvItemTitle = itemView.findViewById(R.id.answer_text);
            tvItemTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeTitle);
            lytContainer = itemView.findViewById(R.id.card_view);

            lytContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);

    }

    @Override
    public void onBindViewHolder(QuizAdapter.ViewHolder mainHolder, int position) {
        final String model = mItemList.get(position);
        final String model1 = mColorList.get(position);

        mainHolder.tvItemTitle.setText(Html.fromHtml(model));
        mainHolder.tvItemTitle.setBackgroundResource(mActivity.getResources().getIdentifier(model1, "drawable", mActivity.getPackageName()));

    }
}
