package vn.edu.dut.itf.e_market.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;

/**
 * @author d_quang
 */
public abstract class AppBaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected List<T> mDataList;
    protected IItemClickListener<T> mItemClickListener;
    protected final Context mContext;
    protected View mSnackBarView;


    public AppBaseAdapter(Context context, List<T> arrayList) {
        mDataList = arrayList;
        mContext = context;
    }

    public AppBaseAdapter(Context context, List<T> arrayList, IItemClickListener<T> listener) {
        this(context, arrayList);
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final V mainHolder, final int position) {
        final T model = mDataList.get(position);
        mainHolder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(model, mainHolder.getAdapterPosition());
                }
            }
        });
        onBind(mainHolder, model, position);
    }

    public void setData(List<T> list) {
        mDataList = list;
    }

    abstract void onBind(V viewHolder, T model, int position);

    public void setOnItemClickListener(IItemClickListener<T> listener) {
        this.mItemClickListener = listener;
    }

    public interface IItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    @Override
    public int getItemCount() {
        return (mDataList != null ? mDataList.size() : 0);
    }

    public void setSnackBarView(View mSnackBarView) {
        this.mSnackBarView = mSnackBarView;
    }

    protected void showNoConnection() {
        if (mSnackBarView != null) {
            // TODO: 12/7/2016 snack bar
//            TSnackbar.make(mSnackBarView, mContext.getString(R.string.no_internet_connection), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_network_unavailable).show();
        }
    }

    @SuppressWarnings("unused")
    protected void showServerError() {
        if (mSnackBarView != null) {
//            TSnackbar.make(mSnackBarView, mContext.getString(R.string.no_internet_connection), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_server_unavailable).show();
        }
    }
}
