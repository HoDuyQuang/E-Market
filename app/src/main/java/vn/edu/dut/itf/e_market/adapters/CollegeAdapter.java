package vn.edu.dut.itf.e_market.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.College;

public class CollegeAdapter extends BaseAdapter {
    private Context mContext;
    private List<College> districts;

    public CollegeAdapter(Context context, List<College> linkPhoto) {
        mContext = context;
        districts = linkPhoto;
    }

    @Override
    public int getCount() {
        // return mLinkPhoto.size();
        return districts != null ? districts.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // return mLinkPhoto.get(position);
        return districts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder itemHolder;

        if (convertView == null) {
            itemHolder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_sp, parent, false);
            itemHolder.text = (TextView) convertView
                    .findViewById(R.id.text);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (Holder) convertView.getTag();
        }
        itemHolder.text.setText(districts.get(position).getName());
        return convertView;
    }

    class Holder {
        TextView text;
    }
}
