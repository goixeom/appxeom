package goixeom.com.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import goixeom.com.CustomTextView;
import goixeom.com.R;

//package com.example.huy.goixeom_08_06.views;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.support.v7.view.ContextThemeWrapper;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.huy.goixeom_08_06.R;
//import OnResponse;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by MyPC on 9/4/2016.
// */
public class AlertDialogCustom {
    public static AlertDialog dialogMessage(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_lost_connect, null, false);
        CustomTextView button = (CustomTextView) view.findViewById(R.id.tv_ok);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return alertDialog;
    }
//
////    public static Dialog dialogMsg(Context context, String message, final OnResponse onResponse) {
////        LayoutInflater factory = LayoutInflater.from(context);
////        final Dialog dialog = new Dialog(context);
////        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
////
////        View view = factory.inflate(R.layout.dialog_message_custom, null, false);
////        TextView textView = (TextView) view.findViewById(R.id.tv_mess);
////        textView.setText(message);
////        Button button = (Button) view.findViewById(R.id.btn_ok);
////        dialog.getWindow().setBackgroundDrawable(
////                new ColorDrawable(android.graphics.Color.TRANSPARENT));
////        dialog.setContentView(view);
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                dialog.cancel();
////                onResponse.onResponse(null);
////            }
////        });
////
////
////        return dialog;
////    }
//
//    public static Dialog dialogMsg(Context context, String message) {
//        LayoutInflater factory = LayoutInflater.from(context);
//        final Dialog dialog = new Dialog(context);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//        View view = factory.inflate(R.layout.dialog_message_custom, null, false);
//        TextView textView = (TextView) view.findViewById(R.id.tv_mess);
//        textView.setText(message);
//        Button button = (Button) view.findViewById(R.id.btn_ok);
//        dialog.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(view);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.cancel();
//            }
//        });
//
//
//        return dialog;
//    }
//
//    public static Dialog dialogConfirm(Context context, String title, String message, String buttonLeft, String buttonRight
//            , final OnResponse onResponeL, final OnResponse onResponseR) {
//        LayoutInflater factory = LayoutInflater.from(context);
//        final Dialog dialog = new Dialog(context);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//        View view = factory.inflate(R.layout.dialog_confirm, null, false);
//        TextView tvMess = (TextView) view.findViewById(R.id.tv_mess);
//        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        TextView tvR = (TextView) view.findViewById(R.id.tv_right);
//        TextView tvL = (TextView) view.findViewById(R.id.tv_left);
//
//        CardView cardLeft = (CardView) view.findViewById(R.id.ll_left);
//        CardView cardRight = (CardView) view.findViewById(R.id.ll_right);
//
//        tvMess.setText(message);
//        tvTitle.setText(title);
//        tvL.setText(buttonLeft);
//        tvR.setText(buttonRight);
//        dialog.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(view);
//        cardLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onResponeL.onResponse(null);
//                dialog.cancel();
//            }
//        });
//        cardRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onResponseR.onResponse(null);
//                dialog.cancel();
//            }
//        });
//
//        return dialog;
//    }
//
//
//    public static Dialog dialogSelection(Context context, String title, List<String> list, final OnResponse<Integer> onResponse) {
//        LayoutInflater factory = LayoutInflater.from(context);
//        final Dialog dialog = new Dialog(context);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//        View view = factory.inflate(R.layout.dialog_selection, null, false);
//        TextView textView = (TextView) view.findViewById(R.id.tv_title);
//        textView.setText(title);
//        CardView button = (CardView) view.findViewById(R.id.ll_ok);
//        int id = 0;
//        final int[] posSelection = new int[1];
//        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
//        for (String s : list) {
//            RadioButton rad = new RadioButton(context);
//            rad.setText(s);
//            rad.setId(id);
//            rad.setButtonDrawable(null);
//            rad.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_radio, 0);
//            id++;
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelOffset(R.dimen.size_selection));
////            params.setMargins(0,20,0,100);
//
////            rad.setPadding(0,0,0,100);
//            rad.setGravity(Gravity.CENTER_VERTICAL);
//            radioGroup.addView(rad, params);
//        }
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                posSelection[0] = i;
//                onResponse.onResponse(posSelection[0]);
//                dialog.cancel();
//
//
//            }
//        });
//        dialog.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(view);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onResponse.onResponse(posSelection[0]);
//
//                dialog.cancel();
//            }
//        });
//
//
//        return dialog;
//    }
//
//
//    public static Dialog dialogFilterGallery(final Context context, int code, Map<String, String> mapInput, final OnResponse<Map<String, String>> mapOnResponse) {
//        LayoutInflater factory = LayoutInflater.from(context);
//        final Dialog dialog;
//        dialog = new Dialog(new ContextThemeWrapper(context, R.style_json.DialogSlideAnim));
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        final boolean[] hasFilter = {false};
//        View view = factory.inflate(R.layout.dialog_filter_gallery, null, false);
//        Button btnCancel = (Button) view.findViewById(R.id.cancel);
//        TextView tvClear = (TextView) view.findViewById(R.id.tv_clear);
//        final ToggleButtonGroupTableLayout radioGroupHUE = (ToggleButtonGroupTableLayout) view.findViewById(R.id.rad_group_hues);
//        final RadioGroup radioGroupSize = (RadioGroup) view.findViewById(R.id.rad_group_size);
//        final RadioGroup radioGroupMaterial = (RadioGroup) view.findViewById(R.id.rad_group_material);
//        final LinearLayout llRotSize = (LinearLayout) view.findViewById(R.id.ll_root_size);
//        RadioButton radMaterialDefault2 = (RadioButton) radioGroupMaterial.findViewById(R.id.rad_material2);
//        RadioButton radMaterialDefault1 = (RadioButton) radioGroupMaterial.findViewById(R.id.rad_recyled);
//
//        RadioButton radMaterialDefault3 = (RadioButton) radioGroupMaterial.findViewById(R.id.rad_material3);
//        final RadioButton[] radioHUE = new RadioButton[1];
//        final RadioButton[] radioMaterial = new RadioButton[1];
//        final RadioButton[] radioSize = new RadioButton[1];
//        final Map<String, String> dictionary = new HashMap<>();
//        dictionary.put(context.getString(R.string.white), "white");
//        dictionary.put(context.getString(R.string.black), "black");
//        dictionary.put(context.getString(R.string.grey), "grey");
//        dictionary.put(context.getString(R.string.beige), "beige");
//        dictionary.put(context.getString(R.string.brown), "brow");
//        dictionary.put(context.getString(R.string.red), "red");
//        dictionary.put(context.getString(R.string.traditional), "traditional");
//        dictionary.put(context.getString(R.string.contemorary), "contemorary");
//        dictionary.put(context.getString(R.string.transittional), "transittional");
//
//        if (code == Constant.GALLERY) {
////            llRotSize.setVisibility(View.GONE);
//            radMaterialDefault1.setText(R.string.traditional);
//            radMaterialDefault2.setText(R.string.contemorary);
//            radMaterialDefault3.setText(R.string.transittional);
//
//            radMaterialDefault2.setVisibility(View.VISIBLE);
//            radMaterialDefault3.setVisibility(View.VISIBLE);
//
//        }
//        RadioButton radSizeDefault1 = (RadioButton) radioGroupSize.findViewById(R.id.rad_size_305);
//        RadioButton radSizeDefault2 = (RadioButton) radioGroupSize.findViewById(R.id.rad_size_330);
//        String site = SharedPref.getInstance(context).getString(Constant.KEY_SITE, context.getString(R.string.us));
//        if (site.equals(context.getString(R.string.us))) {
//            radSizeDefault1.setText(R.string.size_inch_129);
//            radSizeDefault2.setText(R.string.size_inch_119);
//        }
//        if (mapInput != null) {
//            if (mapInput.containsKey("id_hue"))
//                radioHUE[0] = (RadioButton) radioGroupHUE.findViewById(Integer.parseInt(mapInput.get("id_hue")));
//            if (radioHUE[0] != null) {
//
//                radioHUE[0].setChecked(true);
//                radioGroupHUE.setActiveRadioButton(radioHUE[0]);
//            }
//
//            if (mapInput.containsKey("id_material"))
//                radioMaterial[0] = (RadioButton) radioGroupMaterial.findViewById(Integer.parseInt(mapInput.get("id_material")));
//            if (radioMaterial[0] != null) radioMaterial[0].setChecked(true);
//            if (mapInput.containsKey("id_size"))
//                radioSize[0] = (RadioButton) radioGroupSize.findViewById(Integer.parseInt(mapInput.get("id_size")));
//            if (radioSize[0] != null) radioSize[0].setChecked(true);
//
//        }
//
//        CardView btnApply = (CardView) view.findViewById(R.id.ll_apply);
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.cancel();
//            }
//        });
//
//        btnApply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Map<String, String> map = new HashMap<String, String>();
//                radioHUE[0] = (RadioButton) radioGroupHUE.findViewById(radioGroupHUE.getCheckedRadioButtonId());
//                radioMaterial[0] = (RadioButton) radioGroupMaterial.findViewById(radioGroupMaterial.getCheckedRadioButtonId());
//                radioSize[0] = (RadioButton) radioGroupSize.findViewById(radioGroupSize.getCheckedRadioButtonId());
//
//                if (radioHUE[0] != null) {
//                    map.put(Constant.KEY_HUE, dictionary.get(radioHUE[0].getText().toString()));
//                    map.put("id_hue", radioHUE[0].getId() + "");
//                    hasFilter[0] = true;
//                }
//                if (radioSize[0] != null) {
//                    map.put(Constant.KEY_SIZE, radioSize[0].getText().toString());
//                    map.put("id_size", radioSize[0].getId() + "");
//                    hasFilter[0] = true;
//
//                }
//                if (radioMaterial[0] != null) {
//                    map.put(Constant.KEY_MATERIAL, dictionary.get(radioMaterial[0].getText().toString()));
//                    map.put("id_material", radioMaterial[0].getId() + "");
//                    hasFilter[0] = true;
//
//                }
//                map.put("hasFilter", hasFilter[0] + "");
//                mapOnResponse.onResponse(map);
//                dialog.cancel();
//
//            }
//        });
//        tvClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                radioHUE[0] = (RadioButton) radioGroupHUE.findViewById(radioGroupHUE.getCheckedRadioButtonId());
//                radioMaterial[0] = (RadioButton) radioGroupMaterial.findViewById(radioGroupMaterial.getCheckedRadioButtonId());
//                radioSize[0] = (RadioButton) radioGroupSize.findViewById(radioGroupSize.getCheckedRadioButtonId());
//                if (radioHUE[0] != null) {
//                    radioHUE[0].setChecked(false);
//                    radioHUE[0] = null;
//                    radioGroupHUE.getActiveRadioButton().setChecked(false);
//                    radioGroupHUE.setActiveRadioButton(null);
//                    hasFilter[0] = false;
//
//
//                }
//                if (radioMaterial[0] != null) {
//                    radioMaterial[0].setChecked(false);
//                    radioMaterial[0] = null;
//                    radioGroupMaterial.clearCheck();
//                    hasFilter[0] = false;
//
//                }
//                if (radioSize[0] != null) {
//                    radioSize[0].setChecked(false);
//                    radioSize[0] = null;
//                    radioGroupSize.clearCheck();
//                    hasFilter[0] = false;
//
//                }
//            }
//        });
//        dialog.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(view);
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        dialog.setCancelable(true);
//        dialog.getWindow().getAttributes().windowAnimations = R.style_json.DialogAnimation;
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        dialog.setCanceledOnTouchOutside(true);
//
//        return dialog;
//    }
//
//
}
