package fr.inria.astor.approaches.jgenprog;

import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.solutionsearch.population.FitnessFunction;

import java.util.List;

public interface CrossoverOperator {
    void applyCrossover(Pair programVariants, int generation, FitnessFunction fitnessFunction);

    record Pair (ProgramVariant left, ProgramVariant right) {}
}
