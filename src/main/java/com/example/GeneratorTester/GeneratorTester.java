package com.example.GeneratorTester;

import java.util.*;

import com.example.ContinuousGenerator;
import com.example.DiscreteGenerator;

public class GeneratorTester {
    private static final int SAMPLE_SIZE = 1000000;
    private static final int INTERVAL_COUNT = 5;
    
    public static void testContinuousGenerator(Long seed) {
        List<double[]> intervals = generateContinuousIntervals();
        List<Double> probabilities = generateProbabilities(INTERVAL_COUNT);
        ContinuousGenerator generator = new ContinuousGenerator(seed, intervals, probabilities);
        
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < intervals.size(); i++) {
            counts.put(i, 0);
        }

        for (int i = 0; i < SAMPLE_SIZE; i++) {
            double sample = generator.getSample();
            for (int j = 0; j < intervals.size(); j++) {
                if (sample >= intervals.get(j)[0] && sample <= intervals.get(j)[1]) {
                    counts.put(j, counts.get(j) + 1);
                    break;
                }
            }
        }

        printResults("Continuous Generator", intervals, probabilities, counts);
    }

    public static void testDiscreteGenerator(Long seed) {
        List<int[]> intervals = generateDiscreteIntervals();
        List<Double> probabilities = generateProbabilities(INTERVAL_COUNT);
        DiscreteGenerator generator = new DiscreteGenerator(seed, intervals, probabilities);
        
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < intervals.size(); i++) {
            counts.put(i, 0);
        }

        for (int i = 0; i < SAMPLE_SIZE; i++) {
            int sample = generator.getSample();
            for (int j = 0; j < intervals.size(); j++) {
                if (sample >= intervals.get(j)[0] && sample <= intervals.get(j)[1]) {
                    counts.put(j, counts.get(j) + 1);
                    break;
                }
            }
        }

        printResults("Discrete Generator", intervals, probabilities, counts);
    }

    private static void printResults(String title, List<?> intervals, List<?> probabilities, Map<Integer, Integer> counts) {
        System.out.println("--- " + title + " Test Results ---");
        System.out.println("Intervals and Probabilities:");
        double totalProbability = 0.0;
        for (int i = 0; i < intervals.size(); i++) {
            System.out.print("Interval [");
            if (intervals.get(i) instanceof double[]) {
                double[] interval = (double[]) intervals.get(i);
                for (int j = 0; j < interval.length; j++) {
                    System.out.print(interval[j] + (j < interval.length - 1 ? ", " : ""));
                }
            } else if (intervals.get(i) instanceof int[]) {
                int[] interval = (int[]) intervals.get(i);
                for (int j = 0; j < interval.length; j++) {
                    System.out.print(interval[j] + (j < interval.length - 1 ? ", " : ""));
                }
            }
            double probability = ((Number) probabilities.get(i)).doubleValue();
            totalProbability += probability;
            System.out.println("] - Probability: " + probability);
        }
        System.out.println("Total Probability: " + totalProbability);
        if (Math.abs(totalProbability - 1.0) > 1e-6) {
            System.out.println("WARNING: Total probability is not 1.0!");
        }
        System.out.println("Generated Samples Count:");
        for (int i = 0; i < intervals.size(); i++) {
            double expected = SAMPLE_SIZE * ((Number) probabilities.get(i)).doubleValue();
            System.out.print("Interval [");
            if (intervals.get(i) instanceof double[]) {
                double[] interval = (double[]) intervals.get(i);
                for (int j = 0; j < interval.length; j++) {
                    System.out.print(interval[j] + (j < interval.length - 1 ? ", " : ""));
                }
            } else if (intervals.get(i) instanceof int[]) {
                int[] interval = (int[]) intervals.get(i);
                for (int j = 0; j < interval.length; j++) {
                    System.out.print(interval[j] + (j < interval.length - 1 ? ", " : ""));
                }
            }
            System.out.println("] - Count: " + counts.get(i) + " (Expected: " + expected + ")");
        }
    }
    
    private static List<double[]> generateContinuousIntervals() {
        List<double[]> intervals = new ArrayList<>();
        Random random = new Random();
        double start = 0.0;
        for (int i = 0; i < INTERVAL_COUNT; i++) {
            double end = start + random.nextDouble() * 10;
            intervals.add(new double[]{start, end});
            start = end;
        }
        return intervals;
    }
    
    private static List<int[]> generateDiscreteIntervals() {
        List<int[]> intervals = new ArrayList<>();
        Random random = new Random();
        int start = 0;
        for (int i = 0; i < INTERVAL_COUNT; i++) {
            int end = start + random.nextInt(10) + 1;
            intervals.add(new int[]{start, end});
            start = end + 1;
        }
        return intervals;
    }
    
    private static List<Double> generateProbabilities(int count) {
        List<Double> probabilities = new ArrayList<>();
        Random random = new Random();
        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            double p = random.nextDouble();
            probabilities.add(p);
            sum += p;
        }
        for (int i = 0; i < count; i++) {
            probabilities.set(i, probabilities.get(i) / sum);
        }
        return probabilities;
    }
}