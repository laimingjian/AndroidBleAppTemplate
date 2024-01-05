package com.lenzetech.blelib.ble;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

/**
 * Created on 2019/5/24.
 *
 * @author WAMsAI (WAMsAI1096@gmail.com)
 */
@SuppressWarnings({"WeakerAccess"})
public class CycleHandler extends Handler {

    private final CycleHandlerCallback mCycleHandlerCallback;

    public CycleHandler(@NonNull CycleHandlerCallback cycleCallback) {
        this(DEFAULT_WORK_DURATION, DEFAULT_SLEEP_DURATION, cycleCallback);
    }

    public CycleHandler(int workDuration,
                        int sleepDuration,
                        @NonNull CycleHandlerCallback cycleCallback) {
        super();
        mCycleHandlerCallback = cycleCallback;
        mWorkDuration = workDuration;
        mSleepDuration = sleepDuration;
    }

    private static final int MSG_WHAT_WORK = 2;
    private static final int MSG_WHAT_SLEEP = 4;

    private static final int DEFAULT_WORK_DURATION = 1000 * 60;
    private static final int DEFAULT_SLEEP_DURATION = 1000 * 5;

    private int mWorkDuration = DEFAULT_WORK_DURATION;
    private int mSleepDuration = DEFAULT_SLEEP_DURATION;

    @Override
    public final void handleMessage(Message msg) {
        if (msg.what == MSG_WHAT_WORK) {
            mCycleHandlerCallback.onWorkDuration();
            cycleRun();
        } else if (msg.what == MSG_WHAT_SLEEP) {
            mCycleHandlerCallback.onSleepDuration();
            removeMessages(MSG_WHAT_WORK);
            sendEmptyMessageDelayed(MSG_WHAT_WORK, getSleepDuration());
        }
    }

    public int getWorkDuration() {
        return mWorkDuration;
    }

    public int getSleepDuration() {
        return mSleepDuration;
    }

    public void cycleStart() {
        sendEmptyMessage(MSG_WHAT_WORK);
    }

    public void cycleEnd() {
        post(mCycleHandlerCallback::onSleepDuration);
        removeMessages(MSG_WHAT_WORK);
        removeMessages(MSG_WHAT_SLEEP);
    }

    private void cycleRun() {
        removeMessages(MSG_WHAT_SLEEP);
        sendEmptyMessageDelayed(MSG_WHAT_SLEEP, getWorkDuration());
    }

    public interface CycleHandlerCallback {
        void onWorkDuration();

        void onSleepDuration();
    }
}
