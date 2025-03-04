package com.example.Generators;

import java.util.List;
import java.util.Random;

import com.example.Constants.Constants;

public class ContinuousGenerator extends AbstractGenerator<Double> {
    private final List<double[]> intervals;
    private final List<Double> probabilities;
    

    public ContinuousGenerator(Long seed, List<double[]> intervals, List<Double> probabilities) {
        super(seed);
        this.intervals = intervals;
        this.probabilities = probabilities;
        for (int i = 0; i < intervals.size(); i++) {
            intervalGenerators.add(new Random(seed));
        }
    }

    @Override
    public Double getSample() {
        double rand = probability.nextDouble();
        double cumulative = 0.0;
        for (int i = 0; i < intervals.size(); i++) {
            cumulative += probabilities.get(i);
            if (rand < cumulative && Math.abs(rand - cumulative) > Constants.epsilon) {
                double min = intervals.get(i)[0];
                double max = intervals.get(i)[1];
                return min + (intervalGenerators.get(i).nextDouble() * (max - min));
            }
        }
        return null;
    }
}