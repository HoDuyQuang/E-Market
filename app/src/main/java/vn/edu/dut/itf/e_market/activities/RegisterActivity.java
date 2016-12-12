package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import vn.com.brycen.restaurant.R;
import vn.com.brycen.restaurant.tasks.PostRegisterTask;
import vn.com.brycen.restaurant.utils.CommonUtils;
import vn.com.brycen.restaurant.utils.Validator;
import vn.com.brycen.restaurant.views.notification.TSnackbar;

public class RegisterActivity extends BaseActivity {

    EditText edtEmailPhone;
    EditText edtPassword;
    EditText edtRePassword;
    EditText edtFName;
    Button btnDone;
    PostRegisterTask mPostRegister;
    private TextView tvError;
    TextInputLayout layoutEmail, layoutPassword, layoutConfirmPassword, layoutUsername;

    @Override
    public int setLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void findViews() {
        edtEmailPhone = (EditText) findViewById(R.id.edtEmailPhone);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        edtFName = (EditText) findViewById(R.id.edtUserName);
        btnDone = (Button) findViewById(R.id.btnDone);
        tvError = (TextView) findViewById(R.id.tvError);

        layoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email_phone);
        layoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        layoutConfirmPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);
        layoutUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
    }

    public void RegAccount() {

        mPostRegister = new PostRegisterTask(this, edtEmailPhone.getText().toString().trim(),
                edtPassword.getText().toString(), edtFName.getText().toString().trim()) {
            @Override
            protected void onSuccess() {
                super.onSuccess();
                setResult(RESULT_OK);
                if (!Validator.isValidEmail(edtEmailPhone.getText().toString().trim())) {
                    startActivity(new Intent(RegisterActivity.this, RegisterEmailActivity.class));
                }
                finish();
            }

            @Override
            protected void onError(int code) {
                super.onError(code);
                if (code == ERROR_EMAIL_PHONE_EXIST) {
                    TSnackbar.make(findViewById(R.id.root_view), R.string.username_exist, TSnackbar.LENGTH_LONG).show();
                }
            }
        };
        mPostRegister.setShowProgressDialog(null, getString(R.string.registering), false);
        mPostRegister.execute();

    }

    @Override
    public void initViews() {
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onRegister();
            }
        });
        edtFName.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onRegister();
                }
                return false;
            }
        });
    }

    private void onRegister() {
        if (CommonUtils.isNetworkConnected(RegisterActivity.this)) {
            if (validate(edtEmailPhone.getText().toString(), edtPassword.getText().toString(),
                    edtRePassword.getText().toString(), edtFName.getText().toString())) {
                RegAccount();
            }
        } else {
            showNoConnection(findViewById(R.id.root_view));
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void showData() {

    }

    boolean validate(String email, String password, String confirm, String username) {
        email = email.trim();
        username = username.trim();
        boolean result = true;
        if (email.length() == 0) {
            layoutEmail.setError(getString(R.string.email_phone_required));
            result = false;
        } else {
            if (!Validator.isValidEmailOrPhone(email)) {
                layoutEmail.setError(getString(R.string.email_phone_invalid));
                result = false;
            } else {
                layoutEmail.setErrorEnabled(false);
            }
        }
        if (password.length() == 0) {
            layoutPassword.setError(getString(R.string.password_required));
            result = false;
        } else {
            if (!Validator.isValidPassword(password)) {
                layoutPassword.setError(getString(R.string.password_invalid, Validator.MIN_PASSWORD_LENGTH, Validator.MAX_PASSWORD_LENGTH));
                result = false;
            } else {
                layoutPassword.setErrorEnabled(false);
            }
            if (confirm.length() == 0) {
                layoutConfirmPassword.setError(getString(R.string.confirm_password_required));
                result = false;
            } else {
                if (!confirm.equals(password)) {
                    layoutConfirmPassword.setError(getString(R.string.confirm_password_invalid));
                    result = false;
                } else {
                    layoutConfirmPassword.setErrorEnabled(false);
                }
            }
        }

        if (username.length() == 0) {
            layoutUsername.setError(getString(R.string.username_required));
            result = false;
        } else {
            layoutUsername.setErrorEnabled(false);
        }
        tvError.setVisibility(View.INVISIBLE);
        return result;

    }

    void showError(int resId, Object... args) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(getString(resId, args));
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
