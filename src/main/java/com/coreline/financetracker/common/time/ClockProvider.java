package com.coreline.financetracker.common.time;

import java.time.Instant;

public interface ClockProvider {

    Instant now();
}
