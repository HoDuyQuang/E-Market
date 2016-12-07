package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.adapters.ReviewAdapter;
import vn.edu.dut.itf.e_market.models.Review;
import vn.edu.dut.itf.e_market.tasks.GetListReviewRestaurantTask;


public class ListReviewRestaurantActivity extends ListReviewActivity {

    private List<Review> mListReviews;

    @Override
    void onAddReview() {
//        startActivityForResult(new Intent(ListReviewRestaurantActivity.this, RestaurantReviewActivity.class), REVIEW_COMPOSE_REQUEST_CODE);
    }


    @Override
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
            task = new GetListReviewRestaurantTask(this, start, count) {
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

//                protected void onSuccess(ArrayList<Review> info, RestaurantRate rate, int reviewCount) {
//                    if (isLoadMore) {
//                        int size = mListReviews.size();
//                        for (RestaurantReview review : info) {
//                            boolean isContain = false;
//                            for (RestaurantReview item : mListReviews) {
//                                if (review.getReviewId().equals(item.getReviewId())) {
//                                    isContain = true;
//                                    break;
//                                }
//                            }
//                            if (!isContain) {
//                                mListReviews.add(review);
//                            }
//                        }
//                        adapter.notifyItemRangeInserted(size, mListReviews.size() - size);
//                    } else {
//                        mListReviews.clear();
//                        mListReviews.addAll(info);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    if (!isLoadMore) {
//                        numberReview.setText(getString(R.string.number_reviews, reviewCount));
//                        averageReviewPoint.setText(CommonUtils.formatRate(ListReviewRestaurantActivity.this, rate.getAverage()));
//                        SetRatePopUp(rate);
//                        tvRateString.setText(CommonUtils.getRateString(ListReviewRestaurantActivity.this, rate.getAverage()));
//                    }
//
//                    isLoadMore = true;
//                }

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
            task.setSnackbarView(findViewById(R.id.view_data));
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
        adapter = new ReviewAdapter(this, mListReviews);
        adapter.setSnackBarView(findViewById(R.id.root_view));
        rvReviews.setAdapter(adapter);
    }

    @Override
    public void initData() {
        mListReviews = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @NonNull
//    @Override
//    ArrayList<Rating> getRatings(BaseRate rate) {
//        RestaurantRate info = (RestaurantRate) rate;
//        String[] titleArray = getResources().getStringArray(R.array.restaurant_rating_item);
//        ArrayList<Rating> ratings = new ArrayList<>();
//        ratings.add(new Rating(titleArray[0], info.getLocation()));
//        ratings.add(new Rating(titleArray[1], info.getPrice()));
//        ratings.add(new Rating(titleArray[2], info.getQuality()));
//        ratings.add(new Rating(titleArray[3], info.getServices()));
//        ratings.add(new Rating(titleArray[4], info.getDecoration()));
//        return ratings;
//    }
}
