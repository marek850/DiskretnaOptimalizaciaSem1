package com.example.GUI;
import com.example.SimCore.MonteCarloCore;
import com.example.Strategies.StrategyA;
import com.example.Strategies.StrategyB;
import com.example.Strategies.StrategyC;
import com.example.Strategies.StrategyD;

import javax.swing.*;

public class SimulationController {
    private MonteCarloCore strategy;
    private Timer timer;
    private int currentReplication;
    private int totalReplications;

    public void startSimulation(int totalReplications, int points, String selectedStrategy, MonteCarloCore.SimulationCallback callback) {
        this.totalReplications = totalReplications;
        this.currentReplication = 0;

        // Vytvorenie inštancie stratégie
        switch (selectedStrategy) {
            case "Strategy A":
                strategy = new StrategyA();
                break;
            case "Strategy B":
                strategy = new StrategyB();
                break;
            case "Strategy C":
            strategy = new StrategyC();
            break;
            case "Strategy D":
            strategy = new StrategyD();
            break;
            // Pridajte ďalšie stratégie podľa potreby
        }

        strategy.setCallback(callback);

        timer = new Timer(10, e -> {
            if (currentReplication >= totalReplications) {
                stopSimulation();
                return;
            }

            // Spustenie jednej replikácie
            strategy.runSimulation(1);
            currentReplication++;
        });

        timer.start();
    }

    public void stopSimulation() {
        if (timer != null) {
            timer.stop();
        }
    }
}