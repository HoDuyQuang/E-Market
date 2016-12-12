package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.Validator;


public class ForgotPasswordActivity extends BaseActivity {

    private static final int REQUEST_CODE_USERNAME = 1;
    private Button btnSend;
    private EditText edtEmail;
//    private SendForgotPasswordTask mSendTask;
    private TextInputLayout layoutEmail;

    @Override
    public int setLayout() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void findViews() {
        btnSend = (Button) findViewById(R.id.send);
        edtEmail = (EditText) findViewById(R.id.email);

        layoutEmail = (TextInputLayout) findViewById(R.id.input_layout_current_password);
    }

    @Override
    public void initViews() {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        setTitle(getString(R.string.forgot_password));

        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtils.isNetworkConnected(ForgotPasswordActivity.this)) {
                    showNoConnection(findViewById(R.id.view_data));
                    return;
                }
                final String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    layoutEmail.setError(getString(R.string.email_required));
                } else {
                    if (!Validator.isValidEmail(email)) {
                        layoutEmail.setError(getString(R.string.email_invalid));
                    } else {
                        layoutEmail.setErrorEnabled(false);
//                        mSendTask = new SendForgotPasswordTask(ForgotPasswordActivity.this, email) {
//                            @Override
//                            protected void onSuccess() {
//                                super.onSuccess();
//                                ForgotPasswordActivity.this.onSuccess();
//                            }
//
//                            @Override
//                            protected void onError(int code) {
//
//                                if (code == 3) {
//                                    TSnackbar.make(findViewById(R.id.view_data), R.string.email_not_exist, TSnackbar.LENGTH_LONG).show();
//                                    return;
//                                }
//                                if (code == 8) {
//                                    Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordUsernameActivity.class);
//                                    intent.putExtra(ForgotPasswordUsernameActivity.ARG_EMAIL,email);
//                                    startActivityForResult(intent, REQUEST_CODE_USERNAME);
//
//                                    return;
//                                }
//                                super.onError(code);
//                            }
//                        };
//                        mSendTask.setShowProgressDialog(null, getString(R.string.sending), false);
//                        mSendTask.setSnackbarView(findViewById(R.id.view_data));
//                        mSendTask.execute();
                    }
                }
            }
        });
    }

    private void onSuccess() {
        edtEmail.setText("");
//        showSuccessSnack(findViewById(R.id.view_data), getString(R.string.reset_link_sent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_USERNAME) {
            if (resultCode == RESULT_OK) {
                onSuccess();
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void showData() {

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
