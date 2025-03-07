package com.example.Strategies;
import java.util.List;
import java.util.Random;

import com.example.Generators.ContinuousGenerator;
import com.example.Generators.DiscreteGenerator;
import com.example.SimCore.MonteCarloCore;
public class StrategyB extends SimulationStrategy{
    
    private ContinuousGenerator supplierFirstFourTeenGen = new ContinuousGenerator(seedGenerator, List.of(
        new double[]{5.0, 10.0},
        new double[]{10.0, 50.0},
        new double[]{50.0, 70.0},
        new double[]{70.0, 80.0},
        new double[]{80.0, 95.0}), List.of(
            0.4,
            0.3,
            0.2,
            0.06,
            0.04));
    private ContinuousGenerator supplierLastGen = new ContinuousGenerator(seedGenerator, List.of(
        new double[]{5.0, 10.0},
        new double[]{10.0, 50.0},
        new double[]{50.0, 70.0},
        new double[]{70.0, 80.0},
        new double[]{80.0, 95.0}), List.of(
            0.2,
            0.4,
            0.3,
            0.06,
            0.04));
    private DiscreteGenerator suspensionDemandGen = new DiscreteGenerator(seedGenerator, List.of(new int[]{50, 101}), List.of(1.0));
    private DiscreteGenerator brakePadsDemandGen = new DiscreteGenerator(seedGenerator, List.of(new int[]{60, 251}), List.of(1.0));
    private DiscreteGenerator headlightsDemandGen = new DiscreteGenerator(seedGenerator, List.of(
        new int[]{30, 60},
        new int[]{60, 100},
        new int[]{100, 140}, 
        new int[]{140, 160}), List.of(
            0.2,
            0.4,
            0.3, 
            0.1));
    public StrategyB() {
        
    this.suspensionSupply = 100;
    this.brakePadsSupply = 200;
    this.headlightsSupply = 150;
    this.weeks = 30;
    this.suspensionStock = 0;
    this.brakePadsStock = 0;
    this.headlightsStock = 0;
    this.totalCost = 0.0;
    this.result = 0.0;
    }


    @Override
    protected void executeSimRun() {
        //System.out.println(this.seed);
        //Prechadzam 30 tyzdnov
        for (int i = 0; i < weeks; i++) {
            //naskladnim suciastky na zaciatku tyzdna
            getStock(i);
            //Prechadzam dni v tyzdni
            for (int j = 0; j < 7; j++) {
                if (j == 4) {
                    sellStock();
                }
                //pripocitavam naklady na skladovanie
                this.totalCost += this.suspensionStock * 0.2;
                this.totalCost += this.brakePadsStock * 0.3;
                this.totalCost += this.headlightsStock * 0.25;
                //ak je piatok tak predam suciastky
                
            }
        }
        this.suspensionStock = 0;
        this.brakePadsStock = 0;
        this.headlightsStock = 0;
        this.reps += 1;
        //spocitam priemerne naklady za 30 tyzdnov( jednu replikaciu simulacie)
        this.result = this.totalCost / this.reps;
        //System.out.println(this.result);
    }
    @Override
    protected void afterSimulation(){
        
    }
    private void sellStock(){
        //vygenerujem si dopyt na suciastky
        int suspensionDemand = this.suspensionDemandGen.getSample();
        int brakePadsDemand = this.brakePadsDemandGen.getSample();  
        int headlightsDemand = this.headlightsDemandGen.getSample();
        int totalMissing = 0;
        //ak je dopyt vacsi ako zasoby tak pridam chybajucu suciastku do celkovej chyby
        //a zasoby nastavim na 0
        if (suspensionStock < suspensionDemand) {
            totalMissing += suspensionDemand - suspensionStock;
            this.suspensionStock = 0;   
        }
        //ak je dopyt mensi ako zasoby tak odobere suciastky zo zasob
        else {
            this.suspensionStock -= suspensionDemand;
        }
        if (brakePadsStock < brakePadsDemand) {
            totalMissing += brakePadsDemand - brakePadsStock;
            this.brakePadsStock = 0;
        }else {
            this.brakePadsStock -= brakePadsDemand;
        }
        if (headlightsStock < headlightsDemand) {
            totalMissing += headlightsDemand - headlightsStock;
            this.headlightsStock = 0;
        }else {
            this.headlightsStock -= headlightsDemand;
        }
        //za vsetky chybajuce suciastky pridam naklady na pokutu
        this.totalCost += totalMissing * 0.3;
    }
    private void getStock(int week){
        double probabilityOfSupply = 0.0;
        //prvych 10 tyzdnov je pravdepodobnost dodania od 10 do 70 percent
        //Vygenerujem s akou pravdepodobnostou dodavatel doda v danom tyzdni
        if (week < 15) {
            probabilityOfSupply = supplierFirstFourTeenGen.getSample();
        }
        //od 11 tyzdna je pravdepodobnost dodania od 30 do 95 percent
        else {
            probabilityOfSupply = supplierLastGen.getSample();
        }
        //vygenerujem si nahodnu hodnotu a ak je mensia ako pravdepodobnost dodania tak dodam(navysim zasoby na sklade o fixnu hodnotu )
        double randomValue = probabilityGenerator.nextDouble() * 100;
        if (randomValue < probabilityOfSupply) {
            this.suspensionStock += this.suspensionSupply;
            this.brakePadsStock += this.brakePadsSupply;
            this.headlightsStock += this.headlightsSupply;
        }
    }
}
