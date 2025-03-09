package com.example.Strategies;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import com.example.Generators.ContinuousGenerator;
import com.example.Generators.DiscreteGenerator;
import com.example.SimCore.MonteCarloCore;

public class CustomStrategy extends SimulationStrategy {
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
    private List<int[]> weeklyConfigurations = new ArrayList<>();
    public CustomStrategy(BiConsumer<Double,Integer> datasetUpdater) {
        super(datasetUpdater);
        this.suspensionSupply = 100;
        this.brakePadsSupply = 200;
        this.headlightsSupply = 150;
        this.weeks = 30;
        this.suspensionStock = 0;
        this.brakePadsStock = 0;
        this.headlightsStock = 0;
        this.loadConfiguration();
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
        this.result = this.totalCost / this.reps;
    }

    private int[] loadWeekOrder(int week) {
        int suspensionDemand = this.weeklyConfigurations.get(week)[0];
        int brakePadsDemand = this.weeklyConfigurations.get(week)[1];
        int headlightsDemand = this.weeklyConfigurations.get(week)[2];
        return new int[]{suspensionDemand, brakePadsDemand, headlightsDemand};
    }

    private int chooseSupplier(int week) {
        return this.weeklyConfigurations.get(week)[3];
    }

    private boolean simulateDelivery(int supplier, int week) {
        double probability = supplier == 1 ? (week < 10 ? supplierFirstTenGen.getSample() : supplierLastGen.getSample()) : (week < 15 ? supplier2FirstFourTeenGen.getSample() : supplier2LastGen.getSample());
        return probabilityGenerator.nextDouble() * 100 < probability;
    }
    private void getStock(int week){
        int[] order = loadWeekOrder(week);           
        int supplier = chooseSupplier(week);
        boolean isDelivered = simulateDelivery(supplier, week);

        if (isDelivered) {
            this.suspensionStock += order[0];
            this.brakePadsStock += order[1];
            this.headlightsStock += order[2];
        }

    }

    private void sellStock() {
        //vygenerujem si dopyt na suciastky
        int suspensionDemand = this.suspensionDemandGen.getSample();
        int brakePadsDemand = this.brakePadsDemandGen.getSample();  
        int headlightsDemand = this.headlightsDemandGen.getSample();
        int totalMissing = 0;
        //ak je dopyt vacsi ako zasoby tak pridam chybajucu suciastku do celkovej chyby
        //a zasoby nastavim na 0
        if (suspensionStock < suspensionDemand) {
            totalMissing += suspensionDemand - suspensionStock;
            this.suspensionStock = 0;   
        }
        //ak je dopyt mensi ako zasoby tak odobere suciastky zo zasob
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
        //za vsetky chybajuce suciastky pridam naklady na pokutu
        this.totalCost += totalMissing * 0.3;
    }
    public void loadConfiguration() {
    Path path = Paths.get("Config", "CustomStrategy.csv");
    try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            int[] config = new int[4];
            config[0] = Integer.parseInt(values[0]); // Počet tlmičov
            config[1] = Integer.parseInt(values[1]); // Počet brzdových doštičiek
            config[2] = Integer.parseInt(values[2]); // Počet svetlometov
            config[3] = Integer.parseInt(values[3]); // Výber dodávateľa
            weeklyConfigurations.add(config);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
	@Override
	public void setDaily(boolean daily2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setDaily'");
	}
}