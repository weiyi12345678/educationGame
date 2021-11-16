package com.example.educationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GameMenuActivity extends AppCompatActivity {

    private Button btnMemoryGame, btnFillBlankGame;
    private CustomDialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        itemInitialize();

        btnMemoryGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.callDifficultyDialog(1);
            }
        });

        btnFillBlankGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.callDifficultyDialog(2);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        getMenuInflater().inflate(R.menu.toolbar_base, topMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ranking:
                Toast.makeText(this, "Ranking", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_setting:
                customDialog.callSettingDialog();
                break;
            default:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
        return true;
    }

    private void itemInitialize(){
        btnMemoryGame = findViewById(R.id.btnMemory);
        btnFillBlankGame = findViewById(R.id.btnMatch);
        customDialog = new CustomDialog(GameMenuActivity.this);
    }
}
