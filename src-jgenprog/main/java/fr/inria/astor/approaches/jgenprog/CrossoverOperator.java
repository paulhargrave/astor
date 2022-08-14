package fr.inria.astor.approaches.jgenprog;

import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.solutionsearch.population.FitnessFunction;

import java.util.List;

public interface CrossoverOperator {
    void applyCrossover(List<ProgramVariant> temporalInstances, int generation, FitnessFunction fitnessFunction
    );
}
