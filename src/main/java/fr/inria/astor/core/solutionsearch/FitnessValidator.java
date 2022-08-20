package fr.inria.astor.core.solutionsearch;

import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.validation.VariantValidationResult;

import java.net.MalformedURLException;

public interface FitnessValidator {

    VariantValidationResult validateInstance(ProgramVariant variant);
}
