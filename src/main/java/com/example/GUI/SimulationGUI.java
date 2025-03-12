package com.example.GUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import com.example.App.MonteCarloSimApp;
import javax.swing.*;
import java.awt.*;

public class SimulationGUI extends JFrame {
    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private JButton startButton;
    private JButton stopButton;
    private JTextField repsTextField;
    private JTextField pointsTextField;
    private JTextField currentReplicationTextField; // Pole pre aktuálnu replikáciu
    private JTextField averageCostTextField; // Pole pre priemerné náklady
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
        /* XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        chart.getXYPlot().setRenderer(renderer); */
        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(true); // Enable auto-ranging
        rangeAxis.setAutoRangeIncludesZero(false); // Exclude zero from the Y-axis range

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Pole pre zadanie počtu replikácií
        JLabel repsLabel = new JLabel("Počet replikácií:");
        repsTextField = new JTextField(10);

        // Pole pre zadanie počtu bodov na grafe
        JLabel pointsLabel = new JLabel("Počet bodov vykreslených na grafe:");
        pointsTextField = new JTextField(10);

        // Nové polia pre aktuálnu replikáciu a priemerné náklady
        JLabel currentReplicationLabel = new JLabel("Aktuálna replikácia:");
        currentReplicationTextField = new JTextField(10);
        currentReplicationTextField.setEditable(false); // Zakázanie editácie

        JLabel averageCostLabel = new JLabel("Priemerné náklady:");
        averageCostTextField = new JTextField(10);
        averageCostTextField.setEditable(false); // Zakázanie editácie

        // Výber stratégie
        JLabel strategyLabel = new JLabel("Výber stratégie:");
        String[] strategies = {"StrategyA", "StrategyB", "StrategyC", "StrategyD", "StrategyE", "StrategyG", "CustomStrategy"};
        strategyComboBox = new JComboBox<>(strategies);

        // Tlačidlá
        startButton = new JButton("Spustiť");
        stopButton = new JButton("Zastaviť");
        stopButton.setEnabled(false);

        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());

        JPanel controlPanel = new JPanel();
        controlPanel.add(repsLabel);
        controlPanel.add(repsTextField);
        controlPanel.add(pointsLabel);
        controlPanel.add(pointsTextField);
        controlPanel.add(currentReplicationLabel); // Pridanie labelu pre aktuálnu replikáciu
        controlPanel.add(currentReplicationTextField); // Pridanie textového poľa pre aktuálnu replikáciu
        controlPanel.add(averageCostLabel); // Pridanie labelu pre priemerné náklady
        controlPanel.add(averageCostTextField); // Pridanie textového poľa pre priemerné náklady
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
        } else {
            XYPlot plot = (XYPlot) chart.getPlot();
            ValueAxis xAxis = plot.getDomainAxis();
            xAxis.setLabel("Replikácie");
        }
        int points = 0;
        if (pointsTextField.getText() != null && !pointsTextField.getText().isEmpty()) {
            points = Integer.parseInt(pointsTextField.getText());
        }
        String strategyString = (String) strategyComboBox.getSelectedItem();

        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        dataset.getSeries("Celkove naklady").clear();
        final int finalPoints = points;
        Thread thread = new Thread(() -> {
            app.startSimulation(totalReplications, finalPoints, strategyString, dataset, currentReplicationTextField, averageCostTextField);
            enableStartButton();
            disableStopButton();
        }); 
        thread.start();   
    }  
    private void stopSimulation() {
        app.stopSimulation();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

}