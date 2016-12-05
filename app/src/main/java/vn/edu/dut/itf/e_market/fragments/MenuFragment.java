package vn.edu.dut.itf.e_market.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dut.itf.e_market.R;


/**
 * @author d_quang
 */

public class MenuFragment extends BaseFragment {
    private RecyclerView rvMenu;
    private Spinner spinnerCategories;
    private Spinner spinnerSort;
    private GetMenuTask mGetMenuTask;
    private RadioGroup filterGroup;
    private SuggestsFragment mFragmentSuggest;
    private boolean isTakeAway = false;
    private List<Food> dishes;
    private MenuAdapter adapter;
    private TextView tvNumberItem;
    private View vLoading;
    private SwipeRefreshLayout vRefresh;
    private View vError;

    @Override
    protected void processArguments() {

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_menu;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void showData() {

        loadMenu(false, 0, 10);
        // Spinner element
        // initSpinnerCategories();
        // initSpinnerSort();
    }

    private boolean isRunning = false;
    private int lastStartIndex = -1;
    private boolean isLoadMore = false;

    private void loadMenu(final boolean isTakeAway, int start, @SuppressWarnings("SameParameterValue") int count) {
        if (isLoadMore) {
            if (start > lastStartIndex) {
                lastStartIndex = start;
            } else {
                return;
            }
        } else {
            lastStartIndex = -1;
        }
        if (!isRunning) {
            this.isTakeAway = isTakeAway;
            mGetMenuTask = new GetMenuTask(getActivity(), isTakeAway, start, count) {
                @Override
                protected void onSuccess(List<Category> categories, int itemCount) {
                    super.onSuccess(categories, itemCount);
                    if (!isLoadMore) {
                        dishes.clear();
                    }
                    for (Category category : categories) {
                        for (Food dish : category.getFoods()) {
                            dish.setCategory(category);
                        }
                        dishes.addAll(category.getFoods());
                    }
                    adapter.notifyDataSetChanged();
                    tvNumberItem.setText(getString(R.string.number_items, itemCount));

//                    mFragmentSuggest.setData(suggests, true);

                    isLoadMore = true;
                }

                @Override
                protected void onError(int code) {
                    super.onError(code);
                    lastStartIndex = -1;
                    isRunning = false;
                }

                @Override
                protected void onPreExecute() {
                    isRunning = true;
                    super.onPreExecute();

                }

                @Override
                protected void onPostExecute(String result) {
                    isRunning = false;
                    super.onPostExecute(result);
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    isRunning = false;
                }
            };

            mGetMenuTask.setSwipeRefreshView(vRefresh);
            mGetMenuTask.setErrorView(vError);
            mGetMenuTask.setSnackbarView(mRootView);
            if (!isLoadMore) {
                mGetMenuTask.setLoadingView(vLoading);
            }
            mGetMenuTask.setStatus(requestStatus);
            mGetMenuTask.execute();
        }
    }

    private void cancelCurrentTask() {
        if (mGetMenuTask != null) {
            mGetMenuTask.cancel(true);
        }
    }

    private void initSpinnerCategories() {
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_item,
                SampleData.getSampleCategories());
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(dataAdapter);
    }

    private void initSpinnerSort() {
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_item,
                SampleData.getSampleSortType());
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(dataAdapter);
    }

    private LinearLayoutManager layoutManager;

    @Override
    protected void initViews() {
        setHasOptionsMenu(true);


        rvMenu.setHasFixedSize(true);
        rvMenu.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvMenu.setLayoutManager(layoutManager);

        dishes = new ArrayList<>();
        adapter = new MenuAdapter(getActivity(), dishes);
        adapter.setSnackBarView(mRootView);
        // adapter.setOnItemClickListener();
        rvMenu.setAdapter(adapter);
        rvMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && layoutManager.findLastVisibleItemPosition() >= dishes.size() - 3) {
                    loadMenu(((RadioButton) filterGroup.findViewById(R.id.take_away)).isChecked(), dishes.size(), 10);
                }
            }
        });

        filterGroup.check(R.id.all);
        filterGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.all:
                        isLoadMore = false;
                        loadMenu(false, 0, 10);
                        mFragmentSuggest.load(false, true);
                        break;
                    case R.id.take_away:
                        isLoadMore = false;
                        loadMenu(true, 0, 10);
                        mFragmentSuggest.load(true, true);
                        break;
                    default:
                        break;
                }
            }
        });

        vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                loadMenu(isTakeAway, 0, 10);
            }
        });
        vError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoadMore = false;
                loadMenu(isTakeAway, 0, 10);
            }
        });

        initSuggest();
    }

    private void initSuggest() {
        if (mFragmentSuggest==null) {
            mFragmentSuggest = new SuggestsFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(SuggestsFragment.ARG_IS_COLLAPSIBLE, true);
            bundle.putBoolean(SuggestsFragment.ARG_IS_TAKE_AWAY, false);
            mFragmentSuggest.setArguments(bundle);
            mFragmentSuggest.setTSnackBarView(mRootView);
            getChildFragmentManager().beginTransaction().replace(R.id.suggest, mFragmentSuggest).commit();
        }
    }


    @Override
    protected void findViews(View rootView) {
        tvNumberItem = (TextView) rootView.findViewById(R.id.number_item);
        rvMenu = (RecyclerView) rootView.findViewById(R.id.list);
        spinnerCategories = (Spinner) rootView.findViewById(R.id.categories);
        spinnerSort = (Spinner) rootView.findViewById(R.id.sort);
        filterGroup = (RadioGroup) rootView.findViewById(R.id.filter);
//        mFragmentSuggest = (SuggestsFragment) getChildFragmentManager().findFragmentById(R.id.suggest);
        vError = rootView.findViewById(R.id.tvErrorMessage);
        vLoading = rootView.findViewById(R.id.request_progress);
        vRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelCurrentTask();
    }
}
