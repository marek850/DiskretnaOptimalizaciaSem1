package com.example;

import com.example.Strategies.StrategyA;

public class Main {
    public static void main(String[] args) {
         Integer seed = 421; // Konštantné seed pre opakovateľné výsledky

        // Generovanie pravdepodobností pre diskrétny generátor
        /* GeneratorTester.testContinuousGenerator(seed);  
        GeneratorTester.testDiscreteGenerator(seed); */
        
        StrategyA strategyA = new StrategyA();
        strategyA.runSimulation(1000000);
    
    }
}