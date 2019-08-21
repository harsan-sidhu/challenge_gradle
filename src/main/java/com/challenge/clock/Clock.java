package com.challenge.clock;

import java.util.concurrent.TimeUnit;

public class Clock {

    public long getCurrentTimeInSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}
