package vn.edu.dut.itf.e_market.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.com.brycen.restaurant.R;
import vn.com.brycen.restaurant.activities.FoodReviewActivity;
import vn.com.brycen.restaurant.activities.ListReviewFoodActivity;
import vn.com.brycen.restaurant.activities.RestaurantReviewActivity;
import vn.com.brycen.restaurant.adapters.FoodReviewsAdapter;
import vn.com.brycen.restaurant.adapters.RestaurantReviewAdapter;
import vn.com.brycen.restaurant.models.BaseRate;
import vn.com.brycen.restaurant.models.Food;
import vn.com.brycen.restaurant.models.FoodRate;
import vn.com.brycen.restaurant.models.FoodReview;
import vn.com.brycen.restaurant.models.Rating;
import vn.com.brycen.restaurant.models.RestaurantRate;
import vn.com.brycen.restaurant.models.RestaurantReview;
import vn.com.brycen.restaurant.utils.Authentication;
import vn.com.brycen.restaurant.utils.CommonUtils;
import vn.com.brycen.restaurant.utils.Navigation;
import vn.com.brycen.restaurant.views.DividerItemDecoration;
import vn.com.brycen.restaurant.views.popup.QuickAction;

public class ReviewListFragment extends BaseFragment {
    private static final int MAX_REVIEW_COUNT = 3;
    private TextView tvQuality;
    private TextView btnMore;
    private TextView tvReviewCount;
    private RecyclerView rvReviews;
    private TextView tvAverageRate;
    private TextView tvAddReview;
    private TextView tvAddReviewFirst;

    @Override
    protected void processArguments() {
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_review_list;
    }

    @Override
    protected void findViews(View rootView) {
        tvReviewCount = (TextView) rootView.findViewById(R.id.review_count);
        tvQuality = (TextView) rootView.findViewById(R.id.quality);
        btnMore = (TextView) rootView.findViewById(R.id.more);
        rvReviews = (RecyclerView) rootView.findViewById(R.id.list);
        tvAverageRate = (TextView) rootView.findViewById(R.id.total_rate);
        tvAddReview = (TextView) rootView.findViewById(R.id.add_review);
        tvAddReviewFirst = (TextView) rootView.findViewById(R.id.add_review_first);
    }

    @Override
    protected void initViews() {
        rvReviews.setHasFixedSize(true);
        rvReviews.addItemDecoration(new DividerItemDecoration(getActivity(),
                ContextCompat.getDrawable(getActivity(), R.drawable.adapter_promotional_divider), true, true));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.setNestedScrollingEnabled(false);
    }

    @Override
    protected void showData() {
    }

