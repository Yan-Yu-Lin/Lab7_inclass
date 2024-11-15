package com.example.lab7_inclass;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekBarRabbit, seekBarTurtle;
    private Button btnStart;
    private boolean hasWinner = false;
    private int finishCount = 0;  // 用來追蹤完成比賽的選手數量

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                seekBarRabbit.setProgress(msg.arg1);
                if (msg.arg1 >= 100) {
                    if (!hasWinner) {
                        hasWinner = true;
                        Toast.makeText(MainActivity.this, "兔子勝利！", Toast.LENGTH_SHORT).show();
                    }
                    finishCount++;
                    checkRaceFinish();
                }
            } else if (msg.what == 2) {
                seekBarTurtle.setProgress(msg.arg1);
                if (msg.arg1 >= 100) {
                    if (!hasWinner) {
                        hasWinner = true;
                        Toast.makeText(MainActivity.this, "烏龜勝利！", Toast.LENGTH_SHORT).show();
                    }
                    finishCount++;
                    checkRaceFinish();
                }
            }
            return true;
        }
    });

    private void checkRaceFinish() {
        if (finishCount >= 2) {  // 兩個選手都完成比賽
            btnStart.setEnabled(true);  // 重新啟用按鈕
            finishCount = 0;  // 重置計數器
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBarRabbit = findViewById(R.id.seekBarRabbit);
        seekBarTurtle = findViewById(R.id.seekBarTurtle);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {
            hasWinner = false;
            finishCount = 0;
            seekBarRabbit.setProgress(0);
            seekBarTurtle.setProgress(0);
            btnStart.setEnabled(false);  // 禁用按鈕
            runRabbit();
            runTurtle();
        });
    }

    private void runRabbit() {
        new Thread(() -> {
            int progress = 0;
            while (progress < 100) {
                try {
                    Thread.sleep((long) (Math.random() * 750));
                    progress += 4;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = progress;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void runTurtle() {
        new Thread(() -> {
            int progress = 0;
            while (progress < 100) {
                try {
                    Thread.sleep(100);
                    progress += 1;
                    Message msg = new Message();
                    msg.what = 2;
                    msg.arg1 = progress;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
