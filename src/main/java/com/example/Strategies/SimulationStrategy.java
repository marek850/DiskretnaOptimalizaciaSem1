package com.example.Strategies;

import java.util.Random;

import com.example.Generators.ContinuousGenerator;
import com.example.Generators.DiscreteGenerator;
import com.example.SimCore.MonteCarloCore;

public abstract class SimulationStrategy extends MonteCarloCore{
    protected int suspensionSupply;
    protected int brakePadsSupply;
    protected int headlightsSupply;
    protected int weeks;
    protected int suspensionStock;
    protected int brakePadsStock;
    protected int headlightsStock;
    protected double totalCost;
    protected double result;
    protected int seed;
    protected int reps;
    protected Random seedGenerator;
    protected Random probabilityGenerator;
    public SimulationStrategy() {
        this.seed = 52787; // Default seed, can be overridden by successors
        this.seedGenerator = new Random();
        this.probabilityGenerator = new Random(seedGenerator.nextLong());
        this.reps = 0;
        this.totalCost = 0.0;
        this.result = 0.0;
    }
    @Override
    protected void executeSimRun() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeSimRun'");
    }
    
}
