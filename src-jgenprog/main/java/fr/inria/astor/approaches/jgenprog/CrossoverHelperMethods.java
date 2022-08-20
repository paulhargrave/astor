package fr.inria.astor.approaches.jgenprog;

import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class CrossoverHelperMethods {

    static boolean sanityCheckFailed(ProgramVariant left, ProgramVariant right, Logger log) {
        // Same instance
        if (left == right) {
            log.debug("CO|randomless chosen the same variant to apply crossover");
            return true;
        }

        if (left.getOperations().isEmpty() || right.getOperations().isEmpty()) {
            log.debug("CO|Not Enough ops to apply Crossover");
            return true;
        }

        return false;
    }

    static Map<Integer, List<OperatorInstance>> filterOutEmptyOperations(Map<Integer, List<OperatorInstance>> v1Operations) {
        return v1Operations.entrySet().stream()
                .filter(e -> e.getValue().size() > 0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }


}
