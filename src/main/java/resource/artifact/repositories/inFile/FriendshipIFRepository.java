package resource.artifact.repositories.inFile;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.IDfromStringValidator;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.Repository;
import resource.artifact.utils.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Optional;

public class FriendshipIFRepository extends AbstractInFileRepository<Tuple<Long,Long>, Friendship> {
    private final Repository<Long,User> usersRepo;
    public FriendshipIFRepository(Validator<Friendship> validator, Repository<Long, User> usersRepo, String filename) {
        super(validator, filename);
        this.usersRepo = usersRepo;
        postLoad();
    }

    private void postLoad() {
        findAll().forEach(friendship -> {
            usersRepo.findOne(friendship.getFirst()).orElse(new User()).addFriend(
                    usersRepo.findOne(friendship.getLast()).orElse(new User()).getId());
            usersRepo.findOne(friendship.getLast()).orElse(new User()).addFriend(
                    usersRepo.findOne(friendship.getFirst()).orElse(new User()).getId());
        });
    }

    @Override
    public Friendship createEntity(String line) {
        String[] friendshipValues = line.split(";");

        if(friendshipValues.length != 3)
            throw new IllegalArgumentException("The format is invalid for Friendship!");

        IDfromStringValidator IDValidator= new IDfromStringValidator();
        IDValidator.validate(friendshipValues[0]);
        IDValidator.validate(friendshipValues[1]);

        LocalDateTime date = LocalDateTime.parse(friendshipValues[2], DateTimeFormat.DATE_TIME_FORMATTER);

        return new Friendship(Long.parseLong(friendshipValues[0]),Long.parseLong(friendshipValues[1]),date);
    }

    @Override
    public String saveEntity(Friendship entity) {
        return entity.getFirst() + ";" + entity.getLast() + ";" + entity.getFDate();
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        usersRepo.findOne(entity.getFirst()).orElse(new User()).addFriend(
                usersRepo.findOne(entity.getLast()).orElse(new User()).getId());
        usersRepo.findOne(entity.getLast()).orElse(new User()).addFriend(
                usersRepo.findOne(entity.getFirst()).orElse(new User()).getId());

        return super.save(entity);
    }
}
