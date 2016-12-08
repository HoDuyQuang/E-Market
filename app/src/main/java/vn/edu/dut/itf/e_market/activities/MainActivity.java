package vn.edu.dut.itf.e_market.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.fragments.ListReviewActivity;
import vn.edu.dut.itf.e_market.tasks.GetLogOutTask;
import vn.edu.dut.itf.e_market.views.CustomTypefaceSpan;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int LOGOUT_MENU_ORDER = 8;
//    private static final int NOTIFICATION_MENU_ORDER = 4;
    private static final int FAVORITE_MENU_ORDER = 3;
    private static final int REQUEST_CODE_SETTING = 0;
    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_REGISTER = 2;
    private static final int REQUEST_CODE_PROFILE = 3;
    public static final String ACTION_UPDATE_NOTIFICATION = "vn.com.brycen.restaurant.update_notification";
    private CountDownTimer mTimer;
    private boolean doubleBackTab = false;
    private MenuItem currentMenuItem;
    private NavigationView navigationView;
    private AlertDialog dialogLogout;
    private ViewGroup badgeLayout;

    private GetLogOutTask mLogoutTask;
    private BroadcastReceiver mUpdateNotification = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            getNotification();
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mUpdateNotification);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackTab) {
                super.onBackPressed();
            } else {
                toast(getString(R.string.press_back_again_to_exit));
                doubleBackTab = true;
                startTimer();
//				TSnackbar.make(findViewById(R.id.content), "No internet connection", TSnackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(mUpdateNotification, new IntentFilter(MainActivity.ACTION_UPDATE_NOTIFICATION));


    }

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void findViews() {
//        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
//        headerView.findViewById(R.id.nav_register).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_REGISTER);
//                drawer.closeDrawer(GravityCompat.START);
//            }
//        });
//
//        headerView.findViewById(R.id.nav_login).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_LOGIN);
//                drawer.closeDrawer(GravityCompat.START);
//            }
//        });
//        headerView.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (Authentication.isLoggedIn(MainActivity.this)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                    startActivityForResult(new Intent(MainActivity.this, ProfileActivity.class), REQUEST_CODE_PROFILE);
////                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new ProfileFragment())
////                            .commit();
////                    getSupportActionBar().setTitle(getString(R.string.profile));
//                }
//            }
//        });
//        headerView.findViewById(R.id.nav_order).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Authentication.isLoggedIn(MainActivity.this)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                    startActivity(new Intent(MainActivity.this, OrderListActivity.class));
//                }
//            }
//        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this,PayActivity.class));
            }
        });
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        chooseHome();
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            applyFont(navigationView.getMenu().getItem(i));
            // for (int j = 0; j <
            // navigationView.getMenu().getItem(i).getSubMenu().size(); j++) {
            // applyFont(navigationView.getMenu().getItem(i).getSubMenu().getItem(j));
            // }
        }
//        updateNotification(0);

    }

    private void chooseHome() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ListReviewActivity()).commit();
        currentMenuItem = navigationView.getMenu().getItem(0);
        currentMenuItem.setChecked(true);
        setTitle(getString(R.string.home));
    }

//    private void updateNotification(int count) {
//        TextView notification = (TextView) navigationView.getMenu().getItem(4).getActionView()
//                .findViewById(R.id.cart_counter);
//        if (count > 0) {
//            notification.setVisibility(View.VISIBLE);
//            notification.setText(String.valueOf(count));
//        } else {
//            notification.setVisibility(View.GONE);
//        }
//    }

    private void applyFont(MenuItem item) {
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/" + getString(R.string.font_primary));
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", myTypeface), 0, mNewTitle.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }

    private DrawerLayout drawer;

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        // Handle navigation view item clicks here.
//        loadLocale();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ListReviewActivity()).commit();
                        setTitle(getString(R.string.home));
                        break;
//                    case R.id.nav_menu:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MenuFragment()).commit();
//                        setTitle(getString(R.string.menu));
//                        break;
//                    case R.id.nav_favorite:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new FavouriteFragment())
//                                .commit();
//
//                       setTitle(getString(R.string.favorite));
//                        break;
//                    case R.id.nav_notification:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new NotificationFragment())
//                                .commit();
//                        setTitle(getString(R.string.notification));
//                        break;
//                    case R.id.nav_cart:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new CartFragment()).commit();
//                        setTitle(getString(R.string.cart));
//                        break;
//
//                    case R.id.nav_feedback:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new FeedbackFragment())
//                                .commit();
//                        setTitle(getString(R.string.feedback));
//                        break;
//
//                    case R.id.nav_help:
////                        Intent intent = new Intent(getApplicationContext(),
////                                HelpFragment.class);
////                        startActivity(intent);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HelpFragment()).commit();
//                        setTitle(getString(R.string.help));
////                        break;
//                        break;
//                    case R.id.nav_setting:
//                        startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), REQUEST_CODE_SETTING);
//                        return;
//                    case R.id.nav_about:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new AboutFragment()).commit();
//                        getSupportActionBar().setTitle(getString(R.string.about));
//                        break;
                    case R.id.nav_sign_out:
//                        showLogoutDialog();
                        break;
                }
            }
        }, 300);
        if (currentMenuItem != null) {
            currentMenuItem.setChecked(false);
        }
        item.setChecked(true);
        currentMenuItem = item;

        return true;
    }

