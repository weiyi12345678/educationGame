package com.example.educationgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;

public class GameMatchActivity extends AppCompatActivity {

    final static int CARD_NUMBER = 3; // as 2 card similar, so total 6
    //final static int TIME_COUNTER = 120000; // 2 mins
    final static int TIME_COUNTER = 60000; // 1 mins

    private ImageView ivClose;
    private Button btnForfeit, btnSGPlay, btnSGBack;
    private TextView tvTitle, tvContent;
    private CountDownTimer countdown;
    private long timeLeftInMillis;

    private TextView tvResultContent;
    private Button btnPlayAgain, btnLeave, btnUpload;

    private TextView tvUploadScore;
    private Button btnUploadDB;
    private EditText etUploadName;
    private boolean uploaded;

    private ImageView ivQuestion;
    private RelativeLayout rl1, rl2, rl3, rl4, rl5;
    private ImageView iv1, iv2, iv3, iv4, iv5;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private TextView tvTimeCounter, tvScore;
    private TimeCounter timeCounter;
    private AlertDialog resultDialog, startDialog, pauseDialog, uploadDialog;

    private String gameTitle, gameContent;
    private long dbSize;
    private int difficulty, score;
    private CustomDialog customDialog;

    private LinkedList<Alphabet> easy, medium, hard, gameItems;
    private Alphabet ansItem;
    private LinkedList<Alphabet> all;

