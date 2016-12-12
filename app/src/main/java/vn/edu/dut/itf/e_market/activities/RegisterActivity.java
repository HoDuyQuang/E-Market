package vn.edu.dut.itf.e_market.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.tasks.PostRegisterTask;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.Validator;

public class RegisterActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText edtEmailPhone;
    EditText edtPassword;
    EditText edtRePassword;
    EditText edtFName;
    Button btnDone;
    PostRegisterTask mPostRegister;
    private TextView tvError;
    TextInputLayout layoutEmail, layoutPassword, layoutConfirmPassword, layoutUsername;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

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

//        mPostRegister = new PostRegisterTask(this, edtEmailPhone.getText().toString().trim(),
//                edtPassword.getText().toString(), edtFName.getText().toString().trim()) {
//            @Override
//            protected void onSuccess() {
//                super.onSuccess();
//                setResult(RESULT_OK);
//
//                finish();
//            }
//
//            @Override
//            protected void onError(int code) {
//                super.onError(code);
//                if (code == ERROR_EMAIL_PHONE_EXIST) {
////                    TSnackbar.make(findViewById(R.id.root_view), R.string.username_exist, TSnackbar.LENGTH_LONG).show();
//                }
//            }
//        };
//        mPostRegister.setShowProgressDialog(null, getString(R.string.registering), false);
//        mPostRegister.execute();

        String email = edtEmailPhone.getText().toString().trim();
        String password = edtPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    private static final String TAG = "Register";
    private void initFirebase() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth = FirebaseAuth.getInstance();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
