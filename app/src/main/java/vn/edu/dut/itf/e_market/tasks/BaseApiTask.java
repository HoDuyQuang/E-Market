package vn.edu.dut.itf.e_market.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;

import vn.com.brycen.restaurant.R;
import vn.com.brycen.restaurant.activities.BaseActivity;
import vn.com.brycen.restaurant.models.RequestStatus;
import vn.com.brycen.restaurant.utils.Authentication;
import vn.com.brycen.restaurant.utils.CommonUtils;
import vn.com.brycen.restaurant.utils.Navigation;
import vn.com.brycen.restaurant.utils.ServerUnavailableException;
import vn.com.brycen.restaurant.views.LoadingView;
import vn.com.brycen.restaurant.views.notification.TSnackbar;

/**
 * @author d_quang
 *         <p>
 *         Base class for request API
 */
public abstract class BaseApiTask extends AsyncTask<Void, Integer, String> {
    private static final int SUCCESS = 0;
    private static final int TOKEN_INVALID = 7;

    private ProgressDialog mProgressDialog;
    Context mContext;
    private Error mError;
    private View vError;
    private View vLoading;
    private ViewGroup mRootView;
    private SwipeRefreshLayout vSwipeRefresh;
    private AlertDialog mErrorDialog;
    private View mSnackbarView;
    private RequestStatus status;

    private enum Error {
        InternetConnection, UnableConnect, ParsingError
    }

