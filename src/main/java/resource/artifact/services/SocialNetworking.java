package resource.artifact.services;

import resource.artifact.domains.*;
import resource.artifact.domains.validators.IDfromStringValidator;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.repositories.inMemory.InMemoryRepository;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.observers.Observable;
import resource.artifact.utils.observers.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.max;
import static java.lang.Long.parseLong;
import static java.lang.Math.min;


public class SocialNetworking implements Observable<AccountEvent>{
    private final InMemoryRepository<Long, User> usersRepo;
    private final InMemoryRepository<Tuple<Long,Long>,Friendship> friendshipsRepo;
    private final IDfromStringValidator iDfromStringValidator;
    private final List<Observer<AccountEvent>> observers=new ArrayList<>();
    private final InMemoryRepository<Tuple<Long, Long>, FriendRequest> friendRequestRepo;
    private final InMemoryRepository<Long, Message> mesagesRepo;

    public SocialNetworking(InMemoryRepository<Long, User> usersRepo, InMemoryRepository<Tuple<Long, Long>, Friendship> friendshipsRepo,
    InMemoryRepository<Tuple<Long, Long>, FriendRequest> friendRequestRepo, InMemoryRepository<Long, Message> messagesRepo) {
        this.usersRepo = usersRepo;
        this.friendshipsRepo = friendshipsRepo;
        this.friendRequestRepo = friendRequestRepo;
        this.mesagesRepo = messagesRepo;
        iDfromStringValidator = new IDfromStringValidator();
    }
    //Users operations
    //1

