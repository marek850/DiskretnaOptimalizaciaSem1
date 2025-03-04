package com.example.SimCore;

public abstract class MonteCarloCore {
    // Simulačný cyklus
    public final void runSimulation(int numberOfReplications) {
        
        for (int i = 0; i < numberOfReplications; i++) {
            beforeSimRun();
            executeSimRun();
            afterSimRun();
        }
        
    }
    
    protected void beforeSimRun() {
        System.out.println("beforeSimRun()");
    }

    protected void afterSimRun() {
        System.out.println("afterSimRun()");
    }

    protected abstract void executeSimRun();
}