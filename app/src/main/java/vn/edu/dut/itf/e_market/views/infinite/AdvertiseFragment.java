package vn.edu.dut.itf.e_market.views.infinite;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.fragments.BaseFragment;


/**
 * Example Fragment class that shows an identifier inside a TextView.
 */
public class AdvertiseFragment extends BaseFragment {

	public static final String ARG_IMAGE_URL = "image_url";
	private String url;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("dummy", true);
	}

	@Override
	protected void processArguments() {
		Bundle args = getArguments();
		url = args.getString(ARG_IMAGE_URL);
	}

	@Override
	protected int setLayout() {
		return R.layout.fragment_advertise;
	}

	@Override
	protected void findViews(View rootView) {
		ImageView image=(ImageView) rootView.findViewById(R.id.image);
		Picasso.with(getActivity()).load(url).placeholder(R.drawable.ic_no_photo_available).into(image);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void showData() {
		// TODO Auto-generated method stub

	}
}
