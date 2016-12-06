package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import fr.ganfra.materialspinner.MaterialSpinner;
import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.District;


public class PayActivity extends BaseActivity {

    public static final String ARG_FOOD_ID = "cart";
    public static final String ARG_ORDER_NOW = "order_now";

    private MaterialSpinner spProvince, spDistrict, spAddressType;
    private ProvinceAdapter adProvince;
    private DistrictAdapter adDistrict;
    private AddressTypeAdapter adAddressType;
    private List<Province> provinces;
    private List<District> districts;
    private List<AddressType> addressTypes;
    private ArrayList<Cart> mCarts;

    private EditText etFirstName, etLastName, etAddress, etPhone, etEmail, etNote;
    private TextView tvDate;
    private RadioGroup grTime;
    private RadioButton rbTime, rbAsSoonAs;
    private Calendar deliveryDateTime;
    private float fee = 0;
    private TextView tvSubTotal;
    private TextView tvTotal;
    private TextView tvDeliveryFee;

    private SwipeRefreshLayout vRefresh;
    private boolean isOrderNow = false;

    private GetProfileTask getProfileTask;
    private float subTotal;

    private Profile profile;
    private boolean getFeeDone;
    private TextInputLayout layoutFirstName;
    private TextInputLayout layoutLastName;
    private TextInputLayout layoutAddress;
    private TextInputLayout layoutPhone;
    private TextInputLayout layoutEmail;

    private void getProfile() {
        getProfileTask = new GetProfileTask(this) {
            protected void onSuccess(Profile info) {
                profile = info;

                etFirstName.setText(info.getFirstName());
                etLastName.setText(info.getLastName());
                etAddress.setText(info.getAddress());
                etPhone.setText(info.getPhone());
                etEmail.setText(info.getEmail());
                if (info.getDistrict()!=null) {
                    int indexProvince = ProfileFragment.indexOfProvince(provinces, ProfileFragment.getProvinceIdByDistricId(provinces, info.getDistrict()));
                    if (indexProvince == -1) {
                        spProvince.setSelection(0);
                    } else {
                        spProvince.setSelection(indexProvince + 1);
                    }
                } else{
                    spProvince.setSelection(0);
                }
            }

            protected void onError(int code) {
                super.onError(code);
            }

        };
        getProfileTask.setShowProgressDialog(null, getString(R.string.loading), false);
        getProfileTask.setSwipeRefreshView(vRefresh);
        getProfileTask.execute();

    }

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
            layoutLastName.setError(getString(R.string.lastname_required));
            if (result) {
                etLastName.requestFocus();
            }
            result = false;
        } else {
            layoutLastName.setErrorEnabled(false);
        }

        if (spProvince.getSelectedItemPosition() == 0) {
            spProvince.setError(R.string.choose_province);
            result = false;
        } else {
            spProvince.setError(null);
        }

        if (spDistrict.getSelectedItemPosition() == 0) {
            spDistrict.setError(R.string.choose_district);
            result = false;
        } else {
            spDistrict.setError(null);
        }

        if (address.isEmpty()) {
            layoutAddress.setError(getString(R.string.address_required));
            if (result) {
                etAddress.requestFocus();
            }
            result = false;
        } else {
            layoutAddress.setErrorEnabled(false);
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

    private PostOrderTask mPostTask;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btPayDone:
                    String firstName = etFirstName.getText().toString().trim();
                    String lastName = etLastName.getText().toString().trim();
                    String address = etAddress.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();

                    String note = etNote.getText().toString();
                    int addressType = addressTypes.get(spAddressType.getSelectedItemPosition()).getId();

                    if (checkField(firstName, lastName, address, phone, email)) {
                        if (spDistrict.getSelectedItemPosition() == 0) {
                            TSnackbar.make(findViewById(R.id.view_data), R.string.choose_district, TSnackbar.LENGTH_LONG).show();
                            break;
                        }
                        String districtId = ((District) spDistrict.getSelectedItem()).getId();
                        if (getFeeDone) {
                            mPostTask = new PostOrderTask(PayActivity.this, fee, mCarts, firstName, lastName, districtId, address, phone, email, note, addressType, deliveryDateTime, rbAsSoonAs.isChecked()) {
                                @Override
                                protected void onSuccess(Order order) {
                                    super.onSuccess(order);
                                    if (!isOrderNow) {
                                        CartDao cartDao = new CartDao(PayActivity.this);
                                        cartDao.deleteAll();
                                    }
                                    Intent i = new Intent(PayActivity.this, PayCompleteActivity.class);
                                    i.putExtra(PayCompleteActivity.ARG_ORDER, order);
                                    startActivity(i);
                                    finish();
                                }
                            };
                            mPostTask.setShowProgressDialog(null, getString(R.string.sending_order), false);
                            mPostTask.execute();
                        } else {
                            TSnackbar.make(findViewById(R.id.view_data), R.string.unable_get_fee, TSnackbar.LENGTH_LONG).show();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public int setLayout() {
        return R.layout.activity_pay;
    }

    @Override
    public void findViews() {
        spProvince = (MaterialSpinner) findViewById(R.id.spProvice);
        spDistrict = (MaterialSpinner) findViewById(R.id.spDistrict);
        spAddressType = (MaterialSpinner) findViewById(R.id.spAddressType);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNote = (EditText) findViewById(R.id.etNote);

        layoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        layoutLastName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        layoutAddress = (TextInputLayout) findViewById(R.id.input_layout_address);
        layoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        layoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);


        tvDate = (TextView) findViewById(R.id.tvDate);
        grTime = (RadioGroup) findViewById(R.id.grTime);
        rbTime = (RadioButton) findViewById(R.id.rbTime);
        rbAsSoonAs = (RadioButton) findViewById(R.id.rbAsSoonAs);
        tvSubTotal = (TextView) findViewById(R.id.tvSubtotal);
        tvDeliveryFee = (TextView) findViewById(R.id.tvDeliveryFee);
        tvTotal = (TextView) findViewById(R.id.tvTotal);


        vRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

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

        findViewById(R.id.btPayDone).setOnClickListener(clickListener);
        tvDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SelectDateFragment newFragment = new SelectDateFragment();
                Bundle bundle = new Bundle();
                if (deliveryDateTime != null) {
                    bundle.putLong(SelectDateFragment.ARG_DATE, deliveryDateTime.getTime().getTime());
                    newFragment.setArguments(bundle);
                }
                newFragment.setOnDateSetListener(new SelectDateFragment.IOnDateSetListener() {

                    @Override
                    public void onDateSet(Calendar calendar) {
                        setDate(calendar);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        rbTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SelectTimeFragment newFragment = new SelectTimeFragment();
                Bundle bundle = new Bundle();
                if (deliveryDateTime != null) {
                    bundle.putLong(SelectTimeFragment.ARG_TIME, deliveryDateTime.getTime().getTime());
                    newFragment.setArguments(bundle);
                }
                newFragment.setOnDateSetListener(new SelectTimeFragment.IOnTimeSetListener() {

                    @Override
                    public void onTimeSet(Calendar calendar) {
                        deliveryDateTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                        deliveryDateTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                        rbTime.setText(CommonUtils.formatTime(PayActivity.this, deliveryDateTime.getTime()));
                    }
                });
                newFragment.show(getSupportFragmentManager(), "TimePicker");
            }
        });

        vRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProfile();
                vRefresh.setRefreshing(false);
            }
        });

        isOrderNow = getIntent().getBooleanExtra(ARG_ORDER_NOW, false);
        rbAsSoonAs.setText(AppPref.getInstance(this).getString(AppPref.KEY_EXPECT_TIME_TEXT, getString(R.string.as_soon_as)));
    }

    private void setDate(Calendar calendar) {
        deliveryDateTime.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        tvDate.setText(CommonUtils.formatDate(PayActivity.this, deliveryDateTime.getTime()));

    }

    @Override
    public void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            mCarts = b.getParcelableArrayList(ARG_FOOD_ID);
        }
        provinces = new ProvinceDao(this).getProvinces();
