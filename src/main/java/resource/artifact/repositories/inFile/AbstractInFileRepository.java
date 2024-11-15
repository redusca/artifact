package resource.artifact.repositories.inFile;

import resource.artifact.domains.Entity;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.inMemory.InMemoryRepository;

import java.io.*;
import java.util.Optional;

public abstract class AbstractInFileRepository<Id,E extends Entity<Id>> extends InMemoryRepository<Id,E> {
    private final String fileName;

    public AbstractInFileRepository(Validator<E> validator,String filename) {
        super(validator);
        this.fileName = filename;
        loadDataFromFile();
    }

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line= reader.readLine())!= null){
                E entity = createEntity(line);
                super.save(entity);
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    private void writeDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : entities.values()) {
                String line = saveEntity(entity);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public abstract E createEntity(String line);

    public abstract String saveEntity(E entity);

    @Override
    public Optional<E> findOne(Id id) {
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> newEntity = super.save(entity);
        if(newEntity.isEmpty())
            writeDataToFile();
        return newEntity;
    }


    @Override
    public Optional<E> delete(Id id) {
        Optional<E> delValue = super.delete(id);
        if(delValue.isPresent()){
            writeDataToFile();
        }
        return delValue;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> oldValue = super.update(entity);
        if(oldValue.isPresent()){
            writeDataToFile();
        }
        return oldValue;
    }
}
