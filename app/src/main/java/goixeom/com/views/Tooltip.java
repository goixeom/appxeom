package goixeom.com.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import goixeom.com.R;


/**
 * Created by DuongKK on 4/29/2016.
 */
public class Tooltip extends ProgressDialog {

    Context context;
    private Dialog dialog;

    public Tooltip(Context context, String msg, boolean isCancelable) {
        super(context);
        this.context = context;
        dialog = new ProgressDialog(context);
        //   mProgressDialog.setMessage(msg);
        dialog.setCancelable(isCancelable);
    }

    public Tooltip(Context context) {
        super(context);

        this.context = context;
        LayoutInflater factory = LayoutInflater.from(context);
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = factory.inflate(R.layout.dialog_tooltip, null, false);
        LinearLayout llRoot = (LinearLayout) view.findViewById(R.id.ll_root_tooltip);
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideDialog();
                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.setCancelable(true);
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
