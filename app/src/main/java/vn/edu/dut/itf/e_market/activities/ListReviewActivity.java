package vn.edu.dut.itf.e_market.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import vn.edu.dut.itf.e_market.R;


public abstract class ListReviewActivity extends BaseActivity {
	public static final int REVIEW_COMPOSE_REQUEST_CODE = 1;
	public static final int REVIEW_LOGIN_CODE = 2;

	protected RecyclerView rvReviews;
	protected TextView averageReviewPoint;
	protected TextView numberReview;
	protected Button addReview;

	protected SwipeRefreshLayout vRefresh;
	protected View vError;
	protected View vLoading;
	protected View tvEmptyCart;

	protected boolean isLoadMore=false;
	protected TextView tvRateString;

	@Override
	public int setLayout() {
		return R.layout.fragment_list_review_res;
	}

	@Override
	public void findViews() {
		rvReviews = (RecyclerView) findViewById(R.id.rec_frg_list_review_res);
		averageReviewPoint = (TextView) findViewById(R.id.tev_frg_list_review_res_average);
		numberReview = (TextView) findViewById(R.id.tev_frg_list_review_res_number_review);
		tvRateString = (TextView) findViewById(R.id.tev_frg_list_review_res_average_text);

		addReview = (Button) findViewById(R.id.btn_frg_list_review_res_add_review);
		addReview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonUtils.isNetworkConnected(ListReviewActivity.this)) {
					if (Authentication.isLoggedIn(ListReviewActivity.this)) {
						onAddReview();
					} else {
						Navigation.showDialogLogin(ListReviewActivity.this,REVIEW_LOGIN_CODE);
					}
				} else{
					showNoConnection(findViewById(R.id.view_data));
				}
			}
		});

		vRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
		vError = findViewById(R.id.tvErrorMessage);
		vLoading= findViewById(R.id.request_progress);
		tvEmptyCart = findViewById(R.id.tvCartEmpty);
	}

	abstract void onAddReview();

	@Override
	public void initViews() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar()!=null) {
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		setTitle(R.string.review);

		vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				isLoadMore=false;
				request(0,10);
			}
		});
		vError.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isLoadMore=false;
				request(0,10);
			}
		});

		rvReviews.setHasFixedSize(true);
		rvReviews.addItemDecoration(new DividerItemDecoration(this,
				ContextCompat.getDrawable(this, R.drawable.adapter_promotional_divider), true, true));
		final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		rvReviews.setLayoutManager(layoutManager);
		rvReviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);

			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (dy>0&&layoutManager.findLastVisibleItemPosition()>=getCount()-3){
					request(getCount(),10);
				}
			}
		});
	}

	@Override
	public void showData() {
		isLoadMore=false;
		RecycleFill();
		request(0,10);
	}

	abstract int getCount();

	protected abstract void RecycleFill();

	abstract void request(int start, int count);


	@Override
	public void initData() {
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

	protected void SetRatePopUp(BaseRate info) {
		final QuickAction quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
		ArrayList<Rating> ratings = getRatings(info);

		quickAction.setData(ratings, info.getBadCount(), info.getAverageCount(), info.getGoodCount(),
				info.getExcellentCount());
		averageReviewPoint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				quickAction.show(averageReviewPoint);
			}
		});
	}

	@NonNull
	abstract ArrayList<Rating> getRatings(BaseRate info);

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case REVIEW_COMPOSE_REQUEST_CODE:
					showData();
					break;
				case REVIEW_LOGIN_CODE:
					onAddReview();
					break;
			}
		}
	}
}
