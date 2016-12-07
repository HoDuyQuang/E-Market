//package vn.edu.dut.itf.e_market.fragments;
//
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.Calendar;
//
//import vn.edu.dut.itf.e_market.activities.SuggestsFragment;
//
//
///**
// * @author d_quang
// */
//public class HomeFragment extends BaseFragment {
//
//    private SwipeRefreshLayout mRefresh;
//    //    private SlideShowViewPager viewPager;
//    private View vFacilities;
//    private AlertDialog dialogFacilities;
//    private AlertDialog dialogOpenTime;
//    private TextView tvOpenStatus;
//    private TextView tvOpenTime;
//    private View vRestaurantInfo;
//    private TextView tvAddress;
//    private GetHomeTask mGetHomeTask;
//    private SuggestsFragment mFragmentSuggest;
//    private View vError;
//    private View vMap;
//    private View vWebsite;
//    private View vPhone;
//    private RecyclerView rvFacilitiesIcon;
//    private View vLoading;
//    private TextView tvRestaurantName;
//    private ImageView ivRestaurant;
//    private Button btnMenu;
//
//    @Override
//    protected void processArguments() {
//
//    }
//
//    @Override
//    protected int setLayout() {
//        return R.layout.fragment_home_page;
//    }
//
//    @Override
//    protected void showData() {
//
//    }
//
//    List<WeekDay> listOpenTime;
//
//    void setOnClickOpenTime(List<WeekDay> list) {
//        this.listOpenTime = list;
//        View.OnClickListener openTimeClick = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                showOpenTimeDialog(listOpenTime, getDayOfWeekOpenTime());
//            }
//        };
//        tvOpenStatus.setOnClickListener(openTimeClick);
//        tvOpenTime.setOnClickListener(openTimeClick);
//    }
//
//    void setOnClickFooter(final RestaurantInfo info) {
//        View.OnClickListener onClick = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.website:
//                        if (info.getWebsite() != null) {
//                            try {
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getWebsite()));
//                                startActivity(browserIntent);
//                            } catch (ActivityNotFoundException e) {
//                                Intent browserIntent = new Intent(getActivity(), WebsiteActivity.class);
//                                browserIntent.putExtra(WebsiteActivity.ARG_URL, info.getWebsite());
//                                startActivity(browserIntent);
//                            }
//                        }
//                        break;
//                    case R.id.phone:
//                        if (info.getPhone() != null) {
//                            CommonUtils.callRestaurant(getActivity());
//                        }
//                        break;
//                    case R.id.address:
//                    case R.id.map:
////                        Uri gmmIntentUri = Uri.parse("geo:" + info.getMap().latitude + "," + info.getMap().longitude + "?q="
////                                + Uri.encode(info.getAddress()));
////                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
////                        mapIntent.setPackage("com.google.android.apps.maps");
////                        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
////                            startActivity(mapIntent);
////                        }
//
//
//                        Intent intent = new Intent(getActivity(), MapActivity.class);
//                        intent.putExtra(MapActivity.ARG_MAP_INFO, new MapInfo(info.getMap(), info.getName(), info.getAddress(), info.getImageUrl()));
//                        startActivity(intent);
//                        break;
//                }
//            }
//        };
//        vMap.setOnClickListener(onClick);
//        vPhone.setOnClickListener(onClick);
//        vWebsite.setOnClickListener(onClick);
//    }
//
//    @Override
//    protected void initViews() {
//        setHasOptionsMenu(true);
//        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                onResume();
//            }
//        });
////        initAutoSlidePager(null);
//
//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (CommonUtils.isNetworkConnected(getActivity())) {
//                    startActivity(new Intent(getActivity(), MenuActivity.class));
//                } else {
//                    showNoConnection();
//                }
//            }
//        });
//
//        vRestaurantInfo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (CommonUtils.isNetworkConnected(getActivity())) {
//                    startActivity(new Intent(getActivity(), RestaurantInfoActivity.class));
//                } else {
//                    showNoConnection();
//                }
//            }
//        });
//        vError.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onResume();
//            }
//        });
//        initOpenTimeDialog();
//        initFacilitiesIcon();
//    }
//
//    OpenTimeAdapter openTimeAdapter;
//
//    protected void showOpenTimeDialog(List<WeekDay> days, int currentPosition) {
//        if (dialogOpenTime != null) {
//            openTimeAdapter.setData(days);
//            openTimeAdapter.setCurrentPosition(currentPosition);
//            openTimeAdapter.notifyDataSetChanged();
//            dialogOpenTime.show();
//        }
//    }
//
//    private void initOpenTimeDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        View layout = View.inflate(getActivity(),R.layout.dialog_open_time, null);
//        RecyclerView rvOpenTime = (RecyclerView) layout.findViewById(R.id.list_opentime);
//        rvOpenTime.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
//                false);
//        rvOpenTime.setLayoutManager(layoutManager);
//        openTimeAdapter = new OpenTimeAdapter(getActivity(), new ArrayList<WeekDay>(), 0);
//        rvOpenTime.setAdapter(openTimeAdapter);
//        layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialogOpenTime.dismiss();
//            }
//        });
//        builder.setView(layout);
//        builder.setCancelable(true);
//        dialogOpenTime = builder.create();
//    }
//
//    FacilityIconAdapter facilityIconAdapter;
//
//    void initFacilitiesIcon() {
//        rvFacilitiesIcon.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
//                false);
//        rvFacilitiesIcon.setLayoutManager(layoutManager);
//        facilityIconAdapter = new FacilityIconAdapter(getActivity(), new ArrayList<Facility>());
//        rvFacilitiesIcon.setAdapter(facilityIconAdapter);
//    }
//
//    void showFacilitiesDialog(ArrayList<Facility> facilities) {
//        if (dialogFacilities == null) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            View layout = View.inflate(getActivity(),R.layout.dialog_facilities, null);
//            RecyclerView rvFacilities = (RecyclerView) layout.findViewById(R.id.list_facilities);
//            rvFacilities.setHasFixedSize(true);
//            rvFacilities.addItemDecoration(new DividerItemDecoration(getActivity(),
//                    ContextCompat.getDrawable(getActivity(), R.drawable.adapter_promotional_divider), true, true));
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
//                    false);
//            rvFacilities.setLayoutManager(layoutManager);
//            FacilityAdapter adapter = new FacilityAdapter(getActivity(), facilities);
//            rvFacilities.setAdapter(adapter);
//            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialogFacilities.dismiss();
//                }
//            });
//            builder.setView(layout);
//            builder.setCancelable(true);
//            dialogFacilities = builder.create();
//        }
//        dialogFacilities.show();
//    }
//
//    @Override
//    protected void findViews(View rootView) {
//        btnMenu = (Button) rootView.findViewById(R.id.menu);
//        ivRestaurant = (ImageView) rootView.findViewById(R.id.restaurant_image);
//        mRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
////        viewPager = (SlideShowViewPager) rootView.findViewById(R.id.pager);
//        vFacilities = rootView.findViewById(R.id.next);
//        tvOpenStatus = (TextView) rootView.findViewById(R.id.open_status);
//        tvOpenTime = (TextView) rootView.findViewById(R.id.open_time);
//        tvAddress = (TextView) rootView.findViewById(R.id.address);
//        vRestaurantInfo = rootView.findViewById(R.id.view_info);
//        mFragmentReviewList = (ReviewListFragment) getChildFragmentManager().findFragmentById(R.id.review_list);
//        vError = rootView.findViewById(R.id.tvErrorMessage);
//        vMap = rootView.findViewById(R.id.map);
//        vPhone = rootView.findViewById(R.id.phone);
//        vWebsite = rootView.findViewById(R.id.website);
//        tvOpenStatus = (TextView) rootView.findViewById(R.id.open_status);
//        tvOpenTime = (TextView) rootView.findViewById(R.id.open_time);
//        vRestaurantInfo = rootView.findViewById(R.id.view_info);
//        rvFacilitiesIcon = (RecyclerView) rootView.findViewById(R.id.list_facilities_icon);
//        vLoading = rootView.findViewById(R.id.request_progress);
//
//        tvRestaurantName = (TextView) rootView.findViewById(R.id.restaurant_name);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_home, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
////        viewPager.stop();
//    }
//
//    int getDayOfWeekOpenTime() {
//        Calendar c = Calendar.getInstance();
////        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
////        if (dayOfWeek == -1) {
////            dayOfWeek = 6;
////        }
//        return c.get(Calendar.DAY_OF_WEEK) - 1;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        loadHome();
//        initSuggest();
//    }
//
//    private void loadHome() {
//        mGetHomeTask = new GetHomeTask(getActivity()) {
//
//            protected void onSuccess(final RestaurantInfo info, List<Food> suggests, List<RestaurantReview> reviews, int reviewCount, final ArrayList<Facility> facilities) {
//                tvRestaurantName.setText(info.getName());
//                tvAddress.setText(info.getAddress());
//                try {
//                    WeekDay openTime = info.getOpenTime().get(getDayOfWeekOpenTime());
//                    if (openTime.getFrom() != null && openTime.getTo() != null) {
//                        if (CommonUtils.isOpenTimeClose(openTime)) {
//                            tvOpenTime.setVisibility(View.INVISIBLE);
//                            tvOpenStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_red));
//                            tvOpenStatus.setText(getString(R.string.closed));
//                        } else {
//                            tvOpenTime.setVisibility(View.VISIBLE);
//                            tvOpenTime.setText(CommonUtils.formatOpenTime(getActivity(), openTime));
//                            Calendar c = Calendar.getInstance();
//                            if (c.getTime().after(openTime.getFrom()) && c.getTime().before(openTime.getTo())) {
//                                tvOpenStatus.setText(getString(R.string.opening));
//                                tvOpenStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_green));
//                            } else {
//                                tvOpenStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_red));
//                                tvOpenStatus.setText(getString(R.string.closed));
//                            }
//                        }
//                    }
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//                Picasso.with(getActivity()).load(info.getImageUrl()).placeholder(R.drawable.ic_no_photo_available).into(ivRestaurant);
//
//                mFragmentReviewList.setTSnackBarView(mRootView);
//                mFragmentReviewList.setReviews(reviews, info.getRate().getAverage(), info.getRate(), reviewCount, new ReviewListFragment.IOnSendSuccess() {
//                    @Override
//                    public void onSend() {
//                        onResume();
//                    }
//                });
//
//
//                setOnClickFooter(info);
//                setOnClickOpenTime(info.getOpenTime());
//                View.OnClickListener clickListener = new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        showFacilitiesDialog(facilities);
//                    }
//                };
//                rvFacilitiesIcon.setOnClickListener(clickListener);
//                vFacilities.setOnClickListener(clickListener);
//
//                facilityIconAdapter.setData(facilities);
//                facilityIconAdapter.notifyDataSetChanged();
//                ivRestaurant.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (CommonUtils.isNetworkConnected(getActivity())) {
//                            startActivity(new Intent(getActivity(), RestaurantPhotoActivity.class));
//                        } else {
//                            showNoConnection();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            protected void onError(int code) {
//                super.onError(code);
//            }
//        };
//        mGetHomeTask.setSwipeRefreshView(mRefresh);
//        mGetHomeTask.setErrorView(vError);
//        mGetHomeTask.setSnackbarView(mRootView);
//        mGetHomeTask.setLoadingView(vLoading);
//        mGetHomeTask.setStatus(requestStatus);
//        mGetHomeTask.execute();
//    }
//
//    private void initSuggest() {
//        if (mFragmentSuggest==null) {
//            mFragmentSuggest = new SuggestsFragment();
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(SuggestsFragment.ARG_IS_COLLAPSIBLE, false);
//            bundle.putBoolean(SuggestsFragment.ARG_IS_TAKE_AWAY, false);
//            mFragmentSuggest.setArguments(bundle);
//            mFragmentSuggest.setTSnackBarView(mRootView);
//            getChildFragmentManager().beginTransaction().replace(R.id.suggest, mFragmentSuggest).commit();
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (mGetHomeTask != null) {
//            mGetHomeTask.cancel(true);
//        }
//    }
//}
