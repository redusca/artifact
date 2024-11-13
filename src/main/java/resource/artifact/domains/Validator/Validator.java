package resource.artifact.domains.Validator;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
