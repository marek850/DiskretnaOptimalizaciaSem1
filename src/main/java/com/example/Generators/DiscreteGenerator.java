package com.example.Generators;
import java.util.List;
import java.util.Random;

import com.example.Constants.Constants;

public class DiscreteGenerator extends AbstractGenerator<Integer> {
    private final List<int[]> intervals;
    private final List<Double> probabilities;

    public DiscreteGenerator(Long seed, List<int[]> intervals, List<Double> probabilities) {
        super(seed);
        this.intervals = intervals;
        this.probabilities = probabilities;
        for (int i = 0; i < intervals.size(); i++) {
            intervalGenerators.add(new Random(seed));
        }
    }

    @Override
    public Integer getSample() {
        double rand = probability.nextDouble();
        double cumulative = 0.0;
        for (int i = 0; i < intervals.size(); i++) {
            cumulative += probabilities.get(i);
            if (rand < cumulative && Math.abs(rand - cumulative) > Constants.epsilon) {
                int min = intervals.get(i)[0];
                int max = intervals.get(i)[1];

                return  min + intervalGenerators.get(i).nextInt(max - min);
            }
        }

       return null;
    }
}