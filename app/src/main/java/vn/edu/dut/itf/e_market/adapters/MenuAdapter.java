package vn.edu.dut.itf.e_market.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
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
public class MenuAdapter extends AppBaseAdapter<Food, MenuAdapter.MenuViewHolder> {

    private final FragmentActivity mActivity;

    public MenuAdapter(FragmentActivity context, List<Food> arrayList) {
        super(context, arrayList);
        this.mActivity = context;
    }

    @Override
    void onBind(MenuViewHolder mainHolder, final Food model, final int position) {
        mainHolder.category.setText(model.getCategory().getCategory());
        if (position == 0 || (mDataList.get(position - 1).getCategory().getId() != model.getCategory().getId())) {
            mainHolder.category.setVisibility(View.VISIBLE);
            mainHolder.separate.setVisibility(View.GONE);
        } else {
            mainHolder.separate.setVisibility(View.VISIBLE);
            mainHolder.category.setVisibility(View.GONE);
        }

        mainHolder.name.setText(model.getName());
        mainHolder.name.setSelected(true);
        if (model.getRateString() > 0) {
            mainHolder.rate.setText(CommonUtils.formatRate(mContext, model.getRateString()));
            mainHolder.rate.setVisibility(View.VISIBLE);
        } else {
            mainHolder.rate.setVisibility(View.GONE);
        }
        mainHolder.price.setText(CommonUtils.formatPrice(mContext, model.getPrice()));
        if (model.getDiscount() > 0) {
            CommonUtils.setTextViewStrike(mainHolder.price, true);
            mainHolder.salePrice.setVisibility(View.VISIBLE);
            mainHolder.salePrice.setText(CommonUtils.formatPrice(mContext, model.getSale()));
            mainHolder.discount.setText(CommonUtils.formatDiscount(mContext, model.getDiscount()));
            mainHolder.discount.setVisibility(View.VISIBLE);
        } else {
            mainHolder.salePrice.setVisibility(View.GONE);
            mainHolder.discount.setVisibility(View.GONE);
            CommonUtils.setTextViewStrike(mainHolder.price, false);
        }
        mainHolder.addToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtils.isNetworkConnected(mContext)) {
//                    Navigation.showAddCartDialog(mActivity,model);
//                    new AddCartDialog(mContext, model).show();
                } else {
                    showNoConnection();
                }
            }
        });
        mainHolder.order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtils.isNetworkConnected(mContext)) {
//                    Navigation.showOrderNowDialog(mActivity,model);
//                    new OrderNowDialog(mContext, model, mSnackBarView).show();
                } else {
                    showNoConnection();
                }
            }
        });
        mainHolder.itemView.findViewById(R.id.food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isNetworkConnected(mContext)) {
                    Navigation.showFoodDetail(mContext, model.getId());
                } else {
                    showNoConnection();
                }
            }
        });

        Picasso.with(mContext).load(model.getImageUrl()).placeholder(R.drawable.ic_no_photo_available).into(mainHolder.image);
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        View mainGroup = mInflater.inflate(R.layout.adapter_menu_item, viewGroup, false);
        return new MenuViewHolder(mainGroup);
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private final TextView discount;
        ImageView image;
        TextView name;
        TextView category;
        TextView rate;
        TextView price;
        TextView salePrice;
        View order;
        View addToCart;
        View separate;

        MenuViewHolder(View view) {
            super(view);
            this.category = (TextView) view.findViewById(R.id.category);
            this.rate = (TextView) view.findViewById(R.id.ivPoint);
            this.image = (ImageView) view.findViewById(R.id.ivThumbnailFavourite);
            this.name = (TextView) view.findViewById(R.id.tvFoodName);
            this.price = (TextView) view.findViewById(R.id.tvOldCost);
            this.salePrice = (TextView) view.findViewById(R.id.tvNewCost);
            this.order = view.findViewById(R.id.tvOrderNow);
            this.addToCart = view.findViewById(R.id.rlAddToCart);
            this.separate = view.findViewById(R.id.separate);

            this.discount = (TextView) view.findViewById(R.id.discount_percent);
        }
    }
}
