package vn.edu.dut.itf.e_market.activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.adapters.AppBaseAdapter;
import vn.edu.dut.itf.e_market.adapters.LanguageAdapter;
import vn.edu.dut.itf.e_market.utils.AppPref;
import vn.edu.dut.itf.e_market.views.customswitch.SwitchButton;

public class SettingsActivity extends BaseActivity {
	private RecyclerView rvLanguages;
	Switch swbNotification;

	@Override
	public int setLayout() {
		return R.layout.activity_setting_new;
	}

	@Override
	public void findViews() {
		rvLanguages = (RecyclerView) findViewById(R.id.list);
		swbNotification = (Switch) findViewById(R.id.sb_ios);
	}

	@Override
	public void initViews() {
		if (getSupportActionBar()!=null) {
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		setTitle(getString(R.string.setting));

		rvLanguages.setHasFixedSize(true);
		rvLanguages.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL));
		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		rvLanguages.setLayoutManager(layoutManager);

		swbNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				AppPref.getInstance(SettingsActivity.this).putBoolean(AppPref.KEY_PUSH_NOTIFICATION, isChecked);
			}
		});
	}

	@Override
	public void initData() {

	}

	@Override
	public void showData() {
		swbNotification.setChecked(AppPref.getInstance(this).getBoolean(AppPref.KEY_PUSH_NOTIFICATION, true));

		setText();
		ArrayList<String> list = new ArrayList<>();
		list.add(getString(R.string.english));
		list.add(getString(R.string.japan));
		list.add(getString(R.string.vietnamese));
		list.add(getString(R.string.myanmar));

		setTitle(getString(R.string.setting));

		LanguageAdapter adapter = new LanguageAdapter(this, list, true);
		rvLanguages.setAdapter(adapter);
		adapter.setOnItemClickListener(new AppBaseAdapter.IItemClickListener<String>() {

			@Override
			public void onItemClick(String item, int position) {
				// saveLocale(getResources().getStringArray(R.array.pref_language_values)[position]);
				saveLocale(getResources().getStringArray(R.array.pref_language_values)[position]);
				showData();
			}
		});
		String language = AppPref.getInstance(this).getString(AppPref.KEY_LANGUAGE, "en");
		String[] languages = getResources().getStringArray(R.array.pref_language_values);
		for (int i = 0; i < languages.length; i++) {
			if (languages[i].equals(language)) {
				adapter.setSelectedPosition(i);
				break;
			}
		}
	}

	void setText() {
		((TextView) findViewById(R.id.notification_title)).setText(getString(R.string.notify));
		((TextView) findViewById(R.id.notification)).setText(getString(R.string.allowpus));
		((TextView) findViewById(R.id.language_title)).setText(getString(R.string.language));
	}

}
