package com.example.GUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import com.example.App.MonteCarloSimApp;
import com.example.Strategies.SimulationStrategy;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class SimulationGUI extends JFrame {
    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private JButton startButton;
    private JButton stopButton;
    private JTextField repsTextField;
    private JTextField pointsTextField;
    private JComboBox<String> strategyComboBox;
    private MonteCarloSimApp app;

    public SimulationGUI(MonteCarloSimApp app) {
        super("Simulation GUI");
        this.app = app;

        // Inicializácia datasetu a grafu
        dataset = new XYSeriesCollection(new XYSeries("Celkove naklady"));
        chart = ChartFactory.createXYLineChart(
                "Simulácia celkových nákladov", // Názov grafu
                "Replikacie", // X-os
                "Celkové náklady", // Y-os
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(true); // Enable auto-ranging
        rangeAxis.setAutoRangeIncludesZero(false); // Exclude zero from the Y-axis range

        
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
        String[] strategies = {"StrategyA", "StrategyB", "StrategyC", "StrategyD", "StrategyE", "StrategyF"};
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
    public void enableStartButton() {
        startButton.setEnabled(true);
    }

    public void enableStopButton() {
        stopButton.setEnabled(true);
    }
    public void disableStartButton() {
        startButton.setEnabled(false);
    }

    public void disableStopButton() {
        stopButton.setEnabled(false);
    }
    private void startSimulation() {
        
        int totalReplications = Integer.parseInt(repsTextField.getText());
        if (totalReplications == 1) {
            XYPlot plot = (XYPlot) chart.getPlot();
            ValueAxis xAxis = plot.getDomainAxis();
            xAxis.setLabel("Dni");
        }else{
            XYPlot plot = (XYPlot) chart.getPlot();
            ValueAxis xAxis = plot.getDomainAxis();
            xAxis.setLabel("Celkové náklady");
        }
        int points = 0;
        if (pointsTextField.getText() != null && !pointsTextField.getText().isEmpty()) {
            points = Integer.parseInt(pointsTextField.getText());
        }
        String strategyString = (String)strategyComboBox.getSelectedItem();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        dataset.getSeries("Celkove naklady").clear();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        app.startSimulation(totalReplications, points, strategyString, dataset);
    }

    private void stopSimulation() {
        app.stopSimulation();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }
}