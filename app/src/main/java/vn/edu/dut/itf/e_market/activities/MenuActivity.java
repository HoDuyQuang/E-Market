package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.view.MenuItem;

import vn.com.brycen.restaurant.R;

public class MenuActivity extends CartWidgetActivity {

    @Override
    public int setLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public void findViews() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    void navigateToCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showData() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
