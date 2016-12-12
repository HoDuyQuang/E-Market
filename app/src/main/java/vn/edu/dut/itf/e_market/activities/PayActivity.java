package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.adapters.CategoryAdapter;
import vn.edu.dut.itf.e_market.adapters.CollegeAdapter;
import vn.edu.dut.itf.e_market.adapters.ProvinceAdapter;
import vn.edu.dut.itf.e_market.database.CategoryDao;
import vn.edu.dut.itf.e_market.database.ProvinceDao;
import vn.edu.dut.itf.e_market.models.Category;
import vn.edu.dut.itf.e_market.models.College;
import vn.edu.dut.itf.e_market.models.District;
import vn.edu.dut.itf.e_market.tasks.PostOrderTask;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.Validator;


public class PayActivity extends BaseActivity {

    public static final String ARG_FOOD_ID = "cart";
    public static final String ARG_ORDER_NOW = "order_now";

    private MaterialSpinner spDistrict, spCollege, spCategory;
    private ProvinceAdapter adapterProvince;
    private CollegeAdapter adapterCollege;
    private CategoryAdapter adapterCategory;
    private List<District> districts;
    private List<College> colleges;
    private List<Category> addressTypes;

    private EditText etFirstName, etLastName, etAddress, etPhone, etEmail;

    private Calendar deliveryDateTime;

    private SwipeRefreshLayout vRefresh;
    private boolean isOrderNow = false;

//    private GetProfileTask getProfileTask;
    private float subTotal;

//    private Profile profile;
    private TextInputLayout layoutFirstName;
    private TextInputLayout layoutLastName;
    private TextInputLayout layoutCategory;
    private TextInputLayout layoutPhone;
    private TextInputLayout layoutEmail;

//    private void getProfile() {
//        getProfileTask = new GetProfileTask(this) {
//            protected void onSuccess(Profile info) {
//                profile = info;
//
//                etFirstName.setText(info.getFirstName());
//                etLastName.setText(info.getLastName());
//                etAddress.setText(info.getAddress());
//                etPhone.setText(info.getPhone());
//                etEmail.setText(info.getEmail());
//                if (info.getDistrict()!=null) {
//                    int indexProvince = ProfileFragment.indexOfProvince(provinces, ProfileFragment.getProvinceIdByDistricId(provinces, info.getDistrict()));
//                    if (indexProvince == -1) {
//                        spDistrict.setSelection(0);
//                    } else {
//                        spDistrict.setSelection(indexProvince + 1);
//                    }
//                } else{
//                    spDistrict.setSelection(0);
//                }
//            }
//
//            protected void onError(int code) {
//                super.onError(code);
//            }
//
//        };
//        getProfileTask.setShowProgressDialog(null, getString(R.string.loading), false);
//        getProfileTask.setSwipeRefreshView(vRefresh);
//        getProfileTask.execute();
//
//    }

