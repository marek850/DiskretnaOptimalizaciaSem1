package com.example.App;

import java.lang.reflect.Constructor;
import javax.swing.Timer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;

import com.example.Strategies.SimulationStrategy;

public class MonteCarloSimApp {
    private SimulationStrategy simulatedStrategy;

    public void startSimulation(int totalReplications, int points, String strategyString,
            XYSeriesCollection dataset) {
        try {
            // Create a Consumer<Double> to update the dataset
            BiConsumer<Double, Integer> datasetUpdater = (result, reps) -> {
                int replicationsToSkip = (int) (totalReplications * 0.4);
                if (totalReplications == 1) {
                    dataset.getSeries("Celkove naklady").add(reps, result);
                    
                }else{
                    if (reps >= replicationsToSkip) {
                        if (reps % (totalReplications/points) == 0) { 
                            dataset.getSeries("Celkove naklady").add(reps, result);
                        }
                    }
                }
            };

            // Build the full class name
            String classname = "com.example.Strategies." + strategyString;

            // Get the strategy class and its constructor
            Class<?> strategyClass = Class.forName(classname);
            Constructor<?> constructor = strategyClass.getDeclaredConstructor(BiConsumer.class);

            // Instantiate the strategy with the datasetUpdater
            this.simulatedStrategy = (SimulationStrategy) constructor.newInstance(datasetUpdater);

            // Set daily mode if needed
            boolean daily = (totalReplications == 1);
            this.simulatedStrategy.setDaily(daily);
            Thread thread = new Thread(() -> {
            // Run the simulation
                this.simulatedStrategy.runSimulation(totalReplications);
            });
            thread.start();
            

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start simulation: " + e.getMessage(), e);
        }
    }

    public void stopSimulation() {
        this.simulatedStrategy.setStop(true);
    }
    
}
