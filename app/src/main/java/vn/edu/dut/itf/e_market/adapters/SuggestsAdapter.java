package vn.edu.dut.itf.e_market.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.Food;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.Navigation;

/**
 * @author d_quang
 */
public class SuggestsAdapter extends AppBaseAdapter<Food, SuggestsAdapter.SuggestViewHolder> {
    FragmentActivity mActivity;

    public SuggestsAdapter(FragmentActivity context, List<Food> arrayList) {
        super(context, arrayList);
        mActivity = context;
    }


    @Override
    void onBind(SuggestViewHolder mainHolder, final Food model, int position) {
        mainHolder.name.setText(model.getName());
        mainHolder.name.setMovementMethod(new ScrollingMovementMethod());
        mainHolder.price.setText(CommonUtils.formatPrice(mContext, model.getPrice()));
        if (model.getRateString() > 0) {
            mainHolder.rate.setText(CommonUtils.formatRate(mContext, model.getRateString()));
            mainHolder.rate.setVisibility(View.VISIBLE);
        } else {
            mainHolder.rate.setVisibility(View.GONE);
        }
        if (model.getDiscount() > 0) {
            CommonUtils.setTextViewStrike(mainHolder.price, true);
            mainHolder.discountPercent.setText(CommonUtils.formatDiscount(mContext, model.getDiscount()));
            mainHolder.discountPrice.setText(CommonUtils.formatPrice(mContext,
                    model.getSale()));
            mainHolder.discountPercent.setVisibility(View.VISIBLE);
            mainHolder.discountPrice.setVisibility(View.VISIBLE);
        } else {
            CommonUtils.setTextViewStrike(mainHolder.price, false);
            mainHolder.discountPercent.setVisibility(View.INVISIBLE);
            mainHolder.discountPrice.setVisibility(View.INVISIBLE);
        }
        mainHolder.order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Navigation.showOrderNowDialog(mActivity, model);
//                new OrderNowDialog(mContext, model,mSnackBarView).show();
            }
        });

        Picasso.with(mContext).load(model.getImageUrl()).placeholder(R.drawable.ic_no_photo_available)
                .into(mainHolder.image);
        if (model.getType() == Food.TYPE_NEW) {
            mainHolder.vNew.setVisibility(View.VISIBLE);
        } else {
            mainHolder.vNew.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public SuggestViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.adapter_suggest_item, viewGroup, false);
        return new SuggestViewHolder(mainGroup);
    }

    class SuggestViewHolder extends RecyclerView.ViewHolder {

        public final TextView price;
        public final ImageView image;
        public final TextView name;
        public final TextView rate;
        final TextView discountPercent;
        final TextView discountPrice;
        public final TextView order;
        final View vNew;

        SuggestViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name);
            this.price = (TextView) view.findViewById(R.id.price);
            this.image = (ImageView) view.findViewById(R.id.image);
            this.rate = (TextView) view.findViewById(R.id.rate);
            this.discountPercent = (TextView) view.findViewById(R.id.discount_percent);
            this.discountPrice = (TextView) view.findViewById(R.id.discount_price);
            this.order = (TextView) view.findViewById(R.id.order);
            this.vNew = view.findViewById(R.id.new_mark);
        }
    }
}
