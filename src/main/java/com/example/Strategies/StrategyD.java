package com.example.Strategies;
import java.util.List;
import java.util.function.BiConsumer;

import com.example.Generators.ContinuousGenerator;
import com.example.Generators.DiscreteGenerator;
public class StrategyD extends SimulationStrategy{
    private ContinuousGenerator supplier2FirstFourTeenGen = new ContinuousGenerator(seedGenerator, List.of(
        new double[]{5.0, 10.0},
        new double[]{10.0, 50.0},
        new double[]{50.0, 70.0},
        new double[]{70.0, 80.0},
        new double[]{80.0, 95.0}), List.of(
            0.4,
            0.3,
            0.2,
            0.06,
            0.04));
    private ContinuousGenerator supplier2LastGen = new ContinuousGenerator(seedGenerator, List.of(
        new double[]{5.0, 10.0},
        new double[]{10.0, 50.0},
        new double[]{50.0, 70.0},
        new double[]{70.0, 80.0},
        new double[]{80.0, 95.0}), List.of(
            0.2,
            0.4,
            0.3,
            0.06,
            0.04));
    private DiscreteGenerator suspensionDemandGen = new DiscreteGenerator(seedGenerator, List.of(new int[]{50, 101}), List.of(1.0));
    private DiscreteGenerator brakePadsDemandGen = new DiscreteGenerator(seedGenerator, List.of(new int[]{60, 251}), List.of(1.0));
    private DiscreteGenerator headlightsDemandGen = new DiscreteGenerator(seedGenerator, List.of(
        new int[]{30, 60},
        new int[]{60, 100},
        new int[]{100, 140}, 
        new int[]{140, 160}), List.of(
            0.2,
            0.4,
            0.3, 
            0.1));
    private ContinuousGenerator supplierFirstTenGen = new ContinuousGenerator(seedGenerator, List.of(new double[]{10.0, 70.0}), List.of(1.0));;
    private ContinuousGenerator supplierLastGen = new ContinuousGenerator(seedGenerator, List.of(new double[]{30.0, 95.0}), List.of(1.0));
    public StrategyD(BiConsumer<Double,Integer> datasetUpdater) {
        super(datasetUpdater);
        this.suspensionSupply = 100;
        this.brakePadsSupply = 200;
        this.headlightsSupply = 150;
        this.weeks = 30;
        this.suspensionStock = 0;
        this.brakePadsStock = 0;
        this.headlightsStock = 0;
    }
    
    protected void sellStock(){
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
    protected void getStock(int week){
            double probabilityOfSupply = 0.0;
            boolean isOddWeek = (week % 2 == 0); 
            if (!isOddWeek) {
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
            } else{
                if (week < 15) {
                    probabilityOfSupply = supplier2FirstFourTeenGen.getSample();
                }
                else {
                    probabilityOfSupply = supplier2LastGen.getSample();
                }
                double randomValue = probabilityGenerator.nextDouble() * 100;
                if (randomValue < probabilityOfSupply) {
                    this.suspensionStock += this.suspensionSupply;
                    this.brakePadsStock += this.brakePadsSupply;
                    this.headlightsStock += this.headlightsSupply;
                }
            }
            
        }
}
