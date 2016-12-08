package vn.edu.dut.itf.e_market.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.activities.BaseActivity;
import vn.edu.dut.itf.e_market.adapters.ReviewCommentAdapter;
import vn.edu.dut.itf.e_market.models.Review;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.Navigation;


public abstract class ReviewDetailFragment extends BaseActivity implements View.OnClickListener {

    public static final String REVIEW_ID = "reviewId";
    private EditText etMessage;
    private View viewPost;
    private View viewReview;
    private ImageView ivAva;
    protected TextView tvName, tvPoint, tvDateTime, tvContent, tvLike, tvComment;
    protected ArrayList<Review> mComments;
    protected ReviewCommentAdapter mAdapter;

    protected String mReviewId = "";
    protected RecyclerView rvReviews;
    private TextView tvTitle;
    protected CheckBox like;

    protected SwipeRefreshLayout vRefresh;
    protected View vError;
    protected View vLoading;

    @Override
    public int setLayout() {
        return R.layout.activity_review_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void setCheckChanged(Review review);

    protected void cancelLike(boolean b, Review model) {
        like.setOnCheckedChangeListener(null);
        like.setChecked(!b);
        setCheckChanged(model);
    }
    protected void setLiked(boolean b, Review model) {
        if (b){
            model.setLike( model.getLike()+1);
            tvLike.setText(getString(R.string.like,model.getLike()));
        } else{
            model.setLike( model.getLike()-1);
            tvLike.setText(getString(R.string.like, model.getLike()));
        }
    }

    abstract void requestComment(String comment);

    abstract void requestApi();

    protected void setData(Review review) {
        tvName.setText(review.getName());
        if (review.getTitle() != null && review.getTitle().trim().length() > 0) {
            tvTitle.setText(review.getTitle());
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        tvTitle.setText(review.getTitle());
        tvContent.setText(review.getContent());
        tvLike.setText(getString(R.string.like, review.getLike()));
        tvComment.setText(getString(R.string.comment, review.getComment()));

        tvDateTime.setText(CommonUtils.formatDateTime(this, review.getDate()));
        mComments.clear();
        mComments.addAll(review.listComments);

        like.setChecked(review.isLikeStatus() == 1);
        etMessage.setText("");
        setCheckChanged(review);
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void findViews() {
        rvReviews = (RecyclerView) findViewById(R.id.lvReviewComment);
        etMessage = (EditText) findViewById(R.id.etMessage);
        viewReview = findViewById(R.id.viewReview);
        tvName = (TextView) viewReview.findViewById(R.id.user);
        tvPoint = (TextView) viewReview.findViewById(R.id.rate);
        tvTitle = (TextView) viewReview.findViewById(R.id.title);
        tvContent = (TextView) viewReview.findViewById(R.id.review);
        tvDateTime = (TextView) viewReview.findViewById(R.id.time);
        tvLike = (TextView) viewReview.findViewById(R.id.like_num);
        tvComment = (TextView) viewReview.findViewById(R.id.comment_num);
        like = (CheckBox) findViewById(R.id.like);
        viewPost = findViewById(R.id.tvPost);

        vRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        vError = findViewById(R.id.tvErrorMessage);
        vLoading= findViewById(R.id.request_progress);
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        setTitle(getString(R.string.review_detail));

        rvReviews.setHasFixedSize(true);
        rvReviews.setNestedScrollingEnabled(false);
        rvReviews.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        rvReviews.setLayoutManager(layoutManager);
        viewPost.setOnClickListener(this);

        etMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    if (!Authentication.isLoggedIn(ReviewDetailFragment.this)){
                        etMessage.clearFocus();
                        Navigation.showDialogLogin(ReviewDetailFragment.this);
                    }
                }
            }
        });

        vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });
        vError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResume();
            }
        });
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mReviewId = bundle.getString(REVIEW_ID);
        }
        mComments = new ArrayList<>();
    }

    @Override
    public void showData() {

        showComment();
    }

    abstract void showComment();

    @Override
    protected void onResume() {
        super.onResume();
        requestApi();
        findViewById(R.id.root_view).requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPost:
                if (CommonUtils.isNetworkConnected(this)) {
                    if (Authentication.isLoggedIn(this)) {
                        if (!etMessage.getText().toString().trim().isEmpty()) {
                            requestComment(etMessage.getText().toString());
                        } else {
                            toast("Please input your comment.");
                        }
                    } else {
                        hideSoftKeyboard();
                        Navigation.showDialogLogin(this);
                    }
                } else{
                    showNoConnection(findViewById(R.id.view_data));
                }
                break;
        }
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
}
