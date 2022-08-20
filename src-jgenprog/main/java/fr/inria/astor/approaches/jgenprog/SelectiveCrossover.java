package fr.inria.astor.approaches.jgenprog;

import com.google.common.collect.ImmutableMap;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.solutionsearch.AstorCoreEngine;
import fr.inria.astor.core.solutionsearch.FitnessValidator;
import fr.inria.astor.core.solutionsearch.population.FitnessFunction;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class SelectiveCrossover implements CrossoverOperator {

    private static Logger log = Logger.getLogger(AstorCoreEngine.class.getSimpleName());

    @Override
    public void applyCrossover(ProgramVariant left, ProgramVariant right, int generationToApplyCrossover, FitnessFunction fitnessFunction, FitnessValidator fitnessValidator) {
        if (CrossoverHelperMethods.sanityCheckFailed(left, right, log)) {
            return;
        }

        Map<Integer, List<OperatorInstance>> v1FilteredOperations = CrossoverHelperMethods.filterOutEmptyOperations(left.getOperations());
        Map<Integer, List<OperatorInstance>> v2FilteredOperations = CrossoverHelperMethods.filterOutEmptyOperations(right.getOperations());


        //find the most and least "relevent" operations in each of the pair
        for (Integer generation : v1FilteredOperations.keySet()) {
            List<OperatorInstance> operationsLeft = left.getOperations((int) v1FilteredOperations.keySet().toArray()[generation]);
            List<OperatorInstance> operationsRight = right.getOperations((int) v1FilteredOperations.keySet().toArray()[generation]);




            //MinMaxFitness minMaxLeft = minMaxForGeneration(operationsLeft, variants.left(), fitnessFunction);
        }


    }

    private MinMaxFitness minMaxForGeneration(Integer generation, Map<Integer, List<OperatorInstance>> filteredOperations, FitnessFunction fitnessFunction) {

        ProgramVariant variantWithOperationRemoved = new ProgramVariant();

        for (Integer generationInOperations : filteredOperations.keySet()) {
            if (generationInOperations != generation) {
                variantWithOperationRemoved.addModificationPoints(null);
            }
        }

        Map<Integer, List<OperatorInstance>> operationsWithoutGeneration = variantWithOperationRemoved.getOperations()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != generation)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //variantWithOperationRemoved.setOp
        return null;
    }


    private class MinMaxFitness {
        MinMaxFitness(double min, double max) {}
    }
}
