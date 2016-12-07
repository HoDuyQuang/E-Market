package vn.edu.dut.itf.e_market.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.dut.itf.e_market.R;


/**
 * @author d_quang
 */
public class LanguageAdapter extends AppBaseAdapter<String, LanguageAdapter.LanguageViewHolder> {
    private int mSelectedPosition = 0;
    private boolean isSettingPage;

    public LanguageAdapter(Context context, ArrayList<String> arrayList) {
        super(context, arrayList);
    }

    public LanguageAdapter(Context context, ArrayList<String> arrayList, boolean isSettingPage) {
        this(context, arrayList);
        this.isSettingPage = isSettingPage;
    }

    @Override
    void onBind(LanguageViewHolder mainHolder, final String model, final int position) {
        mainHolder.language.setText(model);
        if (position == mSelectedPosition) {
            mainHolder.check.setVisibility(View.VISIBLE);
            if (!isSettingPage) {
                mainHolder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
                mainHolder.language.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
                mainHolder.language.setTypeface(null, Typeface.BOLD);
            }
        } else {
            mainHolder.check.setVisibility(View.INVISIBLE);
            if (!isSettingPage) {
                mainHolder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
                mainHolder.language.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                mainHolder.language.setTypeface(null, Typeface.NORMAL);
            }

        }

        mainHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSelectedPosition = position;
                notifyDataSetChanged();
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(model, position);
                }
            }
        });

        // Fix font for Myanmar language Android version < Kitkat
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mainHolder.language.setTypeface(
                        Typeface.createFromAsset(
                                mContext.getAssets(), "fonts/" + mContext.getString(R.string.myanmar_font)));
        }
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup;
        if (isSettingPage) {
            mainGroup = (ViewGroup) mInflater.inflate(R.layout.adapter_language_setting_item, viewGroup, false);
        } else {
            mainGroup = (ViewGroup) mInflater.inflate(R.layout.adapter_language_item, viewGroup, false);
        }
        return new LanguageViewHolder(mainGroup);
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        public TextView language;
        public Button check;

        LanguageViewHolder(View view) {
            super(view);
            this.language = (TextView) view.findViewById(R.id.language);
            this.check = (Button) view.findViewById(R.id.check);

        }
    }
}
