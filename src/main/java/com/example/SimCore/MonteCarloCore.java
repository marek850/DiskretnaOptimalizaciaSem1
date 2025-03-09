package com.example.SimCore;

public abstract class MonteCarloCore {
    private boolean stop = false;
    public void setStop(boolean stop) {
        this.stop = stop;
    }
    // Simulačný cyklus
    public final void runSimulation(int numberOfReplications) {
        
        for (int i = 0; i < numberOfReplications; i++) {
            if (this.stop) {
                break;
            }
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