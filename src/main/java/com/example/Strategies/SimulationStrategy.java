package com.example.Strategies;

import java.util.Random;
import java.util.function.BiConsumer;

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
        this.seed = 52787; 
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
        int dayIndex = 0;
        for (int i = 0; i < weeks; i++) {
            getStock(i);
            for (int j = 0; j < 7; j++) {
                if (j == 4) {
                    sellStock();
                }
                double cost = this.suspensionStock * 0.2 + this.brakePadsStock * 0.3 + this.headlightsStock * 0.25;
                this.totalCost += cost;
                if (daily) {
                    processDailyResults(dayIndex, cost);
                    
                }
                dayIndex++;
            }
        }
    }
    protected abstract void getStock(int week);
    protected abstract void sellStock();
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
        this.result = this.totalCost / this.reps;
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
