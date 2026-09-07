package com.example.mystats.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class TimerViewModel extends AndroidViewModel {
    private final MutableLiveData<Integer> currentTime;
    private final MutableLiveData<Boolean> isRunning;
    private Thread timerThread;
    private boolean shouldContinue;

    public TimerViewModel(@NonNull Application application) {
        super(application);
        currentTime = new MutableLiveData<>(0);
        isRunning = new MutableLiveData<>(false);
    }

    public void startTimer(int seconds) {
        shouldContinue = true;
        isRunning.setValue(true);

        timerThread = new Thread(() -> {
            int remainingTime = seconds;
            while (remainingTime >= 0 && shouldContinue) {
                final int time = remainingTime;
                currentTime.postValue(time);

                if (remainingTime == 0) {
                    isRunning.postValue(false);
                    break;
                }

                try {
                    Thread.sleep(1000);
                    remainingTime--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timerThread.start();
    }

    public void pauseTimer() {
        shouldContinue = false;
        isRunning.setValue(false);
    }

    public void resetTimer() {
        shouldContinue = false;
        currentTime.setValue(0);
        isRunning.setValue(false);
    }

    public void addTime(int seconds) {
        Integer current = currentTime.getValue();
        if (current != null) {
            currentTime.setValue(current + seconds);
        }
    }

    public MutableLiveData<Integer> getCurrentTime() {
        return currentTime;
    }

    public MutableLiveData<Boolean> getIsRunning() {
        return isRunning;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        shouldContinue = false;
    }
}