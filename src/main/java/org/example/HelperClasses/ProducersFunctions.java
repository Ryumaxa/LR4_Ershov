package org.example.HelperClasses;

import java.util.HashMap;
import java.util.Random;

public class ProducersFunctions {
    private static HashMap<Integer, Double> WindPPowerGraphic = new HashMap<>();
    public static void CountPowerOfWindPP () {
        for (int i = 1; i <= 24; i++) {
            Random random = new Random();
            WindPPowerGraphic.put(i, random.nextGaussian() * B2 + B1);
        }
    }
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
            case "HeatPP" -> A * 2;
            case "WindPP" -> generateRandomValue() * 2;
            case "SolarPP" -> solarPowerCalculation(TimeTracker.getCurrentHour(), C_ARRAY) * 2;
            default -> 0.0;
        };
        return resultPower;
    }


    public static double generateRandomValue() {
        return WindPPowerGraphic.get(TimeTracker.getCurrentHour());
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

    public static double getPPminPrice (String name) {
        double minPrice = switch (name) {
            case "HeatPP" -> 2.4;
            case "WindPP" -> generateWindPPPrice();
            case "SolarPP" -> generateSolarPPPrice();
            default -> 100;
        };
        return minPrice;
    }
    public static double generateWindPPPrice () {
        double minPrice;
        if (generateRandomValue() >= B1) {
            minPrice = 2.5;
        } else {
            minPrice = 2.8;
        }
        return minPrice;
    }

    public static double generateSolarPPPrice () {
        return 2 + (1/solarPowerCalculation(TimeTracker.getCurrentHour(), C_ARRAY));
    }



}
