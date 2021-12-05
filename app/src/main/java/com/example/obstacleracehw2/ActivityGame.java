package com.example.obstacleracehw2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.util.Random;

public class ActivityGame extends AppCompatActivity {
    private final int NUM_OF_LANES = 5;
    private boolean sensorXControl = true;
    private boolean sensorYSpeedUpControl = false;
    private static int currentRockAnimationPosition;
    private static int currentCoinsAnimationPosition;
    private final int NORMAL_SPEED = 3000;
    private final int FAST_SPEED = 1600;
    private float allLanes[];
    private static short previousRandomRock;
    private static short previousRandomCoin;
    public static final String TYPE_OF_GAME = "typeOfGame";
    private MaterialTextView game_TXT_score;
    private MaterialTextView game_TXT_distance;
    private ImageView game_IMG_car;
    private ImageView game_IMG_3lives;
    private ImageView game_IMG_2lives;
    private ImageView game_IMG_1lives;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private ImageView game_IMG_5thLaneRock;
    private ImageView game_IMG_4thLaneRock;
    private ImageView game_IMG_3rdLaneRock;
    private ImageView game_IMG_2ndLaneRock;
    private ImageView game_IMG_1stLaneRock;
    private ImageView game_IMG_1stLaneCoins;
    private ImageView game_IMG_2ndLaneCoins;
    private ImageView game_IMG_3rdLaneCoins;
    private ImageView game_IMG_4thLaneCoins;
    private ImageView game_IMG_5thLaneCoins;
    private Handler handler = new Handler();
    private ObjectAnimator allRocksAnimations[];
    private ObjectAnimator allCoinsAnimations[];
    private ImageView allRocks[];
    private ImageView allCoins[];
    private float allRocksYPositions[];
    private float allCoinsYPositions[];
    private short numOfLives = 3;
    private short sensorRegistered = 0;
    private MediaPlayer coinSound;
    private MediaPlayer crashSound;
    private MediaPlayer carSound;
    private MediaPlayer turnSound;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            final Handler handler = new Handler();
            float x = event.values[0];
            float y = event.values[1];

            if (sensorXControl)
                sensorXTurn(x);

            if (y < 6) {
                sensorYSpeedUpControl = true;
                changeSpeed(FAST_SPEED);
            } else if (y > 6) {
                sensorYSpeedUpControl = false;
                changeSpeed(NORMAL_SPEED);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        randARock();
        randACoins();
        if (intent.getStringExtra(TYPE_OF_GAME).equals("sensors")) {
            game_BTN_left.setVisibility(View.GONE);
            game_BTN_right.setVisibility(View.GONE);
            initSensor();
        } else {
            buttonTurn();

        }
        updateDistance();

    }

    private void initViews() {
        game_IMG_car = findViewById(R.id.game_IMG_car);
        game_IMG_3lives = findViewById(R.id.game_IMG_3lives);
        game_IMG_2lives = findViewById(R.id.game_IMG_2lives);
        game_IMG_1lives = findViewById(R.id.game_IMG_1lives);
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_IMG_5thLaneRock = findViewById(R.id.game_IMG_5thLaneRock);
        game_IMG_4thLaneRock = findViewById(R.id.game_IMG_4thLaneRock);
        game_IMG_3rdLaneRock = findViewById(R.id.game_IMG_3rdLaneRock);
        game_IMG_2ndLaneRock = findViewById(R.id.game_IMG_2ndLaneRock);
        game_IMG_1stLaneRock = findViewById(R.id.game_IMG_1stLaneRock);
        game_IMG_1stLaneCoins = findViewById(R.id.game_IMG_1stLaneCoins);
        game_IMG_2ndLaneCoins = findViewById(R.id.game_IMG_2ndLaneCoins);
        game_IMG_3rdLaneCoins = findViewById(R.id.game_IMG_3rdLaneCoins);
        game_IMG_4thLaneCoins = findViewById(R.id.game_IMG_4thLaneCoins);
        game_IMG_5thLaneCoins = findViewById(R.id.game_IMG_5thLaneCoins);
        game_TXT_distance = findViewById(R.id.game_TXT_distance);
        game_TXT_score = findViewById(R.id.game_TXT_score);
        game_IMG_5thLaneRock.setVisibility(View.INVISIBLE);
        game_IMG_4thLaneRock.setVisibility(View.INVISIBLE);
        game_IMG_3rdLaneRock.setVisibility(View.INVISIBLE);
        game_IMG_2ndLaneRock.setVisibility(View.INVISIBLE);
        game_IMG_1stLaneRock.setVisibility(View.INVISIBLE);
        game_IMG_1stLaneCoins.setVisibility(View.INVISIBLE);
        game_IMG_2ndLaneCoins.setVisibility(View.INVISIBLE);
        game_IMG_3rdLaneCoins.setVisibility(View.INVISIBLE);
        game_IMG_4thLaneCoins.setVisibility(View.INVISIBLE);
        game_IMG_5thLaneCoins.setVisibility(View.INVISIBLE);
        coinSound = MediaPlayer.create(ActivityGame.this, R.raw.coin_sound);
        crashSound = MediaPlayer.create(ActivityGame.this, R.raw.crash_sound);
        carSound = MediaPlayer.create(ActivityGame.this, R.raw.car_accelerating);
        turnSound = MediaPlayer.create(ActivityGame.this, R.raw.car_tires);
        initRocksAndCoins();
        rocksAndCoinsToAnimation();
    }

