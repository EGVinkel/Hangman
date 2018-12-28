package com.vinkel.emil.the_hangmans_game;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

// Fra https://stackoverflow.com/questions/5594877/android-chronometer-pause, nogen har lavet det før, så er der vel
// ikke så meget grund til at gentage arbejdet men dog lavet noget tilføjes i forhold til den funktionalitet jeg ønskede :)


public class PauseableChronometer extends Chronometer {

    private long timeWhenStopped = 0;

    public PauseableChronometer(Context context) {
        super(context);
    }

    public PauseableChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PauseableChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void start() {
        setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        setFormat("%s");
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        timeWhenStopped = SystemClock.elapsedRealtime() - getBase();
    }

    public void reset() {
        stop();
        setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
    }

    public long getCurrentTime() {
        return timeWhenStopped;
    }

    public void setCurrentTime(long time) {
        timeWhenStopped = time;
        setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
    }
}