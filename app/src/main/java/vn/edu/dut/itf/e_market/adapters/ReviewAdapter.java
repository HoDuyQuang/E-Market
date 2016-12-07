package vn.edu.dut.itf.e_market.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.Review;
import vn.edu.dut.itf.e_market.tasks.SetLikeRestaurantReviewTask;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.Navigation;

/**
 * @author d_quang
 */
public class ReviewAdapter extends AppBaseAdapter<Review, ReviewAdapter.ReviewViewHolder> {

    public ReviewAdapter(Context context, List<Review> arrayList) {
        super(context, arrayList);
    }

    @Override
    void onBind(final ReviewViewHolder mainHolder, final Review model, int position) {
        if (model.getTitle() != null && model.getTitle().trim().length() > 0) {
            mainHolder.title.setText(model.getTitle());
            mainHolder.title.setVisibility(View.VISIBLE);
        } else {
            mainHolder.title.setVisibility(View.GONE);
        }
        setName(mainHolder, model);
        mainHolder.content.setText(model.getContent());

        mainHolder.likeNum.setText(mContext.getString(R.string.like, model.getLike()));
        mainHolder.commentNum.setText(mContext.getString(R.string.comment, model.getComment()));
        mainHolder.time.setText(CommonUtils.formatDateTime(mContext, model.getDate()));
        mainHolder.vLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isNetworkConnected(mContext)) {
                    mainHolder.like.performClick();
                } else {
                    showNoConnection();
                }
            }
        });

        setOnItemClickListener(new ReviewAdapter.IItemClickListener<Review>() {

            @Override
            public void onItemClick(Review item, int position) {
                if (CommonUtils.isNetworkConnected(mContext)) {
                    Navigation.showRestaurantReviewDetail(mContext, item.reviewId);
                } else {
                    showNoConnection();
                }
            }
        });

        mainHolder.like.setOnCheckedChangeListener(null);
        mainHolder.like.setChecked(model.isLikeStatus() == 1);
        setCheckChanged(mainHolder, model);

        setAnimation(mainHolder.itemView,position);
    }

    protected void setName(ReviewViewHolder mainHolder, Review model) {
        mainHolder.name.setText(model.getName());
        mainHolder.name.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setCheckChanged(final ReviewViewHolder mainHolder, final Review model) {
        mainHolder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (CommonUtils.isNetworkConnected(mContext)) {
                    if (Authentication.isLoggedIn(mContext)) {
                        onLike(mainHolder, b, model);
                    } else {
                        cancelLike(b, mainHolder, model);
                        Navigation.showDialogLogin(mContext);
                    }
                } else {
                    showNoConnection();
                    cancelLike(b, mainHolder, model);
                }
            }
        });
    }

    private void cancelLike(boolean b, ReviewViewHolder mainHolder, Review model) {
        mainHolder.like.setOnCheckedChangeListener(null);
        mainHolder.like.setChecked(!b);
        setCheckChanged(mainHolder, model);
    }

    private void onLike(final ReviewViewHolder mainHolder, final boolean b, final Review model) {
        int like;
        if (b) {
            like = 1;
        } else {
            like = 0;
        }
        SetLikeRestaurantReviewTask taskLike = new SetLikeRestaurantReviewTask(mContext, model.getReviewId(), like) {
            @Override
            protected void onSuccess() {
                super.onSuccess();
                if (b) {
                    model.setLike(model.getLike() + 1);
                    mainHolder.likeNum.setText(mContext.getString(R.string.like, model.getLike()));
                } else {
                    model.setLike(model.getLike() - 1);
                    mainHolder.likeNum.setText(mContext.getString(R.string.like, model.getLike()));
                }
            }

            @Override
            protected void onError(int code) {
                super.onError(code);
                cancelLike(b, mainHolder, model);

            }
        };
        taskLike.setSnackbarView(mSnackBarView);
        taskLike.execute();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.adapter_review_item, viewGroup, false);
        return new ReviewViewHolder(mainGroup);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private TextView rate;
        private TextView content;
        private TextView likeNum;
        private TextView commentNum;
        private TextView time;
        private TextView title;
        private CheckBox like;
        private View vLike;

        ReviewViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.name = (TextView) view.findViewById(R.id.user);
            this.content = (TextView) view.findViewById(R.id.review);
            this.rate = (TextView) view.findViewById(R.id.rate);
            this.likeNum = (TextView) view.findViewById(R.id.like_num);
            this.commentNum = (TextView) view.findViewById(R.id.comment_num);
            this.image = (ImageView) view.findViewById(R.id.avatar);
            this.time = (TextView) view.findViewById(R.id.time);
            this.like = (CheckBox) view.findViewById(R.id.like);
            this.vLike = view.findViewById(R.id.view_like);
        }

        public void clearAnimation()
        {
            itemView.clearAnimation();
        }
    }

    private int lastPosition = -1;
    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }


    }
    @Override
    public void onViewDetachedFromWindow(final ReviewViewHolder holder)
    {
        holder.clearAnimation();
    }
}