    /**
     * Creation and Adding of a user to the repository
     * @throws ValidationException : if the names are faulty values like null or empty strings or the username is already taken
     * @return {@code Optional} :null if the user was added
     */
    public Optional<User> add_user(String nume, String prenume,String password,String username){

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
     * @throws ValidationException if the id is incorrect
     * @return  {@code Optional} : deleted user or nothing if the id doesn't appear in repository
     */
    public Optional<User> delete_user(String id){
        iDfromStringValidator.validate(id);
        Optional<User> delValue = usersRepo.findOne(parseLong(id));

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
     * @throws ValidationException if the id or names are find by the validator incorrect
     * @return {@code Optional} of old Value is updated or null if the id is not found in the repository
     */
    public Optional<User> update_user(String id,String nume ,String prenume,String password,String username){
        iDfromStringValidator.validate(id);

        Optional<User> oldValue = usersRepo.findOne(parseLong(id));

        if(oldValue.isEmpty())
            return Optional.empty();

        User newValue = new User(nume,prenume,password,username);
        newValue.setId(parseLong(id));

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
     * @throws ValidationException if the ids don't match an existing id in the repository
     * @throws IllegalArgumentException if the friendship already exist
     * @return {@code Optional} null if it was added
     */
    public Optional<Friendship> add_friendship(String id_low, String id_high){
        iDfromStringValidator.validate(id_low);
        iDfromStringValidator.validate(id_high);

        Long id_lowL = parseLong(id_low);
        Long id_highL = parseLong(id_high);

        //adding the friendship in the users lists
        usersRepo.findOne(id_lowL).orElse(new User()).addFriend(id_highL);
        usersRepo.findOne(id_highL).orElse(new User()).addFriend(id_lowL);

        Optional<Friendship> returnValue = friendshipsRepo.save(new Friendship(id_lowL, id_highL));
        if(returnValue.isEmpty())
            notifyObservers(new AccountEvent(ChangeEvent.ADD_FRIENDSHIP,
                    friendshipsRepo.findOne(new Tuple<>(min(id_lowL,id_highL),max(id_lowL,id_highL))).orElse(new Friendship(0L,0L))));

        return returnValue;
    }
    //2

    /**
     * Delete a friendship between 2 users in repository
     * @param id_low id of first user
     * @param id_high id of last user
     * @throws ValidationException if the ids don't match a number format
     * @return {@code Optional} the delete value or null if the pair of ids doesn't exist in repository
     */
    public Optional<Friendship> del_friendship(String id_low,String id_high){
        iDfromStringValidator.validate(id_low);
        iDfromStringValidator.validate(id_high);

        Optional<Friendship> delValue = friendshipsRepo.delete(
                new Friendship(parseLong(id_low), parseLong(id_high)).getId());

        //remove from both users the friendship
        if(delValue.isPresent()) {
            usersRepo.findOne(parseLong(id_low)).orElse(new User()).removeFriend(parseLong(id_high));
            usersRepo.findOne(parseLong(id_high)).orElse(new User()).removeFriend(parseLong(id_low));
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

    public Iterable<User> get_all_friends_of_User(String id) {
        iDfromStringValidator.validate(id);
        Optional<User> user = usersRepo.findOne(parseLong(id));
        return user.map(u -> u.getFriends().stream()
                        .map(id_friend -> usersRepo.findOne(id_friend).orElse(new User()))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    public List<User> get_non_friend_of_user(User mainUser) {
        List<User> returnList = new ArrayList<>();
        for(User  user: get_all_users()){
            if(user!=mainUser && !mainUser.getFriends().contains(user.getId()))
                returnList.add(user);
        }
        return returnList;
    }

    public boolean send_friendRequest(String id_sender, String id_receiver) {
        iDfromStringValidator.validate(id_sender);
        iDfromStringValidator.validate(id_receiver);

        friendRequestRepo.save(new FriendRequest(parseLong(id_sender),parseLong(id_receiver), LocalDateTime.now()));
        notifyObservers(new AccountEvent(ChangeEvent.SEND_FRIENDREQUEST,
                usersRepo.findOne(parseLong(id_sender)).orElse(new User())));
        return true;
    }

    public List<FriendRequest> get_all_friend_requests(){
        List<FriendRequest> returnList = new ArrayList<>();
        for(FriendRequest friendRequest : friendRequestRepo.findAll())
            returnList.add(friendRequest);
        return returnList;
    }


    public boolean friendRequestExists(String id, String id_friend){
        iDfromStringValidator.validate(id);
        iDfromStringValidator.validate(id_friend);
        return friendRequestRepo.findOne(new Tuple<>(parseLong(id),parseLong(id_friend))).isPresent() ||
                friendRequestRepo.findOne(new Tuple<>(parseLong(id_friend),parseLong(id))).isPresent();
    }

    public List<FriendRequest> get_all_friend_requests_of_user(User user) {
        List<FriendRequest> returnList = new ArrayList<>();
        for(FriendRequest friendRequest : get_all_friend_requests()){
            if(Objects.equals(friendRequest.getReceiver(), user.getId()))
                returnList.add(friendRequest);
        }
        return returnList;
    }

    public void del_friendRequest(String sender, String receiver) {
        iDfromStringValidator.validate(receiver);
        iDfromStringValidator.validate(sender);

        friendRequestRepo.delete(new Tuple<>(parseLong(sender),parseLong(receiver)));
        notifyObservers(new AccountEvent(ChangeEvent.REMOVED_FRIENDREQUEST,
                usersRepo.findOne(parseLong(sender)).orElse(new User())));

    }

    public void update_friendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus();
        friendRequestRepo.update(friendRequest);
    }

    public void add_message(Message msg) {
        mesagesRepo.save(msg);
        notifyObservers(new AccountEvent(ChangeEvent.ADD_MESSAGE,msg));
    }

    public List<Message> get_all_user_messages(User user) {
        List<Message> returnList = new ArrayList<>();
        for(Message message : mesagesRepo.findAll()){
            System.out.println("Message retrieved: " + message.getMessage());
            if(message.getTo().contains(user) || message.getFrom().equals(user))
                returnList.add(message);
        }
        returnList.sort(Comparator.comparing(Message::getDate));
        return returnList;
    }
}
