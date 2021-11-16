package com.example.educationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private Button btnLearn, btnPlay, btnQuit;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemInitialize();
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Learn ", Toast.LENGTH_SHORT).show();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameMenuActivity.class));
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu topMenu){
        getMenuInflater().inflate(R.menu.toolbar_base, topMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_ranking:
                Toast.makeText(this, "Ranking", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_setting:
                customDialog.callSettingDialog();
                break;
        }

        return true;
    }
    private void itemInitialize() {
        btnLearn = findViewById(R.id.main_btnLearn);
        btnPlay = findViewById(R.id.main_btnPlay);
        btnQuit = findViewById(R.id.main_btnQuit);
        customDialog = new CustomDialog(MainActivity.this);
    }

}


