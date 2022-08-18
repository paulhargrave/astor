package fr.inria.astor.approaches.jgenprog.operators;

import fr.inria.astor.core.entities.ProgramVariant;

public interface VariantSelector {

    record Pair (ProgramVariant left, ProgramVariant right) {}
}
