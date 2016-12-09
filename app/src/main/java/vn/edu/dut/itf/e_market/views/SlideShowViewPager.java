package vn.edu.dut.itf.e_market.views;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import java.lang.reflect.Field;

import vn.edu.dut.itf.e_market.views.infinite.AdvertiseFragment;
import vn.edu.dut.itf.e_market.views.infinite.FixedSpeedScroller;
import vn.edu.dut.itf.e_market.views.infinite.InfinitePagerAdapter;

public class SlideShowViewPager extends ViewPager {
    CountDownTimer timer = new CountDownTimer(4000, 4000) {

        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            startSlide();
        }
    }.start();

    public SlideShowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideShowViewPager(Context context) {
        super(context);

    }
    PagerAdapter adapter;
    public void init(FragmentManager fragmentManager, String url) {
        final String[] urls = new String[]{url};
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new DecelerateInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
        }
        adapter = new FragmentPagerAdapter(fragmentManager) {

            @Override
            public int getCount() {
                return urls.length;
            }

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new AdvertiseFragment();
                Bundle args = new Bundle();
                args.putString(AdvertiseFragment.ARG_IMAGE_URL, urls[position]);
                fragment.setArguments(args);
                return fragment;
            }
        };

        // wrap pager to provide infinite paging with wrap-around
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);

        // actually an InfiniteViewPager

        setAdapter(wrappedAdapter);

    }

    public void startSlide() {
        if (adapter!=null&&adapter.getCount()>1) {
            setCurrentItem(getCurrentItem() + 1);
            timer.start();
        } else{
            setCurrentItem(0);
        }
    }

    public void stop() {
        timer.cancel();
    }
}