    private void initRocksAndCoins() {
        allLanes = new float[NUM_OF_LANES];
        allRocks = new ImageView[NUM_OF_LANES];
        allCoins = new ImageView[NUM_OF_LANES];
        allRocks[0] = game_IMG_1stLaneRock;
        allRocks[1] = game_IMG_2ndLaneRock;
        allRocks[2] = game_IMG_3rdLaneRock;
        allRocks[3] = game_IMG_4thLaneRock;
        allRocks[4] = game_IMG_5thLaneRock;
        allCoins[0] = game_IMG_1stLaneCoins;
        allCoins[1] = game_IMG_2ndLaneCoins;
        allCoins[2] = game_IMG_3rdLaneCoins;
        allCoins[3] = game_IMG_4thLaneCoins;
        allCoins[4] = game_IMG_5thLaneCoins;
        allRocks[0].setX(20);
        allCoins[0].setX(90);
        allLanes[0] = 20;
        for (int i = 1; i < NUM_OF_LANES; i++) {
            allLanes[i] = allLanes[i - 1] + 285;
            allRocks[i].setX(allRocks[i - 1].getX() + 285);
            allCoins[i].setX((allCoins[i - 1]).getX() + 285);
        }

        //set car to the middle of the screen
        game_IMG_car.setX(allLanes[2]);


    }

    private void rocksAndCoinsToAnimation() {
        allRocksAnimations = new ObjectAnimator[NUM_OF_LANES];
        allCoinsAnimations = new ObjectAnimator[NUM_OF_LANES];

        for (int i = 0; i < NUM_OF_LANES; i++) {
            allRocksAnimations[i] = ObjectAnimator.ofFloat(allRocks[i], "y", 2500);
            allRocksAnimations[i].setDuration(NORMAL_SPEED);
            allCoinsAnimations[i] = ObjectAnimator.ofFloat(allCoins[i], "y", 2500);
            allCoinsAnimations[i].setDuration(NORMAL_SPEED);
        }
        addUpdateListenersToRockAnimations();
        addUpdateListenersToCoinsAnimations();
    }

