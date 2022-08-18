package fr.inria.astor.approaches.jgenprog;

import fr.inria.astor.core.entities.OperatorInstance;

import java.util.List;

interface OperatorSwapInternal {

    Pair swap(List<OperatorInstance> leftOps, List<OperatorInstance> rightOps);

    record Pair (OperatorInstance leftOp, OperatorInstance rightOp) {}
}
