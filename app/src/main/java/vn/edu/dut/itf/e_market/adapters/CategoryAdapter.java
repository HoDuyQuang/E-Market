package vn.edu.dut.itf.e_market.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.Category;

public class CategoryAdapter extends BaseAdapter {
	private final Context mContext;
	private List<Category> address;

	public CategoryAdapter(Context context, List<Category> linkPhoto) {
		mContext = context;
		address = linkPhoto;
	}

	@Override
	public int getCount() {
		return address.size();
	}

	@Override
	public Object getItem(int position) {
		return address.get(position);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sp, parent, false);
			itemHolder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(itemHolder);
		} else {
			itemHolder = (Holder) convertView.getTag();
		}
		itemHolder.text.setText(address.get(position).getCategory());
		return convertView;
	}

	class Holder {
		TextView text;
	}
}
