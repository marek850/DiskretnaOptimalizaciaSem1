package com.example;

import javax.swing.SwingUtilities;

import com.example.GUI.SimulationController;
import com.example.GUI.SimulationGUI;
import com.example.Strategies.StrategyA;
import com.example.Strategies.StrategyB;
import com.example.Strategies.StrategyC;
import com.example.Strategies.StrategyD;
import com.example.Strategies.StrategyE;
import com.example.Strategies.StrategyF;
import com.example.Strategies.StrategyG;

public class Main {
    public static void main(String[] args) {
         

        // Generovanie pravdepodobností pre diskrétny generátor
        /* GeneratorTester.testContinuousGenerator(seed);  
        GeneratorTester.testDiscreteGenerator(seed); */
        
        StrategyA strategyA = new StrategyA();
        strategyA.runSimulation(100000);
        /* StrategyB strategyB = new StrategyB();
        strategyB.runSimulation(100000); */
        /* SwingUtilities.invokeLater(() -> {
            SimulationController controller = new SimulationController();
            SimulationGUI gui = new SimulationGUI(controller);
            gui.setVisible(true);
        }); */
    
    }
}