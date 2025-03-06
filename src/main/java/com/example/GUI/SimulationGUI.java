package com.example.GUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;

public class SimulationGUI extends JFrame {
    private DefaultCategoryDataset dataset;
    private JFreeChart chart;
    private JButton startButton;
    private JButton stopButton;
    private JTextField repsTextField;
    private JTextField pointsTextField;
    private JComboBox<String> strategyComboBox;
    private SimulationController controller;

    public SimulationGUI(SimulationController controller) {
        super("Simulation GUI");
        this.controller = controller;

        // Inicializácia datasetu a grafu
        dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createLineChart(
                "Average Cost Over Replications", // Názov grafu
                "Replication", // X-os
                "Average Cost", // Y-os
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Pole pre zadanie počtu replikácií
        JLabel repsLabel = new JLabel("Number of Replications:");
        repsTextField = new JTextField(10);

        // Pole pre zadanie počtu bodov na grafe
        JLabel pointsLabel = new JLabel("Number of Points on Graph:");
        pointsTextField = new JTextField(10);

        // Výber stratégie
        JLabel strategyLabel = new JLabel("Select Strategy:");
        String[] strategies = {"Strategy A", "Strategy B", "Strategy C", "Strategy D", "Strategy E", "Strategy F"};
        strategyComboBox = new JComboBox<>(strategies);

        // Tlačidlá
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());

        JPanel controlPanel = new JPanel();
        controlPanel.add(repsLabel);
        controlPanel.add(repsTextField);
        controlPanel.add(pointsLabel);
        controlPanel.add(pointsTextField);
        controlPanel.add(strategyLabel);
        controlPanel.add(strategyComboBox);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startSimulation() {
        try {
            int totalReplications = Integer.parseInt(repsTextField.getText());
            int points = Integer.parseInt(pointsTextField.getText());
            String selectedStrategy = (String) strategyComboBox.getSelectedItem();

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            dataset.clear();

            // Spustenie simulácie
            controller.startSimulation(totalReplications, points, selectedStrategy, (replication, averageCost) -> {
                if (replication % (totalReplications / points) == 0) { // Vykreslenie iba každého N-tého bodu
                    dataset.addValue(averageCost, "Average Cost", Integer.toString(replication));
                }
            });
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for replications and points.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopSimulation() {
        controller.stopSimulation();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }
}