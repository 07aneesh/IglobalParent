package com.cts.cheetah.helpers;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.cts.cheetah.interfaces.ITimer;

/**
 * Created by manu.palassery on 14-06-2017.
 */

public class TimerHelper {

    static TimerHelper timerHelperInstance;
    Context context;

    long timeInMilliseconds = 0L;
    long updatedTime = 0L;
    long startTime = 0L;
    String timeString = "";
    CountDownTimer countDownTimer=null;
    TextView countDownTextView;
    TextView countUpTextView;

    public TimerHelper(Context context){
        this.context = context;
    }

    public static synchronized TimerHelper getInstance(Context context) {
        if (timerHelperInstance == null) {
            timerHelperInstance = new TimerHelper(context);
        }
        return timerHelperInstance;
    }

    public void setTextView(TextView tv){
        countUpTextView = tv;
    }

    /**
     *
     * @param tv - TextView to show the time.
     * @param duration - should be in milliseconds.
     */
    public void showCountDownTimer(final TextView tv ,long duration,ITimer iTimer){
        if(duration > 0) {
            countDownTextView = tv;
            if(countDownTimer == null) {
                StartCountDownTimer(duration,iTimer);
            }
        }
    }

    private void StartCountDownTimer(long duration, final ITimer iTimer){
        countDownTimer = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (time < 10) {
                    timeString = "0" + time;
                } else {
                    timeString = "" + time;
                }
                if(countDownTextView != null) {
                    countDownTextView.setText("00:" + timeString + " mins");
                }
                Log.i("TIMER: ",timeString);
            }

            public void onFinish() {
                if(countDownTextView != null) {
                    countDownTextView.setText("00:00 mins");
                }
                countDownTimer = null;
                iTimer.onTimerFinish(context);
            }
        }.start();
    }

    //----------------------------------------------------------------------------------------------
    public void showCountUpTimer(){
        final Handler customHandler = new Handler();
        final long timeSwapBuff = 0L;

        Runnable updateTimerThread = new Runnable() {
            public void run() {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updatedTime / 1000);
                int hrs = secs /(60*60);
                int mins = secs / 60;
                if(mins > 60){
                    mins = mins/60;
                }
                secs = secs % 60;
                int milliseconds = (int) (updatedTime % 1000);
                if(countUpTextView != null) {
                    countUpTextView.setText(String.format("%02d", hrs) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
                }
                customHandler.postDelayed(this, 0);
            }
        };

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }
    //----------------------------------------------------------------------------------------------

    //TRIP REQUEST TIMER ........................
    /**
     *
     * @param tv - TextView to show the time.
     * @param duration - should be in milliseconds.
     */
    TextView tripRequstTimerTextView;
    String trTimeString = "";
    CountDownTimer trCountDownTimer=null;

    public void showTripRequestTimer(final TextView tv ,long duration,ITimer iTimer){
        if(duration > 0) {
            tripRequstTimerTextView = tv;
            if(trCountDownTimer == null) {
                StartTripRequstTimer(duration,iTimer);
            }
        }
    }

    private void StartTripRequstTimer(long duration, final ITimer iTimer){
        trCountDownTimer = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (time < 10) {
                    trTimeString = "0" + time;
                } else {
                    trTimeString = "" + time;
                }
                if(tripRequstTimerTextView != null) {
                    tripRequstTimerTextView.setText("00:" + trTimeString + " mins");
                }
                Log.i("TIMER: ",trTimeString);
            }

            public void onFinish() {
                if(tripRequstTimerTextView != null) {
                    tripRequstTimerTextView.setText("00:00 mins");
                }
                trCountDownTimer = null;
                iTimer.onTimerFinish(context);
            }
        }.start();
    }

    //...........................................
}