    //for loop doesn't work
    private void addUpdateListenersToCoinsAnimations() {
        allCoinsYPositions = new float[NUM_OF_LANES];

        allCoinsAnimations[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allCoinsYPositions[0] = (Float) animation.getAnimatedValue();
                checkCrashWithCoins(allCoinsYPositions[0], allCoins[0], allCoinsAnimations[0]);
            }
        });
        allCoinsAnimations[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allCoinsYPositions[1] = (Float) animation.getAnimatedValue();
                checkCrashWithCoins(allCoinsYPositions[1], allCoins[1], allCoinsAnimations[1]);
            }
        });
        allCoinsAnimations[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allCoinsYPositions[2] = (Float) animation.getAnimatedValue();
                checkCrashWithCoins(allCoinsYPositions[2], allCoins[2], allCoinsAnimations[2]);
            }
        });
        allCoinsAnimations[3].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allCoinsYPositions[3] = (Float) animation.getAnimatedValue();
                checkCrashWithCoins(allCoinsYPositions[3], allCoins[3], allCoinsAnimations[3]);
            }
        });
        allCoinsAnimations[4].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allCoinsYPositions[4] = (Float) animation.getAnimatedValue();
                checkCrashWithCoins(allCoinsYPositions[4], allCoins[4], allCoinsAnimations[4]);
            }
        });
    }

    //    //for loop doesn't work
    private void addUpdateListenersToRockAnimations() {
        allRocksYPositions = new float[NUM_OF_LANES];

        allRocksAnimations[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allRocksYPositions[0] = (Float) animation.getAnimatedValue();
                checkCrashWithRock(allRocksYPositions[0], allRocks[0], allRocksAnimations[0]);
            }
        });
        allRocksAnimations[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allRocksYPositions[1] = (Float) animation.getAnimatedValue();
                checkCrashWithRock(allRocksYPositions[1], allRocks[1], allRocksAnimations[1]);
            }
        });
        allRocksAnimations[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allRocksYPositions[2] = (Float) animation.getAnimatedValue();
                checkCrashWithRock(allRocksYPositions[2], allRocks[2], allRocksAnimations[2]);
            }
        });
        allRocksAnimations[3].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allRocksYPositions[3] = (Float) animation.getAnimatedValue();
                checkCrashWithRock(allRocksYPositions[3], allRocks[3], allRocksAnimations[3]);
            }
        });
        allRocksAnimations[4].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                allRocksYPositions[4] = (Float) animation.getAnimatedValue();
                checkCrashWithRock(allRocksYPositions[4], allRocks[4], allRocksAnimations[4]);
            }
        });

    }


    private void checkCrashWithCoins(float objectYPosition, ImageView element, ObjectAnimator animation) {
        if ((game_IMG_car.getY() >= objectYPosition - 200 && game_IMG_car.getY() <= objectYPosition + 40) && (element.getX() - 70 == game_IMG_car.getX())) {
            coinSound.start();
            toast("+100");
            element.setVisibility(View.INVISIBLE);
            animation.end();
            updateScore();
        }
        if (objectYPosition > 2480)
            element.setVisibility(View.INVISIBLE);
    }


    private void checkCrashWithRock(Float objectYPosition, ImageView element, ObjectAnimator animation) {
        if ((game_IMG_car.getY() >= objectYPosition - 200 && game_IMG_car.getY() <= objectYPosition + 40) && (element.getX() == game_IMG_car.getX())) {
            crashSound.start();
            animation.end();
            element.setVisibility(View.INVISIBLE);
            carAppearAndDisappear();
            vibrate();
            if (numOfLives == 3) {
                toast("Crash! 2 lives left");
                numOfLives--;
                game_IMG_3lives.setVisibility(View.GONE);
            } else if (numOfLives == 2) {
                toast("Crash! 1 life left");
                numOfLives--;
                game_IMG_2lives.setVisibility((View.GONE));
            } else if (numOfLives == 1) {
                toast("Crash! no more lives left");
                numOfLives--;
                game_IMG_1lives.setVisibility((View.GONE));
            } else {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(ActivityGame.this, ActivityGetName.class);
                bundle.putInt("Score", getScore());
                bundle.putFloat("Distance", getDistance());
                intent.putExtra("Bundle", bundle);
                toast("Crash! Game Over!");
                finish();
                startActivity(intent);
                System.exit(0);

            }

        }
        if (objectYPosition > 2480)
            element.setVisibility(View.INVISIBLE);
        carSound.start();
    }


    private void updateDistance() {
        final Handler handler = new Handler();
        DecimalFormat df = new DecimalFormat("#.#");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String str = game_TXT_distance.getText().toString();
                str = str.substring(0, str.length() - 2);
                game_TXT_distance.setText(String.valueOf(df.format(Float.parseFloat(str) + 0.1)) + "km");
                if (sensorYSpeedUpControl)
                    handler.postDelayed(this, FAST_SPEED);
                else
                    handler.postDelayed(this, NORMAL_SPEED);
            }
        };
        handler.post(runnable);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (sensorRegistered == 1)
            sensorManager.unregisterListener(accSensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorRegistered == 1)
            sensorManager.registerListener(accSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorRegistered = 1;
    }

    public boolean isSensorExists(int sensorType) {
        return (sensorManager.getDefaultSensor(sensorType) != null);
    }

    private void randACoins() {
        final Handler handler = new Handler();
        final Random random = new Random();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int randomCoin = random.nextInt(5);
                while (previousRandomCoin == randomCoin) {
                    randomCoin = random.nextInt(5);
                }
                previousRandomCoin = (short) randomCoin;
                allCoins[randomCoin].setVisibility(View.VISIBLE);
                allCoinsAnimations[randomCoin].start();
                currentCoinsAnimationPosition = randomCoin;
                if (sensorYSpeedUpControl)
                    handler.postDelayed(this, FAST_SPEED - 700);
                else
                    handler.postDelayed(this, NORMAL_SPEED - 1000);

            }
        };
        handler.post(runnable);
    }

    private void randARock() {
        final Handler handler = new Handler();
        final Random random = new Random();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int randomRock = random.nextInt(5);
                while (previousRandomRock == randomRock) {
                    randomRock = random.nextInt(5);
                }
                previousRandomRock = (short) randomRock;
                allRocks[randomRock].setVisibility(View.VISIBLE);
                allRocksAnimations[randomRock].start();
                currentRockAnimationPosition = randomRock;

                if (sensorYSpeedUpControl)
                    handler.postDelayed(this, FAST_SPEED - 700);
                else
                    handler.postDelayed(this, NORMAL_SPEED - 1000);
            }
        };
        handler.post(runnable);
    }


    private void buttonTurn() {

        game_BTN_left.setOnClickListener(v -> {
            if (game_IMG_car.getX() != allLanes[0]) {
                turnSound.start();
                game_IMG_car.setRotation(-45);
                game_IMG_car.setX(game_IMG_car.getX() - 285);
                myWait();
            }
        });

        game_BTN_right.setOnClickListener(v -> {
            if (game_IMG_car.getX() != allLanes[4]) {
                turnSound.start();
                game_IMG_car.setRotation(45);
                game_IMG_car.setX(game_IMG_car.getX() + 285);
                myWait();
            }
        });

    }

    private void sensorXTurn(float x) {
        sensorXControl = false;
        final Handler handler = new Handler();
        if (x > 1) {
            if (game_IMG_car.getX() != allLanes[0]) {
                turnSound.start();
                game_IMG_car.setRotation(-45);
                game_IMG_car.setX(game_IMG_car.getX() - 285);
                myWait();
            }
        }
        else if (x < -1) {
            if (game_IMG_car.getX() != allLanes[4]) {
                turnSound.start();
                game_IMG_car.setRotation(45);
                game_IMG_car.setX(game_IMG_car.getX() + 285);
                myWait();
            }
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sensorXControl = true;
                handler.postDelayed(this, 800);
            }
        };
        handler.post(runnable);
    }


    //wait 80 mls before setting the car rotation to normal
    private void myWait() {
        handler.postDelayed(new Runnable() {
            public void run() {
                game_IMG_car.setRotation(0);
            }
        }, 80);
    }


    private float getDistance() {
        String str = game_TXT_distance.getText().toString();
        str = str.substring(0, str.length() - 2);
        return Float.parseFloat(str);
    }

    private int getScore() {
        String str = game_TXT_score.getText().toString();
        str = str.substring(0, str.length() - 1);
        return Integer.parseInt(str);
    }

    private void carAppearAndDisappear() {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setRepeatCount(3);
        game_IMG_car.startAnimation(animation);
    }


    private void vibrate() {
        // get the VIBRATOR_SERVICE system service
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        final VibrationEffect vibrationEffect1;

        // this effect creates the vibration of default amplitude for 1000ms(1 sec)
        vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.cancel();
        vibrator.vibrate(vibrationEffect1);
    }

    private void toast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        System.exit(0);

    }

    private void changeSpeed(int speed) {
        if ((getObjectYPosition(allRocksYPositions, currentRockAnimationPosition) > 2350) && (getObjectYPosition(allCoinsYPositions, currentCoinsAnimationPosition) > 2350)) {
            for (int i = 0; i < NUM_OF_LANES; i++) {
                allRocksAnimations[i].setDuration(speed);
                allCoinsAnimations[i].setDuration(speed);
            }
        }
    }

    private float getObjectYPosition(float allObjectsYPositions[], int index) {
        return allObjectsYPositions[index];
    }

    private void updateScore() {
        String str = game_TXT_score.getText().toString();
        str = str.substring(0, str.length() - 1);
        game_TXT_score.setText(String.valueOf(Integer.parseInt(str) + 100) + "$");
    }


}