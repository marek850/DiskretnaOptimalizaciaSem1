package com.example;

import com.example.GeneratorTester.GeneratorTester;

public class Main {
    public static void main(String[] args) {
         long seed = 42L; // Konštantné seed pre opakovateľné výsledky

        // Generovanie pravdepodobností pre diskrétny generátor
        GeneratorTester.testContinuousGenerator(seed);  
        GeneratorTester.testDiscreteGenerator(seed);
    }
}