package goixeom.com.views;

import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import goixeom.com.CustomTextView;

/**
 * Created by DuongKK on 6/23/2017.
 */

public class ClickPreventableTextView extends CustomTextView implements View.OnClickListener {
    private boolean preventClick;
    private OnClickListener clickListener;
    private boolean ignoreSpannableClick;

    public ClickPreventableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (getMovementMethod() != null)
            getMovementMethod().onTouchEvent(this, (Spannable)getText(), event);
        this.ignoreSpannableClick = true;
        boolean ret = super.onTouchEvent(event);
        this.ignoreSpannableClick = false;
        return ret;
    }

    /**
     * Returns true if click event for a clickable span should be ignored
     * @return true if click event should be ignored
     */
    public boolean ignoreSpannableClick() {
        return ignoreSpannableClick;
    }

    /**
     * Call after handling click event for clickable span
     */
    public void preventNextClick() {
        preventClick = true;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        this.clickListener = listener;
        super.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (preventClick) {
            preventClick = false;
        } else if (clickListener != null)
            clickListener.onClick(v);
    }
}