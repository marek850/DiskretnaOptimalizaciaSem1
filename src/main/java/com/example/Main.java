package com.example;

import javax.swing.SwingUtilities;

import com.example.App.MonteCarloSimApp;
import com.example.GUI.SimulationGUI;
import com.example.Strategies.StrategyA;

public class Main {
    public static void main(String[] args) {
         

        // Generovanie pravdepodobností pre diskrétny generátor
        /* GeneratorTester.testContinuousGenerator(seed);  
        GeneratorTester.testDiscreteGenerator(seed); */
        
        /* StrategyA strategyA = new StrategyA();
        strategyA.runSimulation(100000); */
        /* StrategyB strategyB = new StrategyB();
        strategyB.runSimulation(100000); */
        MonteCarloSimApp app = new MonteCarloSimApp();
            SwingUtilities.invokeLater(() -> {
                SimulationGUI gui = new SimulationGUI(app);
                gui.setVisible(true);
            });
        
    
    }
}