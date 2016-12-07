package vn.edu.dut.itf.e_market.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.Review;
import vn.edu.dut.itf.e_market.tasks.SetLikeRestaurantReviewCommentTask;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.Navigation;


/**
 * @author d_quang
 */
public class ReviewCommentAdapter extends AppBaseAdapter<Review, ReviewCommentAdapter.CommentViewHolder> {
    private final Type mType;

    public enum Type {
        FOOD, RESTAURANT
    }

    public ReviewCommentAdapter(Context context, List<Review> arrayList, Type type) {
        super(context, arrayList);
        this.mType = type;
    }

    @Override
    void onBind(final CommentViewHolder itemHolder, final Review model, int position) {
        if (!TextUtils.isEmpty(model.content)) {
            itemHolder.tvContent.setText(model.content);
        }
        itemHolder.tvName.setText(model.name);
        itemHolder.likeNum.setText(mContext.getString(R.string.like, model.getLike()));
//        itemHolder.commentNum.setText(mContext.getString(R.string.comment, model.getComment()));
        itemHolder.tvDateTime.setText(CommonUtils.formatDateTime(mContext, model.getDate()));

        itemHolder.vLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemHolder.like.performClick();
            }
        });
        itemHolder.like.setOnCheckedChangeListener(null);
        itemHolder.like.setChecked(model.isLikeStatus() == 1);
//		itemHolder.like.setEnabled(Authentication.isLoggedIn(mContext));

        setCheckChanged(itemHolder, model);
    }

    private void setCheckChanged(final CommentViewHolder mainHolder, final Review model) {
        mainHolder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Authentication.isLoggedIn(mContext)) {
                    onLike(mainHolder, b, model);
                } else {
                    cancelLike(b, mainHolder, model);
                    Navigation.showDialogLogin(mContext);
                }
            }
        });
    }

    private void cancelLike(boolean b, CommentViewHolder mainHolder, Review model) {
        mainHolder.like.setOnCheckedChangeListener(null);
        mainHolder.like.setChecked(!b);
        setCheckChanged(mainHolder, model);
    }

    private void onLike(final CommentViewHolder mainHolder, final boolean b, final Review model) {
        int like;
        if (b) {
            like = 1;
        } else {
            like = 0;
        }
        SetLikeRestaurantReviewCommentTask task = new SetLikeRestaurantReviewCommentTask(mContext, model.getReviewId(), like) {
            @Override
            protected void onSuccess() {
                super.onSuccess();
                onRequestSuccess(b, model, mainHolder);
            }

            @Override
            protected void onError(int code) {
                super.onError(code);
                cancelLike(b, mainHolder, model);
            }
        };
        task.setSnackbarView(mSnackBarView);
        task.execute();
    }

    private void onRequestSuccess(boolean b, Review model, CommentViewHolder mainHolder) {
        if (b) {
            model.setLike(model.getLike() + 1);
        } else {
            model.setLike(model.getLike() - 1);
        }
        mainHolder.likeNum.setText(mContext.getString(R.string.like, model.getLike()));
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_comment_review, viewGroup, false);
        return new CommentViewHolder(mainGroup);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView likeNum;
        ImageView ivAva;
        TextView tvName;
        TextView tvDateTime;
        TextView tvContent;
        private CheckBox like;
        private View vLike;

        CommentViewHolder(View view) {
            super(view);
            this.likeNum = (TextView) view.findViewById(R.id.like_num);
            this.tvName = (TextView) view.findViewById(R.id.user);
            this.tvContent = (TextView) view.findViewById(R.id.review);
//            this.commentNum = (TextView) view.findViewById(R.id.comment_num);
            this.ivAva = (ImageView) view.findViewById(R.id.avatar);
            this.tvDateTime = (TextView) view.findViewById(R.id.time);
            this.like = (CheckBox) view.findViewById(R.id.like);
            this.vLike = view.findViewById(R.id.view_like);
        }
    }
}
