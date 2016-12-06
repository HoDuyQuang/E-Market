package vn.edu.dut.itf.e_market.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import vn.edu.dut.itf.e_market.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FoodDetailActivity extends AppCompatActivity {
    public static final String FOOD_ID = "foodId";
    private ImageView ivFoodImage;
    private TextView tvName, tvOldCost, tvNewCost, tvOrderNow, tvNumPhoto, tvNumReview, tvNumBookmark;
    private View rlCall, rlAddToCart, rlBookmark;
    private SuggestsFragment mFragmentSuggest;
    private ReviewListFragment mFragmentReviewList;

    private int mFoodId;
    private GetFoodDetailTask mGetFoodDetailTask;
    private TextView discountPercent;
    private FoodDetail foodDetail;
    private SwipeRefreshLayout vRefresh;
    private View vError;
    private View vLoading;
    private CheckBox vLike;
    private TextView tvDescription;
    private TextView tvDescriptionTitle;

    @Override
    public int setLayout() {
        return R.layout.activity_food_detail;
    }

    private void requestData() {
        mGetFoodDetailTask = new GetFoodDetailTask(this, mFoodId) {
            @Override
            protected void onSuccess(final FoodDetail info) {
                super.onSuccess(info);
                foodDetail = info;
                setData(info);
                rlAddToCart.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new AddCartDialog(FoodDetailActivity.this, info.food).show();
                    }
                });
                tvOrderNow.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new OrderNowDialog(FoodDetailActivity.this, info.food, findViewById(R.id.view_data)).show();
                    }
                });
            }

            @Override
            protected void onError(int code) {
                super.onError(code);
            }
        };
        mGetFoodDetailTask.setSwipeRefreshView(vRefresh);
        mGetFoodDetailTask.setErrorView(vError);
        mGetFoodDetailTask.setSnackbarView(findViewById(R.id.view_data));
        mGetFoodDetailTask.setLoadingView(vLoading);
        mGetFoodDetailTask.setStatus(requestStatus);
        mGetFoodDetailTask.execute();
    }

    private void setData(FoodDetail foodDetail) {
        Picasso.with(FoodDetailActivity.this).load(foodDetail.food.imageUrl).placeholder(R.drawable.ic_no_photo_available).into(ivFoodImage);
        // Fix font for Myanmar language Android version < Kitkat
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            if (CommonUtils.isBurmese(foodDetail.food.name))
//                tvName.setTypeface(
//                    Typeface.createFromAsset(
//                            getAssets(), "fonts/" + getString(R.string.myanmar_font)));
//        }
        tvName.setText(foodDetail.food.name);
        tvOldCost.setText(CommonUtils.formatPrice(this, foodDetail.food.price));
        if (foodDetail.getFood().getDescription() != null && foodDetail.getFood().getDescription().length() > 0) {
            tvDescription.setText(CommonUtils.fromHtml(foodDetail.getFood().getDescription()));
            tvDescription.setVisibility(View.VISIBLE);
            tvDescriptionTitle.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
            tvDescriptionTitle.setVisibility(View.GONE);
        }
        if (foodDetail.getFood().getDiscount() > 0) {
            CommonUtils.setTextViewStrike(tvOldCost, true);
            discountPercent.setText(CommonUtils.formatDiscount(this,foodDetail.getFood().getDiscount()));
            tvNewCost.setText(CommonUtils.formatPrice(this,
                    foodDetail.getFood().getSale()));
            discountPercent.setVisibility(View.VISIBLE);
            tvNewCost.setVisibility(View.VISIBLE);
        } else {
            CommonUtils.setTextViewStrike(tvOldCost, false);
            discountPercent.setVisibility(View.INVISIBLE);
            tvNewCost.setVisibility(View.INVISIBLE);
        }
        tvNewCost.setText(CommonUtils.formatPrice(this, foodDetail.food.sale));
        tvNumPhoto.setText(String.valueOf(foodDetail.imageCount));
        tvNumReview.setText(String.valueOf(foodDetail.reviewCount));
        tvNumBookmark.setText(String.valueOf(foodDetail.bookmarkCount));
        setTitle(foodDetail.food.name);

//        mFragmentSuggest.setData(foodDetail.listSuggest, true);

        mFragmentReviewList.setTSnackBarView(findViewById(R.id.view_data));
        mFragmentReviewList.setReviews(foodDetail.getFood(), foodDetail.listReview, foodDetail.getFood().getRate().getAverage(),
                foodDetail.getFood().getRate(), foodDetail.getReviewCount(), new ReviewListFragment.IOnSendSuccess() {
                    @Override
                    public void onSend() {
                        requestData();
                    }
                });
        if (foodDetail.getFood().getLikeStatus() == 1) {
            vLike.setChecked(true);
        } else {
            vLike.setChecked(false);
        }
    }

    /**
     * on view click
     */
    private OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rlCall:
                    CommonUtils.callRestaurant(FoodDetailActivity.this);
                    break;
                case R.id.rlBookmark:
                    if (Authentication.isLoggedIn(FoodDetailActivity.this)) {
                        if (CommonUtils.isNetworkConnected(FoodDetailActivity.this)) {
                            final int like = foodDetail.getFood().getLikeStatus() == 1 ? 0 : 1;
                            new SetFavoriteFoodTask(FoodDetailActivity.this, mFoodId, like) {
                                @Override
                                protected void onSuccess() {
                                    super.onSuccess();
                                    foodDetail.getFood().setLikeStatus(like);
                                    if (like == 1) {
                                        foodDetail.bookmarkCount += 1;
                                        vLike.setChecked(true);
                                    } else {
                                        foodDetail.bookmarkCount -= 1;
                                        vLike.setChecked(false);
                                    }
                                    tvNumBookmark.setText(String.valueOf(foodDetail.bookmarkCount));
                                }

                            }.execute();
                        } else {
                            showNoConnection(findViewById(R.id.view_data));
                        }
                    } else {
                        Navigation.showDialogLogin(FoodDetailActivity.this);
                    }
                    break;

                case R.id.ivFoodImage:
                    if (CommonUtils.isNetworkConnected(FoodDetailActivity.this)) {
                        Intent intent = new Intent(FoodDetailActivity.this, PhotoAllActivity.class);
                        intent.putExtra(PhotoAllActivity.ARG_FOOD_ID, mFoodId);
                        intent.putExtra(PhotoAllActivity.ARG_TITLE, foodDetail.getFood().getName());
                        startActivity(intent);
                    } else {
                        showNoConnection(findViewById(R.id.view_data));
                    }
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public void findViews() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (ExpandableTextView) findViewById(R.id.description);
        tvDescriptionTitle = (TextView) findViewById(R.id.tvDescriptionTitle);
        tvOldCost = (TextView) findViewById(R.id.tvOldCost);
        tvNewCost = (TextView) findViewById(R.id.tvNewCost);
        tvOrderNow = (TextView) findViewById(R.id.tvOrderNow);
        tvNumPhoto = (TextView) findViewById(R.id.tvNumPhoto);
        tvNumReview = (TextView) findViewById(R.id.tvNumReview);
        tvNumBookmark = (TextView) findViewById(R.id.tvNumBookmark);
        ivFoodImage = (ImageView) findViewById(R.id.ivFoodImage);
        tvOrderNow = (TextView) findViewById(R.id.tvOrderNow);
        rlCall = findViewById(R.id.rlCall);
        rlAddToCart = findViewById(R.id.rlAddToCart);
        rlBookmark = findViewById(R.id.rlBookmark);
        mFragmentReviewList = (ReviewListFragment) getSupportFragmentManager().findFragmentById(R.id.review_list);
        discountPercent = (TextView) findViewById(R.id.discount_percent);
        vLike = (CheckBox) findViewById(R.id.like);

        vRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        vError = findViewById(R.id.tvErrorMessage);
        vLoading = findViewById(R.id.request_progress);


    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rlCall.setOnClickListener(onClick);

        rlBookmark.setOnClickListener(onClick);
        ivFoodImage.setOnClickListener(onClick);


        vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
                mFragmentSuggest.onRefresh();
            }
        });
        vError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData();
                mFragmentSuggest.onRefresh();
            }
        });

        initSuggest();
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFoodId = bundle.getInt(FOOD_ID, -1);
        }
    }

    @Override
    public void showData() {
        super.showData();
    }

    @Override
    void navigateToCart() {
        startActivity(new Intent(this, CartActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        requestData();

    }

    private void initSuggest() {
        if (mFragmentSuggest==null) {
            mFragmentSuggest = new SuggestsFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(SuggestsFragment.ARG_IS_COLLAPSIBLE, true);
            bundle.putBoolean(SuggestsFragment.ARG_IS_TAKE_AWAY, false);
            mFragmentSuggest.setArguments(bundle);
            mFragmentSuggest.setTSnackBarView(findViewById(R.id.view_data));
            getSupportFragmentManager().beginTransaction().replace(R.id.suggest, mFragmentSuggest).commit();
        }
    }
}
