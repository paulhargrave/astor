package fr.inria.astor.approaches.jgenprog;

import fr.inria.astor.core.entities.ProgramVariant;

import java.util.List;

public interface CrossoverOperator {
    void applyCrossover(List<ProgramVariant> temporalInstances,  int generation);
}