//        provinces.add(0, new Province(null, getString(R.string.choose_province)));

        districts = new ArrayList<>();
        addressTypes = new AddressTypeDao(this).getAll();
        deliveryDateTime = Calendar.getInstance();
    }

    @Override
    public void showData() {

        if (provinces != null) {
            adProvince = new ProvinceAdapter(getApplicationContext(), provinces);
            spProvince.setAdapter(adProvince);
        }
        adDistrict = new DistrictAdapter(getApplicationContext(), districts);
        spDistrict.setAdapter(adDistrict);
        spDistrict.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
//                    if (Authentication.isLoggedIn(PayActivity.this) && profile != null) {
                    getFee();
//                    }
                } else {
                    tvDeliveryFee.setText(R.string.free);
                    tvTotal.setText(CommonUtils.formatPrice(PayActivity.this, subTotal + fee));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adAddressType = new AddressTypeAdapter(getApplicationContext(), addressTypes);
        spAddressType.setAdapter(adAddressType);
        if (addressTypes.size() > 0) {
            spAddressType.setSelection(0);
        }
        spProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districts.clear();
                if (position != -1) {
                    districts.addAll(((Province) spProvince.getSelectedItem()).getListDistricts());
                }
//                districts.add(0, new District(null, getString(R.string.choose_district)));
                adDistrict.notifyDataSetChanged();

                if (profile != null) {
                    if (position == ProfileFragment.indexOfProvince(provinces, ProfileFragment.getProvinceIdByDistricId(provinces, profile.getDistrict()))) {
                        spDistrict.setSelection(ProfileFragment.indexOfDistrict(districts, profile.getDistrict()) + 1);
                    } else {
                        spDistrict.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


        subTotal = CartFragment.calculateSummaryPayment(mCarts);
        // Set subTotal of money
        tvSubTotal.setText(CommonUtils.formatPrice(this, subTotal));
        tvTotal.setText(CommonUtils.formatPrice(this, subTotal + fee));
        setDate(Calendar.getInstance());

        refreshProfile();
    }

    private void refreshProfile() {
        if (Authentication.isLoggedIn(this)) {
            getProfile();
        }
    }


    private void getFee() {
        tvDeliveryFee.setText(R.string.loading);
        GetFeeTask task = new GetFeeTask(this, ((District) spDistrict.getSelectedItem()).getId()) {
            @Override
            protected void onSuccess(float fee) {
                super.onSuccess(fee);
                getFeeDone = true;
                PayActivity.this.fee = fee;
                if (fee>0) {
                    tvDeliveryFee.setText(CommonUtils.formatPrice(PayActivity.this, fee));
                } else{
                    tvDeliveryFee.setText(R.string.free);
                }
                tvTotal.setText(CommonUtils.formatPrice(PayActivity.this, subTotal + fee));
            }

            @Override
            protected void onError(int code) {
                super.onError(code);
                getFeeDone = false;
                tvDeliveryFee.setText(R.string.free);
                tvTotal.setText(CommonUtils.formatPrice(PayActivity.this, subTotal));
            }
        };
        task.setShowProgressDialog(null, getString(R.string.loading), false);
        task.execute();
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
