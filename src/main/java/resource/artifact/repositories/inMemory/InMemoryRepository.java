package resource.artifact.repositories.inMemory;

import resource.artifact.domains.Entity;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.Repository;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<Id,E extends Entity<Id>> implements Repository<Id, E> {

    private final Validator<E> validator;
    protected Map<Id, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(Id id) {
        if(id == null)
            throw new IllegalArgumentException("id is null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null");
        if(entities.get(entity.getId()) != null)
            throw new IllegalArgumentException("entity already exists");
        validator.validate(entity);
        return Optional.ofNullable(entities.put(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(Id id) {
        if(id == null)
            throw new IllegalArgumentException("id is null");
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity is null");
        validator.validate(entity);
        return Optional.ofNullable(entities.replace(entity.getId(), entity));
    }
}
