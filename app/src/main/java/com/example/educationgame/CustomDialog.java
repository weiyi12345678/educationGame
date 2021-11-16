package com.example.educationgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CustomDialog {

    private Activity activity;
    private AlertDialog dialog;

    //variable for common dialog item
    private ImageView ivClose;

    //variable for difficulty dialog
    private Button btnEasy, btnMedium, btnHard;
    private Intent intentGameType;

    //variable for setting dialog
    private SeekBar sbSound, sbMusic;

    //variable for pause dialog
    private Button btnForfeit;
    private TextView pauseDialogContent;
    private long remainingTime;

    //variable for start game dialog
    private TextView tvTitle, tvContent;
    private Button btnSGPlay, btnSGBack;

    CustomDialog(Activity myActivity) {
        this.activity = myActivity;

    }

    void callLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lview = inflater.inflate(R.layout.loading_dialog, null);
        builder.setView(lview);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    void callSettingDialog() {
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

    void callDifficultyDialog(int gameType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lview = inflater.inflate(R.layout.game_difficulty_dialog, null);
        builder.setView(lview);
        ivClose = lview.findViewById(R.id.difficulty_iv_close);
        btnEasy = lview.findViewById(R.id.btnDifficultyEasy);
        btnMedium = lview.findViewById(R.id.btnDifficultyMedium);
        btnHard = lview.findViewById(R.id.btnDifficultyHard);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        switch (gameType) {
            case 1:
                intentGameType = new Intent(activity, GameMemoryActivity.class);
                break;
            case 2:
                intentGameType = new Intent(activity, GameMatchActivity.class);
                break;
            default:
                intentGameType = null;
                break;

        }
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGameType.putExtra("Difficulty", 1);
                activity.startActivity(intentGameType);
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGameType.putExtra("Difficulty", 2);
                activity.startActivity(intentGameType);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGameType.putExtra("Difficulty", 3);
                activity.startActivity(intentGameType);
            }
        });

        dialog = builder.create();
        dialog.show();
    }


    void closeDialog() {
        dialog.dismiss();
    }
}
