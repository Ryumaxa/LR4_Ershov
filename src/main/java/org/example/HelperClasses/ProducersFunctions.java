package org.example.HelperClasses;

import java.util.Random;

public class ProducersFunctions {
    static final Double A = 8.1;
    static final Double B1 = 5.8;
    static final Double B2 = 7.2;
    static final Double C0 = -81.355;
    static final Double C1 = 20.922;
    static final Double C2 = -1.358;
    static final Double C3 = 0.025;
    public static final double[] C_ARRAY = {C0, C1, C2, C3};

    public static double ProducerFunc (String name) {
        double resultPower = switch (name) {
            case "HeatPP" -> A;
            case "WindPP" -> generateRandomValue(B1, B2); // ПОЧЕМУ МОЖЕТ УХОДИТЬ В МИНУС ??????????????
            case "SolarPP" -> solarPowerCalculation(TimeTracker.getCurrentHour(), C_ARRAY);
            default -> 0.0;
        };
        return resultPower;
    }


    public static double generateRandomValue(double mean, double deviation) {
        Random random = new Random();
        return random.nextGaussian() * deviation + mean;
    }


    public static double solarPowerCalculation(int hour, double[] c_array) {
        double result = 0.0;
        if (hour >= 6 && hour <= 19) {
            for (int i = 0; i < 4; i++) {
                result += c_array[i] * Math.pow(hour, i);
            }
        }
        return result;
    }


}
