package com.example.final1_6206021611125;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GraphicsView extends View implements View.OnTouchListener {
    private Paint p;

    private int score, time;
    private int Width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int Height = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int[] FRUITS_IMAGES = {
            R.drawable.apple,
            R.drawable.apricot,
            R.drawable.banana,
            R.drawable.cherry,
            R.drawable.mango,
            R.drawable.pear,
            R.drawable.strawberry,
            R.drawable.watermalon
    };
    private int[] IMAGE_WIDTH = new int[8];
    private int[] IMAGE_HEIGHT = new int[8];
    private int[] SPEED = new int[8];
    private int gun;

    private float[] X = new float[8];
    private float[] Y = new float[8];

    private boolean finish = false;
    private boolean[] HIT = new boolean[8];

    private Random random = new Random();
    private CountDownTimer timer1, timer2;
    private SoundPool soundPool;
    private Bitmap[] FRUITS = new Bitmap[8];

    public GraphicsView(Context context) {
        super(context);
        setBackgroundColor(Color.rgb(255, 204, 204));
        setOnTouchListener(this);
        p = new Paint();
        score = 0;
        time = 0;
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        gun = soundPool.load(context, R.raw.gun2, 1);

        for (int i = 0; i < 8; i++) {
            FRUITS[i] = BitmapFactory.decodeResource(getResources(), FRUITS_IMAGES[i]);
            IMAGE_WIDTH[i] = FRUITS[i].getWidth();
            IMAGE_HEIGHT[i] = FRUITS[i].getHeight();
            X[i] = random.nextInt(Width - IMAGE_WIDTH[i]);
            SPEED[i] = random.nextInt(10) + 10;
        }

        timer1 = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time++;
                invalidate();
            }

            @Override
            public void onFinish() {
                finish = true;
                invalidate();
            }
        };

        timer2 = new CountDownTimer(30000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                for (int i = 0; i < 8; i++) {
                    Y[i] += SPEED[i];
                    if (Y[i] > Height - IMAGE_HEIGHT[i]) {
                        Y[i] = 0 - IMAGE_HEIGHT[i];
                        X[i] = random.nextInt(Width - IMAGE_WIDTH[i]);
                    }
                }
                invalidate();
            }

            @Override
            public void onFinish() {
                finish = true;
                invalidate();
            }
        };

        timer1.start();
        timer2.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (finish) {
            finish = false;
            time = 0;
            timer1.start();
            timer2.start();
            score = 0;
            invalidate();
        } else {
            float x = event.getX();
            float y = event.getY();

            for (int i = 0; i < 8; i++) {
                if (x > X[i] && x < X[i] + IMAGE_WIDTH[i]) {
                    if (y > Y[i] && y < Y[i] + IMAGE_HEIGHT[i]) {
                        score++;
                        soundPool.play(gun, 1, 1, 1, 0, 1);
                        HIT[i] = true;
                        invalidate();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (finish) {
            p.setColor(Color.argb(255, 255, 0, 255));
            p.setTextSize(70);
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("E N D  G A M E.", Width / 2, Height / 2 - 500, p);
            canvas.drawText("Your Score : " + score, Width / 2, Height / 2 - 200, p);
            canvas.drawText("Touch for Play Game" , Width / 2 , Height / 2 , p);
        } else {
            p.setColor(Color.BLUE);
            p.setTextSize(70);
            p.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Score : " + score, 20, 70, p);
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Time : " + time, Width - 20, 70, p);
            for (int i = 0; i < 8; i++) {
                if (HIT[i]) {
                    Y[i] = 0 - IMAGE_HEIGHT[i];
                    X[i] = random.nextInt(Width - IMAGE_WIDTH[i]);
                    HIT[i] = false;
                }
                canvas.drawBitmap(FRUITS[i], X[i], Y[i], null);
            }
        }
    }
}