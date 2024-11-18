package resource.artifact.services;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.IDfromStringValidator;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.repositories.inMemory.InMemoryRepository;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.observers.Observable;
import resource.artifact.utils.observers.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Long.parseLong;


public class SocialNetworking implements Observable<AccountEvent>{
    private final InMemoryRepository<Long, User> usersRepo;
    private final InMemoryRepository<Tuple<Long,Long>,Friendship> friendshipsRepo;
    private final IDfromStringValidator iDfromStringValidator;
    private final List<Observer<AccountEvent>> observers=new ArrayList<>();

    public SocialNetworking(InMemoryRepository<Long, User> usersRepo, InMemoryRepository<Tuple<Long, Long>, Friendship> friendshipsRepo) {
        this.usersRepo = usersRepo;
        this.friendshipsRepo = friendshipsRepo;
        iDfromStringValidator = new IDfromStringValidator();
    }
    //Users operations
    //1

    /**
     * Creation and Adding of a user to the repository
     * @throws resource.artifact.domains.validators.ValidationException : if the names are faulty values like null or empty strings or the username is already taken
     * @return {@code Optional} :null if the user was added
     */
    public Optional<User> add_user(String username,String password,String nume, String prenume){

        User saveValue = new User(nume,prenume,password,username);
        if(find_by_Username_User(username).isPresent())
            throw new ValidationException("Username already exists!");

        usersRepo.save(saveValue);
        notifyObservers(new AccountEvent(ChangeEvent.ADD,find_last_user_added().orElse(new User())));

        return find_last_user_added();
    }
    //2

    /**
     * Delete a user and all of his friendships with other users
     * @throws resource.artifact.domains.validators.ValidationException if the id is incorrect
     * @return  {@code Optional} : deleted user or nothing if the id doesn't appear in repository
     */
    public Optional<User> delete_user(String id){
        iDfromStringValidator.validate(id);
        Optional<User> delValue = usersRepo.findOne(Long.parseLong(id));

        //Deleting all friendships
        delValue.ifPresent(user -> user.getFriends().forEach(id_friend -> {
            usersRepo.findOne(id_friend).orElse(new User()).removeFriend(user.getId());
            friendshipsRepo.delete(new Friendship(id_friend, delValue.get().getId()).getId());
        }));

        notifyObservers(new AccountEvent(ChangeEvent.DELETE,delValue.orElse(new User())));

        return usersRepo.delete(parseLong(id));
    }
    //3

    /**
     * Update the user at id with a new first and last name
     * @param id  id of the updated user.
     * @throws resource.artifact.domains.validators.ValidationException if the id or names are find by the validator incorrect
     * @return {@code Optional} of old Value is updated or null if the id is not found in the repository
     */
    public Optional<User> update_user(String id,String nume ,String prenume,String password,String username){
        iDfromStringValidator.validate(id);

        Optional<User> oldValue = usersRepo.findOne(Long.parseLong(id));

        if(oldValue.isEmpty())
            return Optional.empty();

        User newValue = new User(nume,prenume,password,username);
        newValue.setId(Long.parseLong(id));

        notifyObservers(new AccountEvent(ChangeEvent.UPDATE,newValue,oldValue.orElse(new User())));

        return usersRepo.update(newValue);
    }

    public Optional<User> find_last_user_added(){
        return usersRepo.findLast();
    }

    /**
     * Add a friendship between 2 existing users in repository
     * @param id_low id of first user
     * @param id_high id of second user
     * @throws resource.artifact.domains.validators.ValidationException if the ids don't match an existing id in the repository
     * @throws IllegalArgumentException if the friendship already exist
     * @return {@code Optional} null if it was added
     */
    public Optional<Friendship> add_friendship(String id_low, String id_high){
        iDfromStringValidator.validate(id_low);
        iDfromStringValidator.validate(id_high);

        //adding the friendship in the users lists
        usersRepo.findOne(Long.parseLong(id_low)).orElse(new User()).addFriend(Long.parseLong(id_high));
        usersRepo.findOne(Long.parseLong(id_high)).orElse(new User()).addFriend(Long.parseLong(id_low));

        return friendshipsRepo.save(new Friendship(Long.parseLong(id_low),Long.parseLong(id_high)));
    }
    //2

    /**
     * Delete a friendship between 2 users in repository
     * @param id_low id of first user
     * @param id_high id of last user
     * @throws resource.artifact.domains.validators.ValidationException if the ids don't match a number format
     * @return {@code Optional} the delete value or null if the pair of ids doesn't exist in repository
     */
    public Optional<Friendship> del_friendship(String id_low,String id_high){
        iDfromStringValidator.validate(id_low);
        iDfromStringValidator.validate(id_high);

        Optional<Friendship> delValue = friendshipsRepo.delete(
                new Friendship(Long.parseLong(id_low),Long.parseLong(id_high)).getId());

        //remove from both users the friendship
        if(delValue.isPresent()) {
            usersRepo.findOne(Long.parseLong(id_low)).orElse(new User()).removeFriend(Long.parseLong(id_high));
            usersRepo.findOne(Long.parseLong(id_high)).orElse(new User()).removeFriend(Long.parseLong(id_low));
        }

        notifyObservers(new AccountEvent(ChangeEvent.REMOVED_FRIENDSHIP,delValue.orElse(new Friendship(0L,0L))));
        return delValue;
    }

    //4
    public Iterable<User> get_all_users(){
        return usersRepo.findAll();
    }

    //3
    public Iterable<Friendship> get_all_friendships(){
        return friendshipsRepo.findAll();
    }

    @Override
    public void addObserver(Observer<AccountEvent> eventObserver) {
        observers.add(eventObserver);
    }

    @Override
    public void removeObserver(Observer<AccountEvent> eventObserver) {
        observers.remove(eventObserver);
    }

    @Override
    public void notifyObservers(AccountEvent event) {
        observers.forEach(observer->observer.update(event));
    }

    public Optional<User> find_user(Long id) {
        return usersRepo.findOne(id);
    }

    /**
     * Find a user by his username
     * @return {@code Optional} of user or null if username doesn't exist
     */
    public Optional<User> find_by_Username_User(String username){

        for(User user : get_all_users()){
            if(Objects.equals(user.getUsername(), username))
                return Optional.of(user);
        }
        return Optional.empty();
    }

}
