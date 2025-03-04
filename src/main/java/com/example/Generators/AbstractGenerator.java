package com.example.Generators;
import java.util.*;

abstract class AbstractGenerator<T> {
    protected final Random probability;
    protected final List<Random> intervalGenerators;

    public AbstractGenerator(Long seed) {
        this.probability = (seed != null) ? new Random(seed) : new Random();
        this.intervalGenerators = new ArrayList<>();
    }

    public abstract T getSample();
}