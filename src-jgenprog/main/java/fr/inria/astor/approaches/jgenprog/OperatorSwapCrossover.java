package fr.inria.astor.approaches.jgenprog;

import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.setup.RandomManager;
import fr.inria.astor.core.solutionsearch.AstorCoreEngine;
import org.apache.log4j.Logger;

import java.util.List;

public class OperatorSwapCrossover implements CrossoverOperator {

    private static Logger log = Logger.getLogger(AstorCoreEngine.class.getSimpleName());

    @Override
    public void applyCrossover(List<ProgramVariant> variants, int generation) {


        int numberVariants = variants.size();
        if (numberVariants <= 1) {
            log.debug("CO|Not Enough variants to apply Crossover");
            return;
        }
ß
        // We randomly choose the two variants to crossover
        ProgramVariant v1 = variants.get(RandomManager.nextInt(numberVariants));
        ProgramVariant v2 = variants.get(RandomManager.nextInt(numberVariants));
        // Same instance
        if (v1 == v2) {
            log.debug("CO|randomless chosen the same variant to apply crossover");
            return;
        }

        if (v1.getOperations().isEmpty() || v2.getOperations().isEmpty()) {
            log.debug("CO|Not Enough ops to apply Crossover");
            return;
        }
        // we randomly select the generations to apply
        int rgen1index = RandomManager.nextInt(v1.getOperations().keySet().size()) + 1;
        int rgen2index = RandomManager.nextInt(v2.getOperations().keySet().size()) + 1;

        List<OperatorInstance> ops1 = v1.getOperations((int) v1.getOperations().keySet().toArray()[rgen1index]);
        List<OperatorInstance> ops2 = v2.getOperations((int) v2.getOperations().keySet().toArray()[rgen2index]);

        OperatorInstance opinst1 = ops1.remove((int) RandomManager.nextInt(ops1.size()));
        OperatorInstance opinst2 = ops2.remove((int) RandomManager.nextInt(ops2.size()));

        if (opinst1 == null || opinst2 == null) {
            log.debug("CO|We could not retrieve a operator");
            return;
        }

        // The generation of both new operators is the Last one.
        // In the first variant we put the operator taken from the 2 one.
        v1.putModificationInstance(generation, opinst2);
        // In the second variant we put the operator taken from the 1 one.
        v2.putModificationInstance(generation, opinst1);
        //
    }
}
