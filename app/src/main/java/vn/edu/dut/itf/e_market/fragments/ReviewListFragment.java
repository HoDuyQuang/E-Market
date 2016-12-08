//package vn.edu.dut.itf.e_market.fragments;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import vn.edu.dut.itf.e_market.R;
//import vn.edu.dut.itf.e_market.activities.ListReviewRestaurantActivity;
//import vn.edu.dut.itf.e_market.adapters.ReviewAdapter;
//import vn.edu.dut.itf.e_market.models.Review;
//import vn.edu.dut.itf.e_market.utils.Authentication;
//import vn.edu.dut.itf.e_market.utils.CommonUtils;
//import vn.edu.dut.itf.e_market.utils.Navigation;
//
//
//public class ReviewListFragment extends BaseFragment {
//    private RecyclerView rvReviews;
//
//
//    @Override
//    protected void processArguments() {
//    }
//
//    @Override
//    protected int setLayout() {
//        return R.layout.fragment_review_list;
//    }
//
//    @Override
//    protected void findViews(View rootView) {
//        tvReviewCount = (TextView) rootView.findViewById(R.id.review_count);
//        tvQuality = (TextView) rootView.findViewById(R.id.quality);
//        btnMore = (TextView) rootView.findViewById(R.id.more);
//        rvReviews = (RecyclerView) rootView.findViewById(R.id.list);
//        tvAverageRate = (TextView) rootView.findViewById(R.id.total_rate);
//        tvAddReview = (TextView) rootView.findViewById(R.id.add_review);
//        tvAddReviewFirst = (TextView) rootView.findViewById(R.id.add_review_first);
//    }
//
//    @Override
//    protected void initViews() {
//        rvReviews.setHasFixedSize(true);
//        rvReviews.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL));
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rvReviews.setLayoutManager(layoutManager);
//        rvReviews.setNestedScrollingEnabled(false);
//    }
//
//    @Override
//    protected void showData() {
//    }
//
//    public void setReviews(List<Review> reviews, float averageRate, int reviewsCount, IOnSendSuccess listener) {
//        this.listener = listener;
//        tvReviewCount.setText(getResources().getQuantityString(R.plurals.reviews,reviewsCount,reviewsCount));
//        if (reviews != null && reviews.size() > 0) {
//            tvQuality.setText(CommonUtils.getRateString(getActivity(), averageRate));
//            tvAverageRate.setText(CommonUtils.formatRate(getActivity(), averageRate));
//
////            tvReviewCount.setText(getString(R.string.review_count, reviewsCount));
//
//
//
//            ReviewAdapter adapter = new ReviewAdapter(getActivity(), reviews);
//            adapter.setSnackBarView(vSnackBarView);
//            rvReviews.setAdapter(adapter);
//
//            if (reviewsCount > MAX_REVIEW_COUNT) {
//                btnMore.setVisibility(View.VISIBLE);
//                btnMore.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        if (CommonUtils.isNetworkConnected(getActivity())) {
//                            startActivity(new Intent(getActivity(), ListReviewRestaurantActivity.class));
//                        } else {
//                            showNoConnection();
//                        }
//                    }
//                });
//            } else {
//                btnMore.setVisibility(View.GONE);
//            }
//
//            tvAddReview.setVisibility(View.VISIBLE);
//            tvAddReviewFirst.setVisibility(View.GONE);
//            rvReviews.setVisibility(View.VISIBLE);
//            tvQuality.setVisibility(View.VISIBLE);
//            tvAverageRate.setVisibility(View.VISIBLE);
//        } else {
//            tvAddReview.setVisibility(View.GONE);
//            tvAddReviewFirst.setVisibility(View.VISIBLE);
//            rvReviews.setVisibility(View.GONE);
//            btnMore.setVisibility(View.GONE);
//            tvQuality.setVisibility(View.GONE);
////            tvReviewCount.setText(getString(R.string.review_count, 0));
//            tvAverageRate.setVisibility(View.GONE);
//        }
//        View.OnClickListener clickListener = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (CommonUtils.isNetworkConnected(getActivity())) {
//                    if (Authentication.isLoggedIn(getActivity())) {
//                        addRestaurantReview();
//                    } else {
//                        if (CommonUtils.isNetworkConnected(getActivity())) {
//                            Navigation.showDialogLogin(ReviewListFragment.this, REVIEW_RESTAURANT_LOGIN_CODE);
//                        } else {
//                            showNoConnection();
//                        }
//                    }
//                } else {
//                    showNoConnection();
//                }
//            }
//        };
//        tvAddReview.setOnClickListener(clickListener);
//        tvAddReviewFirst.setOnClickListener(clickListener);
//
//    }
//
//    private void addRestaurantReview() {
////        Intent intent = new Intent(getActivity(), ReviewActivity.class);
////        startActivityForResult(intent, REVIEW_COMPOSE_REQUEST_CODE);
//    }
//
//    public static final int REVIEW_COMPOSE_REQUEST_CODE = 1;
//    public static final int REVIEW_FOOD_LOGIN_CODE = 2;
//    public static final int REVIEW_RESTAURANT_LOGIN_CODE = 3;
//
//    IOnSendSuccess listener;
//
//    public interface IOnSendSuccess {
//        void onSend();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REVIEW_COMPOSE_REQUEST_CODE:
//                    if (listener != null) {
//                        listener.onSend();
//                    }
//                    break;
//                case REVIEW_RESTAURANT_LOGIN_CODE:
//                    addRestaurantReview();
//                    break;
//            }
//        }
//    }
//
//
//
//}