    private boolean checkField(String firstName, String lastName, String address, String phone, String email) {
        boolean result = true;
        if (firstName.isEmpty()) {
            layoutFirstName.setError(getString(R.string.firstname_required));
            if (result) {
                etFirstName.requestFocus();
            }
            result = false;
        } else {
            layoutFirstName.setErrorEnabled(false);
        }
        if (lastName.isEmpty()) {
            layoutLastName.setError(getString(R.string.feedback_content_required));
            if (result) {
                etLastName.requestFocus();
            }
            result = false;
        } else {
            layoutLastName.setErrorEnabled(false);
        }

        if (spDistrict.getSelectedItemPosition() == 0) {
            spDistrict.setError(R.string.choose_province);
            result = false;
        } else {
            spDistrict.setError(null);
        }

        if (spCollege.getSelectedItemPosition() == 0) {
            spCollege.setError(R.string.choose_district);
            result = false;
        } else {
            spCollege.setError(null);
        }

        if (address.isEmpty()) {
            layoutCategory.setError(getString(R.string.address_required));
            if (result) {
                etAddress.requestFocus();
            }
            result = false;
        } else {
            layoutCategory.setErrorEnabled(false);
        }
        if (phone.isEmpty()) {
            layoutPhone.setError(getString(R.string.phone_required));
            if (result) {
                etPhone.requestFocus();
            }
            result = false;
        } else {
            if (!Validator.isValidPhone(phone)) {
                layoutPhone.setError(getString(R.string.phone_invalid));
                if (result) {
                    etPhone.requestFocus();
                }
                result = false;
            } else {
                layoutPhone.setErrorEnabled(false);
            }
        }

        if (email.length() > 0) {
            if (!Validator.isValidEmail(email)) {
                layoutEmail.setError(getString(R.string.email_invalid));
                if (result) {
                    etEmail.requestFocus();
                }
                result = false;
            } else {
                layoutEmail.setErrorEnabled(false);
            }
        } else {
            layoutEmail.setError(getString(R.string.email_required));
            if (result) {
                etEmail.requestFocus();
            }
            result = false;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pay_complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
//            finish();
//            if (Authentication.isLoggedIn(this)){
//                startActivity(new Intent(this, OrderListActivity.class));
//            } else{
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            int addressType = addressTypes.get(spCategory.getSelectedItemPosition()).getId();

            if (checkField(firstName, lastName, address, phone, email)) {
                if (spCollege.getSelectedItemPosition() == 0) {
                    Snackbar.make(findViewById(R.id.view_data), R.string.choose_district, Snackbar.LENGTH_LONG).show();
                    return false;
                }
                int districtId = ((College) spCollege.getSelectedItem()).getId();
                    mPostTask = new PostOrderTask(PayActivity.this, firstName, lastName, districtId, address, phone, email, addressType) {
                        @Override
                        protected void onSuccess() {
//                            super.onSuccess(order);
//                            if (!isOrderNow) {
//                                CartDao cartDao = new CartDao(PayActivity.this);
//                                cartDao.deleteAll();
//                            }
//                            Intent i = new Intent(PayActivity.this, PayCompleteActivity.class);
//                            i.putExtra(PayCompleteActivity.ARG_ORDER, order);
//                            startActivity(i);
                            finish();
                        }
                    };
                    mPostTask.setShowProgressDialog(null, getString(R.string.sending_order), false);
                    mPostTask.execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private PostOrderTask mPostTask;
//    private View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btPayDone:
//
//            }
//        }
//    };

    @Override
    public int setLayout() {
        return R.layout.activity_pay;
    }

    @Override
    public void findViews() {
        spDistrict = (MaterialSpinner) findViewById(R.id.spProvice);
        spCollege = (MaterialSpinner) findViewById(R.id.spDistrict);
        spCategory = (MaterialSpinner) findViewById(R.id.spAddressType);

        etFirstName = (EditText) findViewById(R.id.etTitle);
        etLastName = (EditText) findViewById(R.id.etContent);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        layoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_title);
        layoutLastName = (TextInputLayout) findViewById(R.id.input_layout_content);


        vRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        layoutCategory = (TextInputLayout) findViewById(R.id.input_layout_address);
        layoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        layoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        getSupportActionBar().setTitle(R.string.pay);

//        vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshProfile();
//                vRefresh.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
//            mCarts = b.getParcelableArrayList(ARG_FOOD_ID);
        }
        districts = CommonUtils.getDistrictList(this);
//        provinces.add(0, new Province(null, getString(R.string.choose_province)));

        colleges = CommonUtils.getCollegesList(this);
        addressTypes = new CategoryDao(this).getAll();
        deliveryDateTime = Calendar.getInstance();
    }

    @Override
    public void showData() {

        if (districts != null) {
            adapterProvince = new ProvinceAdapter(getApplicationContext(), districts);
            spDistrict.setAdapter(adapterProvince);
        }
        adapterCollege = new CollegeAdapter(getApplicationContext(), colleges);
        spCollege.setAdapter(adapterCollege);
        spCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adapterCategory = new CategoryAdapter(getApplicationContext(), addressTypes);
        spCategory.setAdapter(adapterCategory);
        if (addressTypes.size() > 0) {
            spCategory.setSelection(0);
        }
        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                districts.clear();
//                if (position != -1) {
//                    districts.addAll(((District) spDistrict.getSelectedItem()).getListDistricts());
//                }
//                districts.add(0, new District(null, getString(R.string.choose_district)));
//                adapterCollege.notifyDataSetChanged();
//
//                if (profile != null) {
//                    if (position == ProfileFragment.indexOfProvince(provinces, ProfileFragment.getProvinceIdByDistricId(provinces, profile.getDistrict()))) {
//                        spCollege.setSelection(ProfileFragment.indexOfDistrict(districts, profile.getDistrict()) + 1);
//                    } else {
//                        spCollege.setSelection(0);
//                    }
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

//        refreshProfile();
    }

//    private void refreshProfile() {
//        if (Authentication.isLoggedIn(this)) {
//            getProfile();
//        }
//    }
}
