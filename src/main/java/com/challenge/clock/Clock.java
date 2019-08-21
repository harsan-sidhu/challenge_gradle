package com.challenge.clock;

import java.util.concurrent.TimeUnit;

/**
 * App specific wrapper of System.currentTimeMillis() for testing purposes.
 */
public class Clock {

    /**
     * @return Current time in seconds.
     */
    public long getCurrentTimeInSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}
