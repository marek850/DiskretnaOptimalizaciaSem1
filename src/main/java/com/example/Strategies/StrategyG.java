package com.example.Strategies;
import java.util.List;
import java.util.Random;

import com.example.Generators.ContinuousGenerator;
import com.example.Generators.DiscreteGenerator;
import com.example.SimCore.MonteCarloCore;
public class StrategyG extends SimulationStrategy{
    private ContinuousGenerator supplierFirstTenGen = new ContinuousGenerator(seedGenerator, List.of(new double[]{10.0, 70.0}), List.of(1.0));;
    private ContinuousGenerator supplierLastGen = new ContinuousGenerator(seedGenerator, List.of(new double[]{30.0, 95.0}), List.of(1.0));
    private DiscreteGenerator suspensionDemandGen = new DiscreteGenerator(seedGenerator, List.of(new int[]{50, 101}), List.of(1.0));
    private DiscreteGenerator brakePadsDemandGen = new DiscreteGenerator(seedGenerator, List.of(new int[]{60, 251}), List.of(1.0));
    private DiscreteGenerator headlightsDemandGen = new DiscreteGenerator(seedGenerator, List.of(new int[]{30, 60},new int[]{60, 100},new int[]{100, 140}           
                                                                                        , new int[]{140, 160}), List.of(0.2, 0.4, 0.3, 0.1));
    public StrategyG() {
        this.suspensionSupply = 75;
        this.brakePadsSupply = 155;
        this.headlightsSupply = 91;
        this.weeks = 30;
        this.suspensionStock = 0;
        this.brakePadsStock = 0;
        this.headlightsStock = 0;
    }
    
    

    @Override
    protected void executeSimRun() {
        for (int i = 0; i < weeks; i++) {
            getStock(i);
            for (int j = 0; j < 7; j++) {
                if (j == 4) {
                    sellStock();
                }
                this.totalCost += this.suspensionStock * 0.2;
                this.totalCost += this.brakePadsStock * 0.3;
                this.totalCost += this.headlightsStock * 0.25;                
            }
        }
        this.suspensionStock = 0;
        this.brakePadsStock = 0;
        this.headlightsStock = 0;
        this.reps += 1;
        //spocitam priemerne naklady za 30 tyzdnov( jednu replikaciu simulacie)
        this.result = this.totalCost / this.reps;        
    }
    @Override
    protected void afterSimulation(){
        System.out.println("Cost " + this.result);
    }
    private void sellStock(){
        //vygenerujem si dopyt na suciastky
        int suspensionDemand = this.suspensionDemandGen.getSample();
        int brakePadsDemand = this.brakePadsDemandGen.getSample();  
        int headlightsDemand = this.headlightsDemandGen.getSample();
        int totalMissing = 0;
        if (suspensionStock < suspensionDemand) {
            totalMissing += suspensionDemand - suspensionStock;
            this.suspensionStock = 0;   
        }
        else {
            this.suspensionStock -= suspensionDemand;
        }
        if (brakePadsStock < brakePadsDemand) {
            totalMissing += brakePadsDemand - brakePadsStock;
            this.brakePadsStock = 0;
        }else {
            this.brakePadsStock -= brakePadsDemand;
        }
        if (headlightsStock < headlightsDemand) {
            totalMissing += headlightsDemand - headlightsStock;
            this.headlightsStock = 0;
        }else {
            this.headlightsStock -= headlightsDemand;
        }
        this.totalCost += totalMissing * 0.3;
    }
    private void getStock(int week){
        double probabilityOfSupply = 0.0;
        
        if (week < 10) {
            probabilityOfSupply = supplierFirstTenGen.getSample();
        }
        else {
            probabilityOfSupply = supplierLastGen.getSample();
        }
        double randomValue = probabilityGenerator.nextDouble() * 100;
        if (randomValue < probabilityOfSupply) {
            this.suspensionStock += this.suspensionSupply;
            this.brakePadsStock += this.brakePadsSupply;
            this.headlightsStock += this.headlightsSupply;
        }
    }
}
