package vn.edu.dut.itf.e_market.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.com.brycen.restaurant.R;
import vn.com.brycen.restaurant.adapters.AppBaseAdapter.IItemClickListener;
import vn.com.brycen.restaurant.adapters.SuggestsAdapter;
import vn.com.brycen.restaurant.models.Food;
import vn.com.brycen.restaurant.tasks.GetSuggestTask;
import vn.com.brycen.restaurant.utils.CommonUtils;
import vn.com.brycen.restaurant.utils.Navigation;
import vn.com.brycen.restaurant.views.DividerItemDecoration;
import vn.edu.dut.itf.e_market.fragments.BaseFragment;

/**
 * @author d_quang
 */
public class SuggestsFragment extends BaseFragment {
    public static final String ARG_IS_COLLAPSIBLE="is_collapsible";
    public static final String ARG_IS_TAKE_AWAY="is_take_away";
    private RecyclerView rvSuggests;
    private TextView vCollapse;
    private View vTitle;
    private GetSuggestTask task;
    private View vLoading;
    private View vError;
    private boolean mIsTakeAway;
    private boolean mIsCollapsible;
    private View vContent;

    @Override
    protected void processArguments() {
        Bundle bundle=getArguments();
        mIsCollapsible=bundle.getBoolean(ARG_IS_COLLAPSIBLE,false);
        mIsTakeAway=bundle.getBoolean(ARG_IS_TAKE_AWAY,false);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_suggests;
    }

    @Override
    protected void showData() {
        setData(new ArrayList<Food>(),mIsCollapsible);
        load(mIsTakeAway,mIsCollapsible);
    }

    public void onRefresh(){
        load(mIsTakeAway,mIsCollapsible);
    }

    public void load(boolean isTakeAway, final boolean isCollapsible) {
        this.mIsTakeAway=isTakeAway;
        this.mIsCollapsible =isCollapsible;
        task = new GetSuggestTask(getActivity(), isTakeAway) {

            @Override
            protected String doInBackground(Void... params) {
                return super.doInBackground(params);
            }

            @Override
            protected void onSuccess(ArrayList<Food> suggests) {
                super.onSuccess(suggests);
                setData(suggests,isCollapsible);
            }

            @Override
            protected void onError(int code) {
                super.onError(code);
            }
        };
        task.setErrorView(vError);
        task.setLoadingView(vLoading);
        task.setStatus(requestStatus);
        task.setSnackbarView(vSnackBarView);
        task.execute();
    }

    public void setData(final List<Food> data, boolean isCollapsible) {
        if (data != null) {
            SuggestsAdapter adapter = new SuggestsAdapter(getActivity(), data);
            rvSuggests.setAdapter(adapter);
            adapter.setSnackBarView(vSnackBarView);

            adapter.setOnItemClickListener(new IItemClickListener<Food>() {

                @Override
                public void onItemClick(Food item, int position) {
                    if (CommonUtils.isNetworkConnected(getActivity())) {
                        Navigation.showFoodDetail(getActivity(), item.getId());
                    } else {
                        showNoConnection();
                    }
                }
            });
        }
        if (isCollapsible) {
            vCollapse.setVisibility(View.VISIBLE);
            vCollapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (vContent.getVisibility() == View.VISIBLE) {
                        vContent.setVisibility(View.GONE);
                        vCollapse.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(getActivity(), R.drawable.ic_spinner_arrow_white_up), null, null);
//                        vError.setVisibility(View.GONE);
//                        vLoading.setVisibility(View.GONE);
                    } else {
                        vContent.setVisibility(View.VISIBLE);
                        vCollapse.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(getActivity(), R.drawable.ic_spinner_arrow_white), null, null);
//                        vError.setVisibility(View.VISIBLE);
//                        vLoading.setVisibility(View.VISIBLE);
                    }
                }
            });
//            vContent.setVisibility(View.GONE);
            vTitle.setVisibility(View.GONE);
        } else {
            vTitle.setVisibility(View.VISIBLE);
            vContent.setVisibility(View.VISIBLE);
            vCollapse.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTSnackBarView(View view) {
        super.setTSnackBarView(view);
        if (rvSuggests != null && rvSuggests.getAdapter() != null) {
            ((SuggestsAdapter) rvSuggests.getAdapter()).setSnackBarView(view);
        }
    }

    @Override
    protected void initViews() {
        rvSuggests.setHasFixedSize(true);
        rvSuggests.addItemDecoration(new DividerItemDecoration(getActivity(),
                ContextCompat.getDrawable(getActivity(), R.drawable.adapter_promotional_divider), false, false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        rvSuggests.setLayoutManager(layoutManager);
        vError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(mIsTakeAway, mIsCollapsible);
            }
        });

    }

    @Override
    protected void findViews(View rootView) {
        rvSuggests = (RecyclerView) rootView.findViewById(R.id.list_suggest);
        vCollapse = (TextView) rootView.findViewById(R.id.collapse);
        vTitle = rootView.findViewById(R.id.title);
        vContent = rootView.findViewById(R.id.content);
        vError = rootView.findViewById(R.id.vErrorSuggest);
        vLoading = rootView.findViewById(R.id.vLoadingSuggest);
    }
}