    public void setReviews(List<RestaurantReview> reviews, float averageRate, BaseRate rate, int reviewsCount, IOnSendSuccess listener) {
        this.listener = listener;
        tvReviewCount.setText(getResources().getQuantityString(R.plurals.reviews,reviewsCount,reviewsCount));
        if (reviews != null && reviews.size() > 0) {
            tvQuality.setText(CommonUtils.getRateString(getActivity(), averageRate));
            tvAverageRate.setText(CommonUtils.formatRate(getActivity(), averageRate));

//            tvReviewCount.setText(getString(R.string.review_count, reviewsCount));



            RestaurantReviewAdapter adapter = new RestaurantReviewAdapter(getActivity(), reviews);
            adapter.setSnackBarView(vSnackBarView);
            rvReviews.setAdapter(adapter);

            if (reviewsCount > MAX_REVIEW_COUNT) {
                btnMore.setVisibility(View.VISIBLE);
                btnMore.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (CommonUtils.isNetworkConnected(getActivity())) {
                            startActivity(new Intent(getActivity(), ListReviewRestaurantActivity.class));
                        } else {
                            showNoConnection();
                        }
                    }
                });
            } else {
                btnMore.setVisibility(View.GONE);
            }

            showDialogQuickRate(rate);
            tvAddReview.setVisibility(View.VISIBLE);
            tvAddReviewFirst.setVisibility(View.GONE);
            rvReviews.setVisibility(View.VISIBLE);
            tvQuality.setVisibility(View.VISIBLE);
            tvAverageRate.setVisibility(View.VISIBLE);
        } else {
            tvAddReview.setVisibility(View.GONE);
            tvAddReviewFirst.setVisibility(View.VISIBLE);
            rvReviews.setVisibility(View.GONE);
            btnMore.setVisibility(View.GONE);
            tvQuality.setVisibility(View.GONE);
//            tvReviewCount.setText(getString(R.string.review_count, 0));
            tvAverageRate.setVisibility(View.GONE);
        }
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtils.isNetworkConnected(getActivity())) {
                    if (Authentication.isLoggedIn(getActivity())) {
                        addRestaurantReview();
                    } else {
                        if (CommonUtils.isNetworkConnected(getActivity())) {
                            Navigation.showDialogLogin(ReviewListFragment.this, REVIEW_RESTAURANT_LOGIN_CODE);
                        } else {
                            showNoConnection();
                        }
                    }
                } else {
                    showNoConnection();
                }
            }
        };
        tvAddReview.setOnClickListener(clickListener);
        tvAddReviewFirst.setOnClickListener(clickListener);

    }

    private void addRestaurantReview() {
        Intent intent = new Intent(getActivity(), RestaurantReviewActivity.class);
        startActivityForResult(intent, REVIEW_COMPOSE_REQUEST_CODE);
    }

    public void setReviews(final Food food, List<FoodReview> reviews, float averageRate, BaseRate rate, int reviewsCount, IOnSendSuccess listener) {
        this.listener = listener;
        mFood = food;
        tvReviewCount.setText(getResources().getQuantityString(R.plurals.reviews,reviewsCount,reviewsCount));
        if (reviews.size() > 0) {
            tvQuality.setText(CommonUtils.getRateString(getActivity(), averageRate));
            tvAverageRate.setText(CommonUtils.formatRate(getActivity(), averageRate));
//            tvReviewCount.setText(getString(R.string.review_count, reviewsCount));
            FoodReviewsAdapter adapter = new FoodReviewsAdapter(getActivity(), reviews);
            adapter.setSnackBarView(vSnackBarView);
            rvReviews.setAdapter(adapter);
            if (reviewsCount > MAX_REVIEW_COUNT) {
                btnMore.setVisibility(View.VISIBLE);
                btnMore.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (CommonUtils.isNetworkConnected(getActivity())) {
                            Intent intent = new Intent(getActivity(), ListReviewFoodActivity.class);
                            intent.putExtra(ListReviewFoodActivity.ARG_FOOD, food);
                            startActivity(intent);
                        } else {
                            showNoConnection();
                        }
                    }
                });
            } else {
                btnMore.setVisibility(View.GONE);
            }

            showDialogQuickRate(rate);
            tvAddReview.setVisibility(View.VISIBLE);
            tvAddReviewFirst.setVisibility(View.GONE);
            rvReviews.setVisibility(View.VISIBLE);
            tvQuality.setVisibility(View.VISIBLE);
            tvAverageRate.setVisibility(View.VISIBLE);
        } else {
            tvAddReview.setVisibility(View.GONE);
            tvAddReviewFirst.setVisibility(View.VISIBLE);

            rvReviews.setVisibility(View.GONE);
            btnMore.setVisibility(View.GONE);
            tvQuality.setVisibility(View.GONE);
//            tvReviewCount.setText(getString(R.string.review_count, 0));
            tvAverageRate.setVisibility(View.GONE);
        }

        View.OnClickListener addReviewClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtils.isNetworkConnected(getActivity())) {
                    if (Authentication.isLoggedIn(getActivity())) {
                        addFoodReview(food);
                    } else {
                        Navigation.showDialogLogin(ReviewListFragment.this, REVIEW_FOOD_LOGIN_CODE);
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivityForResult(intent, REVIEW_FOOD_LOGIN_CODE);
                    }
                } else {
                    showNoConnection();
                }
            }
        };
        tvAddReviewFirst.setOnClickListener(addReviewClick);
        tvAddReview.setOnClickListener(addReviewClick);
    }

    private void addFoodReview(Food food) {
        Intent intent = new Intent(getActivity(), FoodReviewActivity.class);
        intent.putExtra(FoodReviewActivity.ARG_FOOD, food);
        startActivityForResult(intent, REVIEW_COMPOSE_REQUEST_CODE);
    }

    Food mFood;
    public static final int REVIEW_COMPOSE_REQUEST_CODE = 1;
    public static final int REVIEW_FOOD_LOGIN_CODE = 2;
    public static final int REVIEW_RESTAURANT_LOGIN_CODE = 3;

    IOnSendSuccess listener;

    public interface IOnSendSuccess {
        void onSend();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REVIEW_COMPOSE_REQUEST_CODE:
                    if (listener != null) {
                        listener.onSend();
                    }
                    break;
                case REVIEW_FOOD_LOGIN_CODE:
                    addFoodReview(mFood);

                    break;
                case REVIEW_RESTAURANT_LOGIN_CODE:
                    addRestaurantReview();
                    break;
            }
        }
    }

    private void showDialogQuickRate(BaseRate rate) {

        final QuickAction quickAction = new QuickAction(getActivity(), QuickAction.HORIZONTAL);

        ArrayList<Rating> ratings = getRate(rate);

        quickAction.setData(ratings, rate.getBadCount(), rate.getAverageCount(), rate.getGoodCount(),
                rate.getExcellentCount());
        tvAverageRate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                quickAction.show(tvAverageRate);
            }
        });
    }

    private ArrayList<Rating> getRate(BaseRate baseRate) {
        ArrayList<Rating> ratings = new ArrayList<>();
        if (baseRate instanceof RestaurantRate) {
            RestaurantRate rate = (RestaurantRate) baseRate;
            String[] titleArray = getResources().getStringArray(R.array.restaurant_rating_item);
            ratings.add(new Rating(titleArray[0], rate.getLocation()));
            ratings.add(new Rating(titleArray[1], rate.getPrice()));
            ratings.add(new Rating(titleArray[2], rate.getQuality()));
            ratings.add(new Rating(titleArray[3], rate.getServices()));
            ratings.add(new Rating(titleArray[4], rate.getDecoration()));
        } else if (baseRate instanceof FoodRate) {
            FoodRate rate = (FoodRate) baseRate;
            String[] titleArray = getResources().getStringArray(R.array.food_rating_item);
            ratings.add(new Rating(titleArray[0], rate.getPrice()));
            ratings.add(new Rating(titleArray[1], rate.getQuality()));
            ratings.add(new Rating(titleArray[2], rate.getDecoration()));
        }
        return ratings;
    }

}
