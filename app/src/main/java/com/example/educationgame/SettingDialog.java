package com.example.educationgame;


import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

class SettingDialog {

    private Activity activity;
    private AlertDialog dialog;

    //variable for common item in dialog
    private ImageView ivClose;
    private SeekBar sbSound, sbMusic;

    SettingDialog(Activity myActivity) {
        activity = myActivity;
    }

    void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lview = inflater.inflate(R.layout.setting_dialog, null);
        builder.setView(lview);
        ivClose = lview.findViewById(R.id.setting_iv_close);
        sbSound = lview.findViewById(R.id.sound_seekbar);
        sbMusic = lview.findViewById(R.id.music_seekbar);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    void closeDialog() {
        dialog.dismiss();
    }
}