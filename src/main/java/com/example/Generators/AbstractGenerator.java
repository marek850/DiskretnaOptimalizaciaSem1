package com.example.Generators;
import java.util.*;

abstract class AbstractGenerator<T> {
    protected final Random seedGenerator;
    protected final Random probabilityGenerator;
    protected final List<Random> intervalGenerators;

    public AbstractGenerator(Random seedGenerator) {
        this.seedGenerator = seedGenerator;
        this.probabilityGenerator = new Random(seedGenerator.nextLong());
        this.intervalGenerators = new ArrayList<>();
    }

    public abstract T getSample();
}