package resource.artifact.repositories.inFile;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.IDfromStringValidator;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.Repository;

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
            usersRepo.findOne(friendship.first()).get().addFriend(
                    usersRepo.findOne(friendship.last()).get().getId());
            usersRepo.findOne(friendship.last()).get().addFriend(
                    usersRepo.findOne(friendship.first()).get().getId());
        });
    }

    @Override
    public Friendship createEntity(String line) {
        String[] friendshipValues = line.split(";");

        IDfromStringValidator IDValidator= new IDfromStringValidator();
        IDValidator.validate(friendshipValues[0]);
        IDValidator.validate(friendshipValues[1]);

        return new Friendship(Long.parseLong(friendshipValues[0]),Long.parseLong(friendshipValues[1]));
    }

    @Override
    public String saveEntity(Friendship entity) {
        return entity.first() + ";" + entity.last();
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        usersRepo.findOne(entity.first()).get().addFriend(
                usersRepo.findOne(entity.last()).get().getId());
        usersRepo.findOne(entity.last()).get().addFriend(
                usersRepo.findOne(entity.first()).get().getId());

        return super.save(entity);
    }
}