    private int checkAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_match);
        initialize();

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(0);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(1);
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(2);
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(3);
            }
        });

        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(4);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        getMenuInflater().inflate(R.menu.toobar_game, topMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pause:
                pauseGame();
                callPauseDialog();
                break;
        }
        return true;
    }

    private void initialize() {
        itemInitialize();
        gameContentInitialize();
        gameLayoutInitialize(difficulty);
        firebaseInitialize();
        if (score == 0)
            callStartDialog();
    }
    private void itemInitialize() {
        rl1 = findViewById(R.id.match_card_1);
        rl2 = findViewById(R.id.match_card_2);
        rl3 = findViewById(R.id.match_card_3);
        rl4 = findViewById(R.id.match_card_4);
        rl5 = findViewById(R.id.match_card_5);

        iv1 = findViewById(R.id.match_iv_1);
        iv2 = findViewById(R.id.match_iv_2);
        iv3 = findViewById(R.id.match_iv_3);
        iv4 = findViewById(R.id.match_iv_4);
        iv5 = findViewById(R.id.match_iv_5);

        tv1 = findViewById(R.id.match_tv_1);
        tv2 = findViewById(R.id.match_tv_2);
        tv3 = findViewById(R.id.match_tv_3);
        tv4 = findViewById(R.id.match_tv_4);
        tv5 = findViewById(R.id.match_tv_5);

        ivQuestion = findViewById(R.id.match_iv_question);
        tvTimeCounter = findViewById(R.id.game_tv_timecounter);
        tvScore = findViewById(R.id.match_tv_score);
        Intent intentItem = getIntent();
        difficulty = intentItem.getIntExtra("Difficulty", 0);
        customDialog = new CustomDialog(GameMatchActivity.this);
        customDialog.callLoadingDialog();
        all = new LinkedList<>();
        easy = new LinkedList<>();
        medium = new LinkedList<>();
        hard = new LinkedList<>();
        score = 0;
        uploaded = false;
        tvScore.setText("Score: " + score);



    }

    private void gameContentInitialize() {
        String difficultyTxt;
        switch (difficulty) {
            case 1:
                difficultyTxt = "Easy";
                break;
            case 2:
                difficultyTxt = "Medium";
                break;
            case 3:
                difficultyTxt = "Hard";
                break;
            default:
                difficultyTxt = null;
                break;

        }
        gameTitle = "Select the correct word that represent the image!";
        gameContent = "Select correct word to earn score.\nDifficulty: " + difficultyTxt;
    }

    private void gameLayoutInitialize(int difficulty) {
        rl1.setVisibility(View.VISIBLE);
        rl2.setVisibility(View.VISIBLE);
        rl3.setVisibility(View.VISIBLE);
        switch (difficulty) {
            case 1:
                rl4.setVisibility(View.GONE);
                rl5.setVisibility(View.GONE);
                break;
            case 2:
                rl4.setVisibility(View.VISIBLE);
                rl5.setVisibility(View.GONE);
                break;
            case 3:
                rl4.setVisibility(View.VISIBLE);
                rl5.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this, "Difficulty out of bound: " + difficulty, Toast.LENGTH_SHORT).show();
        }
    }

    private void gameItemInitialize(int difficulty) {
        Log.e("Initialize", "Running");
        switch (difficulty) {
            case 1:
                easyGameSetup();
                break;
            case 2:
                mediumGameSetup();
                break;
            case 3:
                hardGameSetup();
                break;
            default:
                Toast.makeText(this, "Difficulty out of bound: " + difficulty, Toast.LENGTH_SHORT).show();
                break;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv1.setImageResource(R.drawable.white_2);
                iv2.setImageResource(R.drawable.white_2);
                iv3.setImageResource(R.drawable.white_2);
                iv4.setImageResource(R.drawable.white_2);
                iv5.setImageResource(R.drawable.white_2);
            }
        }, 1000); // 1s
    }

    private void gameChecking(int id) {

            if (ansItem.getTitle().equals(gameItems.get(id).getTitle())) {
                score++;
                tvScore.setText("Score: " + score);
                gameItemInitialize(difficulty);
            }
            else if (checkAns == 0) {
                iv1.setImageResource(R.drawable.green);
                iv2.setImageResource(R.drawable.red);
                iv3.setImageResource(R.drawable.red);
                iv4.setImageResource(R.drawable.red);
                iv5.setImageResource(R.drawable.red);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameItemInitialize(difficulty);
                    }
                }, 1000); // 1s
            }
            else if(checkAns == 1) {
                iv1.setImageResource(R.drawable.red);
                iv2.setImageResource(R.drawable.green);
                iv3.setImageResource(R.drawable.red);
                iv4.setImageResource(R.drawable.red);
                iv5.setImageResource(R.drawable.red);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageResource(R.drawable.white_2);
                        iv2.setImageResource(R.drawable.white_2);
                        iv3.setImageResource(R.drawable.white_2);
                        iv4.setImageResource(R.drawable.white_2);
                        iv5.setImageResource(R.drawable.white_2);
                        gameItemInitialize(difficulty);
                    }
                }, 1000); // 1s
            }
            else if(checkAns == 2) {
                iv1.setImageResource(R.drawable.red);
                iv2.setImageResource(R.drawable.red);
                iv3.setImageResource(R.drawable.green);
                iv4.setImageResource(R.drawable.red);
                iv5.setImageResource(R.drawable.red);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageResource(R.drawable.white_2);
                        iv2.setImageResource(R.drawable.white_2);
                        iv3.setImageResource(R.drawable.white_2);
                        iv4.setImageResource(R.drawable.white_2);
                        iv5.setImageResource(R.drawable.white_2);
                        gameItemInitialize(difficulty);
                    }
                }, 1000); // 1s
            }
            else if(checkAns == 3) {
                iv1.setImageResource(R.drawable.red);
                iv2.setImageResource(R.drawable.red);
                iv3.setImageResource(R.drawable.red);
                iv4.setImageResource(R.drawable.green);
                iv5.setImageResource(R.drawable.red);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageResource(R.drawable.white_2);
                        iv2.setImageResource(R.drawable.white_2);
                        iv3.setImageResource(R.drawable.white_2);
                        iv4.setImageResource(R.drawable.white_2);
                        iv5.setImageResource(R.drawable.white_2);
                        gameItemInitialize(difficulty);
                    }
                }, 1000); // 1s
            }
            else if(checkAns == 4) {
                iv1.setImageResource(R.drawable.red);
                iv2.setImageResource(R.drawable.red);
                iv3.setImageResource(R.drawable.red);
                iv4.setImageResource(R.drawable.red);
                iv5.setImageResource(R.drawable.green);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageResource(R.drawable.white_2);
                        iv2.setImageResource(R.drawable.white_2);
                        iv3.setImageResource(R.drawable.white_2);
                        iv4.setImageResource(R.drawable.white_2);
                        iv5.setImageResource(R.drawable.white_2);
                        gameItemInitialize(difficulty);
                    }
                }, 1000); // 1s
            }
    }

    private void easyGameSetup() {
        gameItems = new LinkedList<>();
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            int position = rand.nextInt(easy.size());
            gameItems.add(easy.get(position));
            easy.remove(position);
        }

        int position2 = rand.nextInt(gameItems.size());
        ansItem = gameItems.get(position2);

        Picasso.get().load(ansItem.getGameImage()).fit().centerCrop().into(ivQuestion);

        int shuffleCounter = rand.nextInt(10) + 2;

        for (int i = 0; i < shuffleCounter; i++) {
            Collections.shuffle(gameItems);
        }

        for(int i = 0; i < gameItems.size(); i++){
            if(ansItem.getTitle().equals(gameItems.get(i).getTitle()))
            {
                checkAns = i;
            }
        }

        tv1.setText(gameItems.get(0).getTitle());
        tv2.setText(gameItems.get(1).getTitle());
        tv3.setText(gameItems.get(2).getTitle());

        firebaseInitialize();

        customDialog.closeDialog();
    }

    private void mediumGameSetup() {
        gameItems = new LinkedList<>();
        Random rand = new Random();
        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(easy.size());
            gameItems.add(easy.get(position));
            easy.remove(position);
        }

        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(medium.size());
            gameItems.add(medium.get(position));
            medium.remove(position);
        }

        int position2 = rand.nextInt(gameItems.size());
        ansItem = gameItems.get(position2);

        Picasso.get().load(ansItem.getGameImage()).fit().centerCrop().into(ivQuestion);

        int shuffleCounter = rand.nextInt(10) + 2;

        for (int i = 0; i < shuffleCounter; i++) {
            Collections.shuffle(gameItems);
        }

        for(int i = 0; i < gameItems.size(); i++){
            if(gameItems.get(i).getTitle().equals(ansItem.getTitle()))
            {
                checkAns = i;
            }
        }

        tv1.setText(gameItems.get(0).getTitle());
        tv2.setText(gameItems.get(1).getTitle());
        tv3.setText(gameItems.get(2).getTitle());
        tv4.setText(gameItems.get(3).getTitle());

        firebaseInitialize();

        customDialog.closeDialog();
    }

    private void hardGameSetup() {
        gameItems = new LinkedList<>();
        Random rand = new Random();
        for (int i = 0; i < 1; i++) {
            int position = rand.nextInt(easy.size());
            gameItems.add(easy.get(position));
            easy.remove(position);
        }

        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(medium.size());
            gameItems.add(medium.get(position));
            medium.remove(position);
        }

        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(hard.size());
            gameItems.add(hard.get(position));
            hard.remove(position);
        }

        int position2 = rand.nextInt(gameItems.size());
        ansItem = gameItems.get(position2);

        Picasso.get().load(ansItem.getGameImage()).fit().centerCrop().into(ivQuestion);

        int shuffleCounter = rand.nextInt(10) + 2;

        for (int i = 0; i < shuffleCounter; i++) {
            Collections.shuffle(gameItems);
        }

        for(int i = 0; i < gameItems.size(); i++){
            if(gameItems.get(i).getTitle().equals(ansItem.getTitle()))
            {
                checkAns = i;
            }
        }

        tv1.setText(gameItems.get(0).getTitle());
        tv2.setText(gameItems.get(1).getTitle());
        tv3.setText(gameItems.get(2).getTitle());
        tv4.setText(gameItems.get(3).getTitle());
        tv5.setText(gameItems.get(4).getTitle());

        firebaseInitialize();

        customDialog.closeDialog();
    }

    private void callStartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View lView = inflater.inflate(R.layout.start_game_dialog, null);
        builder.setView(lView);

        tvTitle = lView.findViewById(R.id.startgame_tv_title);
        tvContent = lView.findViewById(R.id.startgame_tv_content);
        btnSGPlay = lView.findViewById(R.id.btnSGPlay);
        btnSGBack = lView.findViewById(R.id.btnSGBack);

        tvTitle.setText(gameTitle);
        tvContent.setText(gameContent);
        btnSGPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                startDialog.dismiss();
            }
        });
        btnSGBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameMenuActivity.class));
            }
        });

        startDialog = builder.create();
        startDialog.setCanceledOnTouchOutside(false);
        startDialog.show();
    }

    private void callPauseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View lView = inflater.inflate(R.layout.pause_dialog, null);
        builder.setView(lView);
        ivClose = lView.findViewById(R.id.pause_iv_close);
        btnForfeit = lView.findViewById(R.id.btnPauseForfeit);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueGame();
                pauseDialog.dismiss();
            }
        });
        btnForfeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Forfeit clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), GameMenuActivity.class));
            }
        });
        pauseDialog = builder.create();
        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.show();
    }

    private void callResultDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View lView = inflater.inflate(R.layout.result_dialog, null);
        builder.setView(lView);
        tvResultContent = lView.findViewById(R.id.result_tv_score_content);
        btnPlayAgain = lView.findViewById(R.id.btnResultAgain);
        btnLeave = lView.findViewById(R.id.btnResultBack);
        btnUpload = lView.findViewById(R.id.btnResultUpload);
        tvResultContent.setText("Final Score: " + score);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
                resultDialog.dismiss();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploaded)
                    Toast.makeText(getApplicationContext(), "You uploaded just now!", Toast.LENGTH_SHORT).show();
                else if (score == 0)
                    Toast.makeText(getApplicationContext(), "Your score is too low", Toast.LENGTH_SHORT).show();
                else
                    callUploadDialog();
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameMenuActivity.class));
            }
        });
        resultDialog = builder.create();
        resultDialog.setCanceledOnTouchOutside(false);
        resultDialog.show();
    }

    private void callUploadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflaterNew = getLayoutInflater();
        View view = inflaterNew.inflate(R.layout.upload_score_dialog, null);
        builder.setView(view);
        tvUploadScore = view.findViewById(R.id.upload_score_tv_score);
        ivClose = view.findViewById(R.id.upload_score_iv_close);
        btnUploadDB = view.findViewById(R.id.btn_uploadscore_upload);
        etUploadName = view.findViewById(R.id.upload_score_et_name);
        tvUploadScore.setText("" + score);
        btnUploadDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUploadName.getText().toString();
                if (name.isEmpty() || name.equals(" s")){
                    customDialog.closeDialog();
                    Toast.makeText(getApplicationContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                } else {
                    customDialog.callLoadingDialog();
                    Player player = new Player(name, score);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("Ranking").push().setValue(player).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            customDialog.closeDialog();
                            Toast.makeText(getApplicationContext(), "Uploaded!", Toast.LENGTH_SHORT).show();
                            uploaded = true;
                            uploadDialog.dismiss();
                        }
                    });
                }
            }
        });
        uploadDialog = builder.create();
        uploadDialog.show();
    }

    public void startGame() {
        startTimer();
    }

    public void pauseGame() {
        pauseTimer();
        callPauseDialog();
    }

    public void continueGame() {
        continueTimer();
    }

    private void firebaseInitialize() {
        DatabaseReference alphabetDB = FirebaseDatabase.getInstance().getReference().child("Word");

        alphabetDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dtSnapshot : snapshot.getChildren()) {
                    Alphabet alphabet = dtSnapshot.getValue(Alphabet.class);
                    all.add(alphabet);
                    switch (alphabet.getDifficulty()) {
                        case 1:
                            easy.add(alphabet);
                            break;
                        case 2:
                            medium.add(alphabet);
                            break;
                        case 3:
                            hard.add(alphabet);
                            break;
                        default:
                            Log.e("Difficulty", "Out of bound: " + alphabet.getDifficulty());
                    }
                    dbSize = snapshot.getChildrenCount();
                    checkFinishRetrieving();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Data Error", "Error: " + error.toException());
            }

        });
    }

    private void updateCountDownText() {
        int minutes = (int) timeLeftInMillis / 1000 / 60;
        int seconds = (int) timeLeftInMillis / 1000 % 60;
        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimeCounter.setText(timeLeft);
    }

    private void startTimer(){
        timeLeftInMillis = TIME_COUNTER;
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
                callResultDialog();
            }
        }.start();
    }

    private void continueTimer(){
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
                callResultDialog();
            }
        }.start();
    }

    private void pauseTimer(){
        countdown.cancel();
    }

    private void checkFinishRetrieving() {
        if ((all.size() == dbSize) && (dbSize != 0)) {
            Log.e("Easy", "Size: " + easy.size());
            Log.e("Medium", "Size: " + medium.size());
            Log.e("Hard", "Size: " + hard.size());
            gameItemInitialize(difficulty);
        }
    }
}
