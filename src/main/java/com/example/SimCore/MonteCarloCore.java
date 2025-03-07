package com.example.SimCore;

public abstract class MonteCarloCore {
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