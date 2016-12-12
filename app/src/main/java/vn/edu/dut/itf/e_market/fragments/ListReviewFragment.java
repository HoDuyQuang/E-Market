package vn.edu.dut.itf.e_market.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.adapters.ReviewAdapter;
import vn.edu.dut.itf.e_market.models.Review;
import vn.edu.dut.itf.e_market.tasks.GetListReviewRestaurantTask;


public class ListReviewFragment extends BaseFragment {
    public static final int REVIEW_LOGIN_CODE = 2;

    protected RecyclerView rvReviews;
    protected SwipeRefreshLayout vRefresh;
    protected View vError;
    protected View vLoading;
    protected View tvEmptyCart;
    protected boolean isLoadMore = false;

    @Override
    public int setLayout() {
        return R.layout.fragment_list_review_res;
    }

    @Override
    protected void findViews(final View rootView) {
        rvReviews = (RecyclerView) rootView.findViewById(R.id.rec_frg_list_review_res);
        vRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        vError = rootView.findViewById(R.id.tvErrorMessage);
        vLoading = rootView.findViewById(R.id.request_progress);
        tvEmptyCart = rootView.findViewById(R.id.tvCartEmpty);
    }

    @Override
    public void initViews() {
        vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                request(0, 10);
            }
        });
        vError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoadMore = false;
                request(0, 10);
            }
        });

        rvReviews.setHasFixedSize(true);
//        rvReviews.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && layoutManager.findLastVisibleItemPosition() >= getCount() - 3) {
                    request(getCount(), 10);
                }
            }
        });
    }

    @Override
    public void showData() {
        isLoadMore = false;
        RecycleFill();
		request(0,10);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REVIEW_LOGIN_CODE:
                    onAddReview();
                    break;
            }
        }
    }


    private List<Review> mListReviews;


    void onAddReview() {
//        startActivityForResult(new Intent(ListReviewRestaurantActivity.this, RestaurantReviewActivity.class), REVIEW_COMPOSE_REQUEST_CODE);
    }

    int getCount() {
        return mListReviews.size();
    }


    GetListReviewRestaurantTask task;
    boolean isRunning = false;
    int lastStartIndex = -1;

    void request(int start, int count) {
        if (isLoadMore) {
            if (start > lastStartIndex) {
                lastStartIndex = start;
            } else {
                return;
            }
        } else {
            lastStartIndex = -1;
        }
        if (!isRunning) {
            task = new GetListReviewRestaurantTask(getActivity(), start, count) {
                @Override
                protected void onPreExecute() {
                    isRunning = true;
                    super.onPreExecute();

                }

                @Override
                protected void onPostExecute(String result) {
                    isRunning = false;
                    super.onPostExecute(result);
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    isRunning = false;
                }

                protected void onSuccess(ArrayList<Review> info) {
                    if (isLoadMore) {
                        int size = mListReviews.size();
                        for (Review review : info) {
                            boolean isContain = false;
                            for (Review item : mListReviews) {
                                if (review.getId().equals(item.getId())) {
                                    isContain = true;
                                    break;
                                }
                            }
                            if (!isContain) {
                                mListReviews.add(review);
                            }
                        }
                        adapter.notifyItemRangeInserted(size, mListReviews.size() - size);
                    } else {
                        mListReviews.clear();
                        mListReviews.addAll(info);
                        adapter.notifyDataSetChanged();
                    }

                    if (!isLoadMore) {
//                        numberReview.setText(getString(R.string.number_reviews, reviewCount));
//                        averageReviewPoint.setText(CommonUtils.formatRate(ListReviewRestaurantActivity.this, rate.getAverage()));
//                        SetRatePopUp(rate);
//                        tvRateString.setText(CommonUtils.getRateString(ListReviewRestaurantActivity.this, rate.getAverage()));
                    }

                    isLoadMore = true;
                }

                @Override
                protected void onError(int code) {
                    super.onError(code);
                    lastStartIndex = -1;
                    isRunning = false;
                }
            };
//		task.setShowProgressDialog(null, getString(R.string.loading), false);
            task.setSwipeRefreshView(vRefresh);
            task.setErrorView(vError);
//			task.setSnackbarView(findViewById(R.id.view_data));
            if (!isLoadMore) {
                task.setLoadingView(vLoading);
            }
            task.setStatus(requestStatus);
            task.execute();
        }
    }

    /**
     * Fill recycle
     *
     * @param info
     */
    ReviewAdapter adapter;

    protected void RecycleFill() {
        mListReviews = new ArrayList<>();
//        mListReviews.add(new Review("0", "Manchester", Calendar.getInstance().getTime(), "Transfer winter", "100 dollars", 10, 1));
//        mListReviews.add(new Review("0", "Manchester", Calendar.getInstance().getTime(), "Transfer winter", "100 dollars", 10, 1));
//        mListReviews.add(new Review("0", "Manchester", Calendar.getInstance().getTime(), "Transfer winter", "100 dollars", 10, 1));
//        mListReviews.add(new Review("0", "Manchester", Calendar.getInstance().getTime(), "Transfer winter", "100 dollars", 10, 1));
//        mListReviews.add(new Review("0", "Manchester", Calendar.getInstance().getTime(), "Transfer winter", "100 dollars", 10, 1));
        adapter = new ReviewAdapter(getActivity(), mListReviews);

//		adapter.setSnackBarView(findViewById(R.id.root_view));
        rvReviews.setAdapter(adapter);
    }

    @Override
    protected void processArguments() {

    }


}
