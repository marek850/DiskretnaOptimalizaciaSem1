package com.example.Strategies;
import java.util.List;
import java.util.Random;

import com.example.Generators.ContinuousGenerator;
import com.example.Generators.DiscreteGenerator;
import com.example.SimCore.MonteCarloCore;

public class StrategyF extends SimulationStrategy {
    // Generátory pre pravdepodobnosti dodania
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
    private ContinuousGenerator supplierFirstTenGen = new ContinuousGenerator(seedGenerator, List.of(new double[]{10.0, 70.0}), List.of(1.0));
    private ContinuousGenerator supplierLastGen = new ContinuousGenerator(seedGenerator, List.of(new double[]{30.0, 95.0}), List.of(1.0));

    // Premenné pre sledovanie štatistík
    private double[] totalSuspensionOrder = new double[weeks];
    private double[] totalBrakePadsOrder = new double[weeks];
    private double[] totalHeadlightsOrder = new double[weeks];
    private int[] supplier1Usage = new int[weeks];
    private int[] supplier2Usage = new int[weeks];
    public StrategyF() {
        this.suspensionSupply = 100;
        this.brakePadsSupply = 200;
        this.headlightsSupply = 150;
        this.weeks = 30;
        this.suspensionStock = 0;
        this.brakePadsStock = 0;
        this.headlightsStock = 0;
    }   
    @Override
    protected void executeSimRun() {
        for (int i = 0; i < weeks; i++) {
            //predpovedame dopyt
            int[] demand = predictDemand();
            //vypocitame idealnu objednavku
            int[] order = calculateOrder(demand);
            //vyberieme vhodnejsieho dodavatela
            int supplier = chooseSupplier(i);
            //nasimulujeme dodanie objednavky
            boolean isDelivered = simulateDelivery(supplier, i);

            // Aktualizácia štatistík objednávok
            totalSuspensionOrder[i] += order[0];
            totalBrakePadsOrder[i] += order[1];
            totalHeadlightsOrder[i] += order[2];

            // Aktualizácia štatistík využitia dodávateľov
            if (supplier == 1) {
                supplier1Usage[i]++;
            } else {
                supplier2Usage[i]++;
            }
            //doplnime zasoby ak bola objednavka dorucena
            if (isDelivered) {
                this.suspensionStock += order[0];
                this.brakePadsStock += order[1];
                this.headlightsStock += order[2];
            }

            for (int j = 0; j < 7; j++) {
                if (j == 4) {
                    sellStock(demand);
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
        this.result = this.totalCost / this.reps;
    }

    @Override
    protected void afterSimulation() {
        System.out.println("Cost " + this.result);

        // Výpočet priemerných objednávok pre každý produkt
        double avgSuspensionOrder = 0;
        double avgBrakePadsOrder = 0;
        double avgHeadlightsOrder = 0;

        for (int i = 0; i < weeks; i++) {
            avgSuspensionOrder += totalSuspensionOrder[i];
            avgBrakePadsOrder += totalBrakePadsOrder[i];
            avgHeadlightsOrder += totalHeadlightsOrder[i];
        }

        avgSuspensionOrder /= (weeks * reps);
        avgBrakePadsOrder /= (weeks * reps);
        avgHeadlightsOrder /= (weeks * reps);

        System.out.println("Average suspension order per week: " + avgSuspensionOrder);
        System.out.println("Average brake pads order per week: " + avgBrakePadsOrder);
        System.out.println("Average headlights order per week: " + avgHeadlightsOrder);

        // Výpočet priemerného využitia dodávateľov
        System.out.println("\nSupplier usage per week:");
        for (int i = 0; i < weeks; i++) {
            double supplier1UsageRate = (double) supplier1Usage[i] / reps * 100;
            double supplier2UsageRate = (double) supplier2Usage[i] / reps * 100;
            System.out.println("Week " + (i + 1) + ": Supplier 1 = " + supplier1UsageRate + "%, Supplier 2 = " + supplier2UsageRate + "%");
        }
    }

    private int[] predictDemand() {
        int suspensionDemand = this.suspensionDemandGen.getSample();
        int brakePadsDemand = this.brakePadsDemandGen.getSample();
        int headlightsDemand = this.headlightsDemandGen.getSample();
        return new int[]{suspensionDemand, brakePadsDemand, headlightsDemand};
    }

    private int[] calculateOrder(int[] demand) {
        int suspensionOrder = Math.max(0, demand[0] - this.suspensionStock);
        int brakePadsOrder = Math.max(0, demand[1] - this.brakePadsStock);
        int headlightsOrder = Math.max(0, demand[2] - this.headlightsStock);
        return new int[]{suspensionOrder, brakePadsOrder, headlightsOrder};
    }

    private int chooseSupplier(int week) {
        double supplier1Probability = week < 10 ? supplierFirstTenGen.getSample() : supplierLastGen.getSample();
        double supplier2Probability = week < 15 ? supplier2FirstFourTeenGen.getSample() : supplier2LastGen.getSample();
        return supplier1Probability > supplier2Probability ? 1 : 2;
    }

    private boolean simulateDelivery(int supplier, int week) {
        double probability = supplier == 1 ? (week < 10 ? supplierFirstTenGen.getSample() : supplierLastGen.getSample()) : (week < 15 ? supplier2FirstFourTeenGen.getSample() : supplier2LastGen.getSample());
        return probabilityGenerator.nextDouble() * 100 < probability;
    }

    private void sellStock(int[] demand) {
        int totalMissing = 0;

        if (suspensionStock < demand[0]) {
            totalMissing += demand[0] - suspensionStock;
            this.suspensionStock = 0;   
        } else {
            this.suspensionStock -= demand[0];
        }
        if (brakePadsStock < demand[1]) {
            totalMissing += demand[1] - brakePadsStock;
            this.brakePadsStock = 0;
        } else {
            this.brakePadsStock -= demand[1];
        }
        if (headlightsStock < demand[2]) {
            totalMissing += demand[2] - headlightsStock;
            this.headlightsStock = 0;
        } else {
            this.headlightsStock -= demand[2];
        }
        this.totalCost += totalMissing * 0.3;
    }
}