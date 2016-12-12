package vn.edu.dut.itf.e_market.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.adapters.AppBaseAdapter;
import vn.edu.dut.itf.e_market.adapters.LanguageAdapter;
import vn.edu.dut.itf.e_market.utils.AppPref;

public class LanguageActivity extends BaseActivity {

	private RecyclerView rvLanguages;
	private Button btnDone;

	@Override
	public int setLayout() {
		return R.layout.activity_language;
	}

	@Override
	public void findViews() {
		rvLanguages = (RecyclerView) findViewById(R.id.list);
		btnDone = (Button) findViewById(R.id.done);
	}

	@Override
	public void initViews() {
		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onDone();
			}
		});
		rvLanguages.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		rvLanguages.setLayoutManager(layoutManager);
	}

	private void onDone() {
		AppPref.getInstance(this).putBoolean(AppPref.KEY_FIRST_OPEN_APP, true);
		finish();
		startActivity(new Intent(this, SplashActivity.class));
	}

	@Override
	public void initData() {

	}

	@Override
	public void showData() {
		setText();
		ArrayList<String> list = new ArrayList<>();
		list.add(getString(R.string.english));
		list.add(getString(R.string.vietnamese));

		LanguageAdapter adapter = new LanguageAdapter(this, list);
		rvLanguages.setAdapter(adapter);
		adapter.setOnItemClickListener(new AppBaseAdapter.IItemClickListener<String>() {

			@Override
			public void onItemClick(String item, int position) {
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

	private void setText() {
		((TextView) findViewById(R.id.choose)).setText(getString(R.string.please_choose_your_language));
		((TextView) findViewById(R.id.done)).setText(getString(R.string.done));
	}
}