//    private void showLogoutDialog() {
//        if (dialogLogout == null) {
//            dialogLogout = new CustomDialog(this, R.layout.dialog_log_out).setTitle(getString(R.string.sign_out))
//                    .setMessage(getString(R.string.logout_confirm_message))
//                    .setOKButton(getString(R.string.yes), new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            dialogLogout.dismiss();
//                            mLogoutTask = new GetLogOutTask(MainActivity.this) {
//                                @Override
//                                protected void onSuccess() {
//                                    super.onSuccess();
//                                    checkAuthenticate();
//                                    chooseHome();
//                                }
//
//                                @Override
//                                protected void onError(int code) {
//                                    super.onError(code);
//                                    checkAuthenticate();
//                                }
//                            };
//                            mLogoutTask.setShowProgressDialog(null, getString(R.string.logging_out), false);
//                            mLogoutTask.execute();
//                        }
//                    }).setCancelButton(getString(R.string.no), new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            dialogLogout.dismiss();
//                        }
//                    }).show();
//        }
//        dialogLogout.show();
//    }

    @Override
    public void initData() {

    }

    @Override
    public void showData() {
//        LocaleHelper.onCreate(this);
    }

//    private void getNotification() {
//        new GetCountNotificationNew(this) {
//            protected void onSuccess(int count) {
//                updateNotification(count);
//            }
//        }.execute();
//    }

    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                doubleBackTab = false;
            }
        };
        mTimer.start();
    }

    /**
     * Return to previous fragment in stack
     *
     * @param keyCode
     * @param event
     * @return if fragment is popped, consume a event and return true
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
//        super.onActivityResult(requestCode, resultCode, arg2);
//        switch (requestCode) {
//            case REQUEST_CODE_SETTING:
//                restartActivity();
//                break;
//            case REQUEST_CODE_LOGIN:
//                checkAuthenticate();
//                break;
//            case REQUEST_CODE_REGISTER:
//                if (resultCode == RESULT_OK) {
//                    checkAuthenticate();
//                }
//                break;
//            case REQUEST_CODE_PROFILE:
//                checkAuthenticate();
//                break;
//            default:
//                break;
//        }
//
//    }

//    private void checkAuthenticate() {
//        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
//        TextView tvDisplayName = (TextView) headerView.findViewById(R.id.display_name);
//        if (Authentication.isLoggedIn(this)) {
//            headerView.findViewById(R.id.login_register_button).setVisibility(View.GONE);
//            headerView.findViewById(R.id.nav_order).setVisibility(View.VISIBLE);
//            tvDisplayName.setText(Authentication.getDisplayName(this));
//            navigationView.getMenu().getItem(LOGOUT_MENU_ORDER).setVisible(true);
////            navigationView.getMenu().getItem(NOTIFICATION_MENU_ORDER).setVisible(true);
//            navigationView.getMenu().getItem(FAVORITE_MENU_ORDER).setVisible(true);
//            getNotification();
//        } else {
//            headerView.findViewById(R.id.login_register_button).setVisibility(View.VISIBLE);
//            headerView.findViewById(R.id.nav_order).setVisibility(View.GONE);
//            tvDisplayName.setText(R.string.welcome_guest);
//            navigationView.getMenu().getItem(LOGOUT_MENU_ORDER).setVisible(false);
////            navigationView.getMenu().getItem(NOTIFICATION_MENU_ORDER).setVisible(false);
//            navigationView.getMenu().getItem(FAVORITE_MENU_ORDER).setVisible(false);
//            getNotification();
//        }
//    }


    public void navigateToMenu() {
//        navigationView.setCheckedItem(R.id.nav_menu);
//        onNavigationItemSelected(navigationView.getMenu().getItem(1));
//        startActivity(new Intent(this, MenuActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkAuthenticate();
    }
}