    public BaseApiTask(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showLoading();
        hideError();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        hideLoading();
        if (result == null) {
            if (mError == Error.InternetConnection) {
                onError(-1);
            }
            if (mError == Error.UnableConnect) {
                onError(-2);
            }
            if (mError == Error.ParsingError) {
                onError(-3);
            }
            showError(mError);
            return;
        }

        try {
            Log.d("result", result);
            JSONObject object = new JSONObject(result);
            int code = object.getJSONObject("error").getInt("code");
            if (code == SUCCESS) {
                parseData(object);
                if (mContext instanceof BaseActivity) {
                    if (status != null) {
                        status.setFirstSuccess(true);
                    }
                }
                hideError();
            } else if (code == TOKEN_INVALID){
                Authentication.logout(mContext);
                Navigation.showDialogPasswordChanged(mContext);
                onError(code);
            } else {
                mError = Error.UnableConnect;
                showError(mError);
                onError(code);
            }

        } catch (JSONException e) {
            onError(-3);
            mError= Error.UnableConnect;
            showError(mError);
            e.printStackTrace();
        } catch (Exception e) {
            onError(-3);
            mError= Error.UnableConnect;
            showError(mError);
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        if (!CommonUtils.isNetworkConnected(mContext)) {
            mError = Error.InternetConnection;
            return null;
        }
        try {
            String response = request();
            if (response != null) {
                return response;
            } else {
                mError = Error.ParsingError;
                return null;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            mError = Error.UnableConnect;
            return null;
        } catch (IOException e) {
            mError = Error.UnableConnect;
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            mError = Error.ParsingError;
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            mError = Error.ParsingError;
            e.printStackTrace();
            return null;
        } catch (ServerUnavailableException e) {
            e.printStackTrace();
            mError = Error.UnableConnect;
            return null;
        }
    }

    protected abstract void parseData(JSONObject result) throws JSONException, JsonSyntaxException;

    protected abstract void onError(int code);

    abstract String request() throws IOException, JSONException, ServerUnavailableException;

    protected void showErrorToast(int stringId) {
        Toast.makeText(mContext, mContext.getString(stringId), Toast.LENGTH_SHORT).show();
    }

    protected Context getContext() {
        return mContext;
    }

    public void setErrorView(View view) {
        vError = view;
    }

    public void setLoadingView(View view) {
        vLoading = view;
    }

    public void setLoading(ViewGroup rootView) {
        mRootView = rootView;
        vLoading = new LoadingView(mContext);
        LayoutParams layoutParamsDrawing = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRootView.addView(vLoading, layoutParamsDrawing);
    }

    public void setSwipeRefreshView(SwipeRefreshLayout view) {
        vSwipeRefresh = view;
    }

    public void setSnackbarView(View view) {
        mSnackbarView = view;
    }

    public void setStatus(RequestStatus isFirstSuccess) {
        status = isFirstSuccess;
    }

    private void showError(Error mError) {
        showSnackError(mError);

        if (vError != null) {
            if (status == null || !status.isFirstSuccess()) {
                vError.setVisibility(View.VISIBLE);
            }
        }

        if (mErrorDialog != null) {
            mErrorDialog.setMessage(getErrorMessage(mError));
            mErrorDialog.show();
        }
    }

    protected void showSnackError(Error mError) {
        if (mSnackbarView != null && mContext != null) {
            if (mError == Error.InternetConnection) {
                TSnackbar.make(mSnackbarView, "<b>" + mContext.getString(R.string.snack_network_error) + "</b><br/>" + mContext.getString(R.string.no_internet_connection), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_network_unavailable).show();
                return ;
            }
            if (mError == Error.UnableConnect) {
                TSnackbar.make(mSnackbarView, "<b>" + mContext.getString(R.string.snack_server_error) + "</b><br/>" + mContext.getString(R.string.unable_connect_server), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_server_unavailable).show();
            }
            if (mError == Error.ParsingError) {
                TSnackbar.make(mSnackbarView, "<b>" + mContext.getString(R.string.snack_server_error) + "</b><br/>" + mContext.getString(R.string.unable_perform_action), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_server_unavailable).show();
            }
//            TSnackbar.make(mSnackbarView, getErrorMessage(mError), TSnackbar.LENGTH_LONG).show();
        }
    }

    private String getErrorMessage(Error mError) {
        if (mError == Error.InternetConnection) {
            return mContext.getString(R.string.no_internet_connection);
        }
        if (mError == Error.UnableConnect) {
            return mContext.getString(R.string.unable_connect_server);
        }
        if (mError == Error.ParsingError) {
            return mContext.getString(R.string.unable_perform_action);
        }
        return "";
    }

    private void showLoading() {
        if (vSwipeRefresh != null && vSwipeRefresh.isRefreshing()) {
            if ((status == null || !status.isFirstSuccess()) && vLoading != null) {
                vLoading.setVisibility(View.INVISIBLE);
            }
        } else if (mProgressDialog != null) {
            mProgressDialog.show();
        } else if (vLoading != null) {
            vLoading.setVisibility(View.VISIBLE);
        }
    }

    private void hideError() {
        if (vError != null) {
            vError.setVisibility(View.GONE);
        }
    }

    private void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (vLoading != null) {
            vLoading.setVisibility(View.GONE);
        }
        if (mRootView != null && vLoading != null) {
            mRootView.removeView(vLoading);
        }
        if (vSwipeRefresh != null) {
            vSwipeRefresh.setRefreshing(false);
        }
    }

    public void setShowProgressDialog(String title, String message, boolean isCancelable) {
        mProgressDialog = new ProgressDialog(mContext);
        if (title != null) {
            mProgressDialog.setTitle(title);
        }
        if (message != null) {
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.setCancelable(isCancelable);
    }

    public void setErrorDialog(String positiveButtonText,
                               DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        Builder builder = new Builder(getContext());
//		if (title != null) {
//			builder.setTitle(title);
//		}
//		if (message != null) {
//			builder.setMessage(message);
//		}
        if (positiveButtonText != null) {
//			builder.setCancelable(isCancelable);
            builder.setCancelable(false);
            builder.setPositiveButton(positiveButtonText, onPositiveClick);
            builder.setNegativeButton(getContext().getString(R.string.cancel), onNegativeClick);
            builder.setMessage(R.string.load_data_failure);
        }
        mErrorDialog = builder.create();
    }
}
