package fr.inria.astor.approaches.jgenprog;

import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.setup.RandomManager;
import fr.inria.astor.core.solutionsearch.AstorCoreEngine;
import fr.inria.astor.core.solutionsearch.population.FitnessFunction;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperatorSwapCrossover implements CrossoverOperator {

    private static Logger log = Logger.getLogger(AstorCoreEngine.class.getSimpleName());

    @Override
    public void applyCrossover(Pair variants, int generation, FitnessFunction fitnessFunction) {



        // Same instance
        if (variants.left() == variants.right()) {
            log.debug("CO|randomless chosen the same variant to apply crossover");
            return;
        }

        if (variants.left().getOperations().isEmpty() || variants.right().getOperations().isEmpty()) {
            log.debug("CO|Not Enough ops to apply Crossover");
            return;
        }
        Map<Integer, List<OperatorInstance>> v1FilteredOperations = filterOutEmptyOperations(variants.left().getOperations());
        Map<Integer, List<OperatorInstance>> v2FilteredOperations = filterOutEmptyOperations(variants.right().getOperations());


        // we randomly select the generations to apply
        //removed +1 since throws exception where number operations is 1
        int rgen1index = RandomManager.nextInt(v1FilteredOperations.keySet().size());
        int rgen2index = RandomManager.nextInt(v2FilteredOperations.keySet().size());

        List<OperatorInstance> ops1 = variants.left().getOperations((int) v1FilteredOperations.keySet().toArray()[rgen1index]);
        List<OperatorInstance> ops2 = variants.right().getOperations((int) v2FilteredOperations.keySet().toArray()[rgen2index]);


        OperatorInstance opinst1 = ops1.remove((int) RandomManager.nextInt(ops1.size()));
        OperatorInstance opinst2 = ops2.remove((int) RandomManager.nextInt(ops2.size()));

        if (opinst1 == null || opinst2 == null) {
            log.debug("CO|We could not retrieve a operator");
            return;
        }

        // The generation of both new operators is the Last one.
        // In the first variant we put the operator taken from the 2 one.
        variants.left().putModificationInstance(generation, opinst2);
        // In the second variant we put the operator taken from the 1 one.
        variants.right().putModificationInstance(generation, opinst1);
        //
    }




    private Map<Integer, List<OperatorInstance>> filterOutEmptyOperations(Map<Integer, List<OperatorInstance>> v1Operations) {
        return v1Operations.entrySet().stream()
                .filter(e -> e.getValue().size() > 0)
                .collect(Collectors.toUnmodifiableMap(e -> e.getKey(), e -> e.getValue()));
    }
}
