package fr.inria.astor.approaches.jgenprog;

import com.google.common.collect.ImmutableMap;
import com.gzoltar.sfl.formulas.Opt;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.validation.VariantValidationResult;
import fr.inria.astor.core.setup.RandomManager;
import fr.inria.astor.core.solutionsearch.AstorCoreEngine;
import fr.inria.astor.core.solutionsearch.FitnessValidator;
import fr.inria.astor.core.solutionsearch.population.FitnessFunction;
import org.apache.log4j.Logger;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class SelectiveCrossover implements CrossoverOperator {

    private static Logger log = Logger.getLogger(AstorCoreEngine.class.getSimpleName());

    @Override
    public void applyCrossover(ProgramVariant left, ProgramVariant right, int generationToApplyCrossover, FitnessFunction fitnessFunction, FitnessValidator fitnessValidator) {
        if (CrossoverHelperMethods.sanityCheckFailed(left, right, log)) {
            return;
        }

        Optional<MinMaxFitness> minMaxLeft = findMinMax(left, fitnessFunction, fitnessValidator);
        Optional<MinMaxFitness> minMaxRight = findMinMax(right, fitnessFunction, fitnessValidator);

        //only do a crossover if there are modification points in each variant
        if(minMaxLeft.isPresent() && minMaxRight.isPresent()) {
            List<OperatorInstance> ops1 = left.getOperations(minMaxLeft.get().minFitnessGeneration);
            List<OperatorInstance> ops2 = right.getOperations(minMaxRight.get().minFitnessGeneration);

            OperatorInstance opinst1 = ops1.remove((int) RandomManager.nextInt(ops1.size()));
            OperatorInstance opinst2 = ops2.remove((int) RandomManager.nextInt(ops2.size()));

            if (opinst1 == null || opinst2 == null) {
                log.debug("CO|We could not retrieve a operator");
                return;
            }

            // The generation of both new operators is the Last one.
            // In the first variant we put the operator taken from the 2 one.
            left.putModificationInstance(minMaxLeft.get().maxFitnessGeneration, opinst2);
            // In the second variant we put the operator taken from the 1 one.
            right.putModificationInstance(minMaxRight.get().maxFitnessGeneration, opinst1);
        }


    }

    /**
     * Find the min and max fitness for any program modification points being removed. Optional since there may not
     * be any modification points.
     * @param programVariant
     * @param fitnessFunction
     * @param fitnessValidator
     * @return
     */
    private Optional<MinMaxFitness> findMinMax(ProgramVariant programVariant, FitnessFunction fitnessFunction, FitnessValidator fitnessValidator) {

        Optional<Integer> generationMin = Optional.empty();
        Optional<Integer> generationMax = Optional.empty();
        Optional<Double> minScore = Optional.empty();
        Optional<Double> maxScore = Optional.empty();


        Map<Integer, List<OperatorInstance>> filteredOperations = CrossoverHelperMethods.filterOutEmptyOperations(programVariant.getOperations());
        for (Integer generation : filteredOperations.keySet()) {
            double fitness = fitnessWithGenerationRemoved(generation, programVariant, fitnessFunction, fitnessValidator);
            if (!generationMin.isPresent()) {

                generationMin = Optional.of(generation);
                generationMax = Optional.of(generation);
                minScore = Optional.of(fitness);
                maxScore = Optional.of(fitness);
            }
            if (fitness < minScore.get()) {
                minScore = Optional.of(fitness);
                generationMin = Optional.of(generation);
            }
            if (fitness > maxScore.get()) {
                maxScore = Optional.of(fitness);
                generationMax = Optional.of(generation);
            }
        }
        if(generationMax.isPresent()) {
            return Optional.of(new MinMaxFitness(generationMin.get(), generationMax.get()));
        }
        return Optional.empty();

    }


    private double fitnessWithGenerationRemoved(Integer generation, ProgramVariant programVariant, FitnessFunction fitnessFunction, FitnessValidator fitnessValidator) {

        List<OperatorInstance> operatorInstancesForGenerationRemoved = programVariant.removeOperationForGeneration(generation);

        VariantValidationResult variantValidationResult = fitnessValidator.validateInstance(programVariant);
        double fitnessValue = fitnessFunction.calculateFitnessValue(variantValidationResult);
        operatorInstancesForGenerationRemoved
                .forEach(operatorInstance -> programVariant.putModificationInstance(generation, operatorInstance));
        return fitnessValue;
    }


    private class MinMaxFitness {
        private int minFitnessGeneration;
        private int maxFitnessGeneration;

        MinMaxFitness(int minFitnessGeneration, int maxFitnessGeneration) {
            this.minFitnessGeneration = minFitnessGeneration;
            this.maxFitnessGeneration = maxFitnessGeneration;
        }
    }


}
