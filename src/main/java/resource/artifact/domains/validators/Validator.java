package resource.artifact.domains.validators;

public interface Validator<T> {
    void validate(T Entity) throws ValidationException;
}

