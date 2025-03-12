package com.example.App;

import java.lang.reflect.Constructor;
import java.util.function.BiConsumer;

import javax.swing.JTextField;

import org.jfree.data.xy.XYSeriesCollection;

import com.example.Strategies.SimulationStrategy;

public class MonteCarloSimApp {
    private SimulationStrategy simulatedStrategy;

    public void startSimulation(int totalReplications, int points, String strategyString,
            XYSeriesCollection dataset, JTextField currentReplicationTextField, JTextField averageCostTextField) {
        try {
            BiConsumer<Double, Integer> datasetUpdater = (result, reps) -> {
                int replicationsToSkip = (int) (totalReplications * 0.3);
                if (totalReplications == 1) {
                    dataset.getSeries("Celkove naklady").add(reps, result);
                }else{
                    
                    if (reps >= replicationsToSkip) {
                        if (totalReplications > points) {
                            if (reps % (totalReplications/points) == 0) { 
                                dataset.getSeries("Celkove naklady").add(reps, result);
                            }
                        }
                        else {
                            dataset.getSeries("Celkove naklady").add(reps, result);
                        }
                        
                    }
                }
                currentReplicationTextField.setText(String.valueOf(reps));
                averageCostTextField.setText(String.format("%.2f", result));
            };

            String classname = "com.example.Strategies." + strategyString;
            Class<?> strategyClass = Class.forName(classname);
            Constructor<?> constructor = strategyClass.getDeclaredConstructor(BiConsumer.class);
            this.simulatedStrategy = (SimulationStrategy) constructor.newInstance(datasetUpdater);
            boolean daily = (totalReplications == 1);
            this.simulatedStrategy.setDaily(daily);
            
            this.simulatedStrategy.runSimulation(totalReplications);
            

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start simulation: " + e.getMessage(), e);
        }
    }

    public void stopSimulation() {
        this.simulatedStrategy.setStop(true);
    }
    
}
