package vn.edu.dut.itf.e_market.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import vn.edu.dut.itf.e_market.R;


/**
 * @author d_quang
 * 
 *         A class inherited TextView to show
 */
public class CustomFontButton extends AppCompatButton {

	public CustomFontButton(Context context) {
		super(context);
		initCustomFont(null);
	}

	public CustomFontButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCustomFont(attrs);
	}

	public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initCustomFont(attrs);
	}

	private void initCustomFont(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.custom_font);
			String fontName = a.getString(R.styleable.custom_font_font);
			try {
				if (fontName == null) {
					fontName = getContext().getString(R.string.font_primary);
				}
				Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
				int style = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android",
						getContext().getResources().getResourceEntryName(android.R.attr.textStyle), Typeface.NORMAL);
				setTypeface(myTypeface, style);
			} catch (Exception e) {
				e.printStackTrace();
			}
			a.recycle();
		}
	}
}
