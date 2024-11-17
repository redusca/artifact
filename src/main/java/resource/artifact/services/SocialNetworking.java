package resource.artifact.services;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.IDfromStringValidator;
import resource.artifact.repositories.inMemory.InMemoryRepository;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.events.UserOrFriendShipChangeEvent;
import resource.artifact.utils.observers.Observable;
import resource.artifact.utils.observers.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SocialNetworking implements Observable<UserOrFriendShipChangeEvent>{
    private final InMemoryRepository<Long, User> usersRepo;
    private final InMemoryRepository<Tuple<Long,Long>,Friendship> friendshipsRepo;
    private final IDfromStringValidator iDfromStringValidator;
    private final List<Observer<UserOrFriendShipChangeEvent>> observers=new ArrayList<>();

    public SocialNetworking(InMemoryRepository<Long, User> usersRepo, InMemoryRepository<Tuple<Long, Long>, Friendship> friendshipsRepo) {
        this.usersRepo = usersRepo;
        this.friendshipsRepo = friendshipsRepo;
        iDfromStringValidator = new IDfromStringValidator();
    }
    //Users operations
    //1

    /**
     * Creation and Adding of a user to the repository
     * @param nume : first name of user
     * @param prenume : last name of user
     * @throws resource.artifact.domains.validators.ValidationException : if the names are faulty values like null or empty strings
     * @return {@code Optional} :null if the user was added
     */
    public Optional<User> add_user(String nume, String prenume){
        User saveValue = new User(nume,prenume);
        Optional<User> returnValue = usersRepo.save(saveValue);
        notifyObservers(new UserOrFriendShipChangeEvent(ChangeEvent.ADD,saveValue));
        return returnValue;
    }
    //2

    /**
     * Delete a user and all of his friendships with other users
     * @param id : user id string format
     * @throws resource.artifact.domains.validators.ValidationException if the id is incorrect
     * @return  {@code Optional} : deleted user or nothing if the id doesn't appear in repository
     */
    public Optional<User> delete_user(String id){
        iDfromStringValidator.validate(id);
        Optional<User> delValue = usersRepo.findOne(Long.parseLong(id));

        //Deleting all friendships
        delValue.ifPresent(user -> user.getFriends().forEach(id_friend -> {
            usersRepo.findOne(id_friend).orElse(new User()).removeFriend(user.getId());
            friendshipsRepo.delete(new Friendship(id_friend,delValue.get().getId()).getId());
        }));
        notifyObservers(new UserOrFriendShipChangeEvent(ChangeEvent.DELETE,delValue.orElse(new User())));

        return usersRepo.delete(delValue.orElse(new User()).getId());
    }
    //3

    /**
     * Update the user at id with a new first and last name
     * @param id  id of the updated user.
     * @param nume  new value of first name. If given no value it will keep the old value
     * @param prenume  new value of last name. If given no value it will keep the old value
     * @throws resource.artifact.domains.validators.ValidationException if the id or names are find by the validator incorrect
     * @return {@code Optional} of old Value is updated or null if the id is not found in the repository
     */
    public Optional<User> update_user(String id,String nume ,String prenume){
        iDfromStringValidator.validate(id);

        Optional<User> oldValue = usersRepo.findOne(Long.parseLong(id));

        if(oldValue.isEmpty())
            return Optional.empty();

        User newValue = new User(nume,prenume);
        newValue.setId(Long.parseLong(id));

        return usersRepo.update(newValue);
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
    public void addObserver(Observer<UserOrFriendShipChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UserOrFriendShipChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(UserOrFriendShipChangeEvent event) {
        observers.forEach(observer->observer.update(event));
    }
}
