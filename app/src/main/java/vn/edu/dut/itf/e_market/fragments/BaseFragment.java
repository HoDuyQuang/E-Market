package vn.edu.dut.itf.e_market.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import vn.com.brycen.restaurant.R;
import vn.com.brycen.restaurant.models.RequestStatus;
import vn.com.brycen.restaurant.views.notification.TSnackbar;

public abstract class BaseFragment extends Fragment {
    View mRootView;
    protected RequestStatus requestStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStatus = new RequestStatus();
        requestStatus.setFirstSuccess(false);

        if (getArguments() != null) {
            processArguments();
        }
    }

    protected abstract void processArguments();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(setLayout(), container, false);
        this.mRootView = rootView;
        findViews(rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showData();
    }

    protected abstract int setLayout();

    protected abstract void findViews(View rootView);

    protected abstract void initViews();

    protected abstract void showData();

    Toast mToast;

    protected void toast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    protected void toast(int stringId) {
        toast(getString(stringId));
    }

    protected void showNoConnection() {
        if (vSnackBarView != null) {
            TSnackbar.make(vSnackBarView,"<b>"+getString(R.string.snack_network_error)+"</b><br/>"+getString(R.string.no_internet_connection), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_network_unavailable).show();
        } else if (mRootView != null) {
            TSnackbar.make(mRootView,"<b>"+getString(R.string.snack_network_error)+"</b><br/>"+getString(R.string.no_internet_connection), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_network_unavailable).show();
        }
    }

    protected void showSuccessSnack(String message) {
        if (vSnackBarView != null) {
            TSnackbar.make(vSnackBarView, "<b>"+getString(R.string.snack_success)+"</b><br/>"+message, TSnackbar.LENGTH_LONG).setTextColor(R.color.app_green).setIcon(R.drawable.ic_request_ok).show();
        } else if (mRootView != null) {
            TSnackbar.make(mRootView, "<b>"+getString(R.string.snack_success)+"</b><br/>"+message, TSnackbar.LENGTH_LONG).setTextColor(R.color.app_green).setIcon(R.drawable.ic_request_ok).show();
        }
    }
    protected View vSnackBarView;

    public void setTSnackBarView(View view) {
        vSnackBarView = view;
    }
}
