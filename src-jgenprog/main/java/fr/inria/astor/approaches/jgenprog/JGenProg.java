package fr.inria.astor.approaches.jgenprog;

import java.util.List;

import com.martiansoftware.jsap.JSAPException;

import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.ingredientbased.IngredientBasedEvolutionaryRepairApproachImpl;
import fr.inria.astor.core.manipulation.MutationSupporter;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.core.setup.ProjectRepairFacade;
import fr.inria.astor.core.setup.RandomManager;
import fr.inria.main.evolution.ExtensionPoints;

/**
 * Core repair approach based on reuse of ingredients.
 * 
 * @author Matias Martinez, matias.martinez@inria.fr
 * 
 */
public class JGenProg extends IngredientBasedEvolutionaryRepairApproachImpl {


	private CrossoverOperator crossoverOperator;

	public JGenProg(MutationSupporter mutatorExecutor, ProjectRepairFacade projFacade) throws JSAPException {
		super(mutatorExecutor, projFacade);
		crossoverOperator = new OperatorSwapCrossover();
		setPropertyIfNotDefined(ExtensionPoints.OPERATORS_SPACE.identifier, "irr-statements");

		setPropertyIfNotDefined(ExtensionPoints.TARGET_CODE_PROCESSOR.identifier, "statements");
	}

	@Override
	public void prepareNextGeneration(List<ProgramVariant> temporalInstances, int generation) {

		super.prepareNextGeneration(temporalInstances, generation);

		if (ConfigurationProperties.getPropertyBool("applyCrossover")) {
			applyCrossover(generation);
		}
	}

	private void applyCrossover(int generation) {
		int numberVariants = variants.size();
		if (numberVariants <= 1) {
			log.debug("CO|Not Enough variants to apply Crossover");
			return;
		}
		// We randomly choose the two variants to crossover
		ProgramVariant v1 = variants.get(RandomManager.nextInt(numberVariants));
		ProgramVariant v2 = variants.get(RandomManager.nextInt(numberVariants));
		crossoverOperator.applyCrossover(new CrossoverOperator.Pair(v1, v2), generation, fitnessFunction);
	}

	@Override
	public void loadOperatorSpaceDefinition() throws Exception {

		super.loadOperatorSpaceDefinition();

		if (this.getOperatorSpace() == null) {

			this.setOperatorSpace(new jGenProgSpace());
		}

	}

}
