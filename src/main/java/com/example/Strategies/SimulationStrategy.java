package com.example.Strategies;

import java.util.Random;
import java.util.function.BiConsumer;

import javax.swing.Spring;

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
    protected boolean daily;
    protected BiConsumer<Double,Integer> datasetUpdater;
    public SimulationStrategy(BiConsumer<Double,Integer> datasetUpdater) {
        this.seed = 52787; // Default seed, can be overridden by successors
        this.seedGenerator = new Random();
        this.probabilityGenerator = new Random(seedGenerator.nextLong());
        this.reps = 0;
        this.totalCost = 0.0;
        this.result = 0.0;
        this.daily = false;
        this.datasetUpdater = datasetUpdater;
    }
    @Override
    protected void executeSimRun() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeSimRun'");
    }
    public void setDaily(boolean daily2){
        this.daily = daily2;
    }
    public void setDatasetUpdater(BiConsumer<Double,Integer> datasetUpdater) {
        this.datasetUpdater = datasetUpdater;
    }
    protected void processDailyResults(int day, double cost) {
        if (this.datasetUpdater != null) {
            this.datasetUpdater.accept(cost, day+1);
        }
    }
    @Override
    protected void afterSimRun() {
        super.afterSimRun();
        this.suspensionStock = 0;
        this.brakePadsStock = 0;
        this.headlightsStock = 0;
        this.reps += 1;
        //spocitam priemerne naklady za 30 tyzdnov( jednu replikaciu simulacie)
        this.result = this.totalCost / this.reps;
        // Update the dataset with the result
        if (this.datasetUpdater != null && !this.daily) {
            this.datasetUpdater.accept(this.result, this.reps);
        }
    }
    @Override
    protected void afterSimulation() {
        super.afterSimulation();
        System.out.println("Priemerne naklady na 30 tyzdnov: " + this.result);
    }
}
