package org.example.HelperClasses;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeTracker {
    private static LocalDateTime startTime;

    public static void StartTime() {
        startTime = LocalDateTime.now();
    }

    public static long getMillsUntilNextHour () { // ЗАМЕНИТЬ 1000 на 48!
        LocalDateTime currentTime = LocalDateTime.now();
        long millisecondsPassed = startTime.until(currentTime, ChronoUnit.MILLIS);
        long hour = millisecondsPassed * 1000 / 3600000;
        long millisecondsUntil = (hour + 1) * 3600000 / 1000 - millisecondsPassed;
        return millisecondsUntil;
    }

    // ДОБАВИТЬ СБРОС ВРЕМЕНИ ПРИ ДОСТИЖЕНИИ 24 ЧАСОВ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public static int getCurrentHour() { // ЗАМЕНИТЬ 1000 на 48!
        LocalDateTime currentTime = LocalDateTime.now();
        long millisecondsPassed = startTime.until(currentTime, ChronoUnit.MILLIS);
        long long_hour = millisecondsPassed * 1000 / 3600000 + 1; // Пока что ускорено в 1000 раз (нужно в 48)
        if (long_hour > 24) {
            long_hour = long_hour - (long_hour/24)*24; // Не допускает превышение 24-часового формата
        }
        return (int) long_hour;
    }

}
