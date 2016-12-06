package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import vn.com.brycen.restaurant.R;
import vn.com.brycen.restaurant.models.Order;
import vn.com.brycen.restaurant.utils.Authentication;
import vn.com.brycen.restaurant.utils.CommonUtils;

/**
 * Created by Gossip
 * dev@gossip.com
 */
public class PayCompleteActivity extends BaseActivity {
    public static  final String ARG_ORDER="order";
    Order order;
    private TextView tvOrderId;
    private TextView tvBuyDate;
    private TextView tvTotal;

    @Override
    public int setLayout() {
        return R.layout.activity_pay_complete;
    }

    @Override
    public void findViews() {
        tvOrderId= (TextView) findViewById(R.id.order_id);
        tvBuyDate=(TextView)findViewById(R.id.buy_date);
        tvTotal=(TextView)findViewById(R.id.total);
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.pay_completed);
    }

    @Override
    public void initData() {
        order=getIntent().getParcelableExtra(ARG_ORDER);
    }

    @Override
    public void showData() {
        if (order!=null){
            tvOrderId.setText(order.getId());
            tvBuyDate.setText(CommonUtils.formatDateTime(this,order.getOrderDate()));
            tvTotal.setText(CommonUtils.formatPrice(this,order.getSubTotal()+order.getFee()));
        }
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
            finish();
            if (Authentication.isLoggedIn(this)){
                startActivity(new Intent(this, OrderListActivity.class));
            } else{
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
