package resource.artifact.domains.validator;

public interface Validator<T> {
    void validate(T Entity) throws ValidationException;
}

