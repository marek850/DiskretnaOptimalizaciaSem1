package com.example.SimCore;

public abstract class MonteCarloCore {
    // Callback rozhranie pre posielanie dát do GUI
    public interface SimulationCallback {
        void onDataPoint(int replication, double averageCost);
    }

    protected SimulationCallback callback;

    public void setCallback(SimulationCallback callback) {
        this.callback = callback;
    }
    // Simulačný cyklus
    public final void runSimulation(int numberOfReplications) {
        
        for (int i = 0; i < numberOfReplications; i++) {
            beforeSimRun();
            executeSimRun();
            afterSimRun();
        }
        afterSimulation();
        
    }
    protected void afterSimulation(){
    }
    protected void beforeSimRun() {
    }

    protected void afterSimRun() {
    }

    protected abstract void executeSimRun();
}