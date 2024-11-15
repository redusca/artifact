package resource.artifact.domains.validators;

public class IDfromStringValidator implements Validator<String>{

    @Override
    public void validate(String Entity) throws ValidationException {
        try {
            Long ValidateString = Long.parseLong(Entity);
        }catch (NumberFormatException e) {
            throw new ValidationException("The ID format is incorrect, should be positive Integer!");
        }
    }
}
