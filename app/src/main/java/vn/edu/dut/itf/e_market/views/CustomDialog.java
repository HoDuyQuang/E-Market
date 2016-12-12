package vn.edu.dut.itf.e_market.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import vn.com.brycen.restaurant.R;

public class CustomDialog {
	AlertDialog dialog;
	private AlertDialog.Builder builder;
	private View layout;

	public CustomDialog(Context context, int layoutId) {
		builder = new Builder(context);
		layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
		builder.setView(layout);
		builder.setCancelable(false);
	}

	public CustomDialog setCancelButton(String text, View.OnClickListener listener) {
		layout.findViewById(R.id.cancel).setOnClickListener(listener);
		layout.findViewById(R.id.cancel).setVisibility(View.VISIBLE);
		layout.findViewById(R.id.layout_button).setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setOKButton(String text, View.OnClickListener listener) {
		layout.findViewById(R.id.ok).setOnClickListener(listener);
		layout.findViewById(R.id.ok).setVisibility(View.VISIBLE);
		layout.findViewById(R.id.layout_button).setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setTitle(String title) {
		((TextView) layout.findViewById(R.id.title)).setText(title);
		layout.findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setMessage(String message) {
		((TextView) layout.findViewById(R.id.message)).setText(message);
		layout.findViewById(R.id.message).setVisibility(View.VISIBLE);
		return this;
	}

	public AlertDialog show() {
		if (layout.findViewById(R.id.cancel).getVisibility() == View.VISIBLE
				&& layout.findViewById(R.id.ok).getVisibility() == View.VISIBLE) {
			layout.findViewById(R.id.separate).setVisibility(View.VISIBLE);
		}
		return builder.create();
	}
}
