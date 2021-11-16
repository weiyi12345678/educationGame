package com.example.educationgame;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

public class TimeCounter {

    //final static long TIME_COUNTER_MILLIS = 600000; // 10 mins
    //final static long TIME_COUNTER_MILLIS = 60000; // 1mins
    final static long TIME_COUNTER_MILLIS = 120000; // 2 mins

    private CountDownTimer countdown;
    private long timeLeftInMillis;
    private TextView tvTimeCounter;

    TimeCounter(Activity myActivity) {
        timeLeftInMillis = TIME_COUNTER_MILLIS;
        tvTimeCounter = myActivity.findViewById(R.id.game_tv_timecounter);
    }

    TimeCounter(Activity myActivity, long timeLeftInMillis) {
        this.timeLeftInMillis = timeLeftInMillis;
        tvTimeCounter = myActivity.findViewById(R.id.game_tv_timecounter);
    }

    public void startTimer() {
        countdown = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
            }
        }.start();
    }

    public void pauseTimer() {
        countdown.cancel();
    }

    private void updateCountDownText() {
        int minutes = (int) timeLeftInMillis / 1000 / 60;
        int seconds = (int) timeLeftInMillis / 1000 % 60;
        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimeCounter.setText(timeLeft);
    }

    public long getRemainingTime() {
        return timeLeftInMillis;
    }
}
