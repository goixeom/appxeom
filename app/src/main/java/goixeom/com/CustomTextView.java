package goixeom.com;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Huy on 6/9/2017.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public static final String Android_shema="http://schemas.android.com/apk/res/android";

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context,attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context,attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs){
        int textStyle = attrs.getAttributeIntValue(Android_shema,"textStyle",Typeface.NORMAL);
        Typeface typeface = selectTypeface(context,textStyle);
        setTypeface(typeface);

    }

    private Typeface selectTypeface(Context context, int textStyle){
        switch (textStyle){
            case Typeface.BOLD:
                return FontCache.getTypeface(context,"MyriadPro-Bold.otf");
            case Typeface.ITALIC:
                return FontCache.getTypeface(context,"MyriadPro-Regular.otf");
            case Typeface.NORMAL:
            default:
                return FontCache.getTypeface(context,"MyriadPro-Regular.otf");
        }

    }
}
