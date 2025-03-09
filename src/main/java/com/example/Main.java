package com.example;

import javax.swing.SwingUtilities;

import com.example.App.MonteCarloSimApp;
import com.example.GUI.SimulationGUI;

public class Main {
    public static void main(String[] args) { 
        MonteCarloSimApp app = new MonteCarloSimApp();
            SwingUtilities.invokeLater(() -> {
                SimulationGUI gui = new SimulationGUI(app);
                gui.setVisible(true);
            });
        
    
    }
}