package vn.edu.dut.itf.e_market.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import vn.edu.dut.itf.e_market.R;

/**
 * @author d_quang
 *         <p>
 *         A class inherite TextView to show
 */
public class CustomFontTextView extends AppCompatTextView {

    public CustomFontTextView(Context context) {
        super(context);
        initCustomFont(null, -1);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomFont(attrs, -1);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCustomFont(attrs, defStyle);
    }

    private void initCustomFont(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.custom_font);
            String fontName = a.getString(R.styleable.custom_font_font);
            try {
                if (fontName == null) {
                    fontName = getContext().getString(R.string.font_primary);
                }
                int style = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android",
                        getContext().getResources().getResourceEntryName(android.R.attr.textStyle), Typeface.NORMAL);
                if (getTypeface() != null) {
                    style = getTypeface().getStyle();
                }

                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);

                // if (getTypeface()!=null){
                // setTypeface(myTypeface, getTypeface().getStyle());
                // } else{
//				int style = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android",
//						getContext().getResources().getResourceEntryName(android.R.attr.textStyle), Typeface.NORMAL);
//				if (defStyle!=-1){
//					setTypeface(myTypeface, defStyle);
//					return;
//				}
                setTypeface(myTypeface, style);
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
            a.recycle();
        }
    }
}
