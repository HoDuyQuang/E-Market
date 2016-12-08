package vn.edu.dut.itf.e_market.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.activities.LoginActivity;
import vn.edu.dut.itf.e_market.fragments.ReviewDetailFragment;
import vn.edu.dut.itf.e_market.fragments.BaseFragment;


public class Navigation {
    private static AlertDialog changePassDialog;

    public static void showFoodDetail(Context context, int foodId) {
//        Intent intent = new Intent(context, FoodDetailActivity.class);
//        intent.putExtra(FoodDetailActivity.FOOD_ID, foodId);
//        context.startActivity(intent);
    }
//
//    public static void showFoodReviewDetail(Context context, String reviewId) {
//        Intent intent = new Intent(context, FoodReviewDetailActivity.class);
//        intent.putExtra(FoodReviewDetailActivity.REVIEW_ID, reviewId);
//        context.startActivity(intent);
//    }
//
    public static void showRestaurantReviewDetail(Context context, String reviewId) {
        Intent intent = new Intent(context, ReviewDetailFragment.class);
        intent.putExtra(ReviewDetailFragment.REVIEW_ID, reviewId);
        context.startActivity(intent);
    }

    public static void showDialogLogin(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setPositiveButton(context.getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.setMessage(R.string.login_to_perform);
        builder.create().show();
    }

    public static void showDialogLogin(final Activity context, final int REQUEST_CODE) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setPositiveButton(context.getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivityForResult(new Intent(context, LoginActivity.class),REQUEST_CODE);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.setMessage(R.string.login_to_perform);
        builder.create().show();
    }

    public static void showDialogLogin(final BaseFragment fragment, final int REQUEST_CODE) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setCancelable(true);
        builder.setPositiveButton(fragment.getActivity().getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fragment.startActivityForResult(new Intent(fragment.getActivity(), LoginActivity.class), REQUEST_CODE);
            }
        });
        builder.setNegativeButton(fragment.getActivity().getString(R.string.cancel), null);
        builder.setMessage(R.string.login_to_perform);
        builder.create().show();
    }

//    public static void showDialogPasswordChanged(final Context context) {
//        if (changePassDialog == null || !changePassDialog.isShowing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setPositiveButton(context.getString(R.string.login), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                    dialogInterface.dismiss();
//                }
//            });
//            builder.setMessage(R.string.error_password_changed);
//            changePassDialog = builder.create();
//            changePassDialog.show();
//        }
//    }

//    public static void showAddCartDialog(FragmentActivity activity, Food food){
//        Bundle bundle=new Bundle();
//        bundle.putParcelable(AddCartDialog.FOOD,food);
//        AddCartDialog fragment=new AddCartDialog();
//        fragment.setArguments(bundle);
//        fragment.show(activity.getSupportFragmentManager(),null);
//    }
//
//    public static void showOrderNowDialog(FragmentActivity activity, Food food){
//        Bundle bundle=new Bundle();
//        bundle.putParcelable(OrderNowDialog.FOOD,food);
//        OrderNowDialog fragment=new OrderNowDialog();
//        fragment.setArguments(bundle);
//        fragment.show(activity.getSupportFragmentManager(),null);
//    }
}
