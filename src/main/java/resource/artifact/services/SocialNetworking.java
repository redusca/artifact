package resource.artifact.services;

import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.repositories.inMemory.InMemoryRepository;

import java.util.Optional;

/*
public class SocialNetworking {
    private final InMemoryRepository<Long, User> usersRepo;
    private final InMemoryRepository<Tuple<Long,Long>,Friendship> friendshipsRepo;

    public SocialNetworking(InMemoryRepository<Long, User> usersRepo, InMemoryRepository<Tuple<Long, Long>, Friendship> friendshipsRepo) {
        this.usersRepo = usersRepo;
        this.friendshipsRepo = friendshipsRepo;
    }
    //Users operations
    //1
    public Optional<User> add_user(String nume, String prenume){
        return usersRepo.save(new User(nume,prenume));
    }
    //2
    public User delete_user(){

    }
    //3
    public User update_user(){

    }
    //4
    public Iterable<User> getAllUsers(){
        return usersRepo.findAll();
    }

    //FriendShips operations
    //1
    public Friendship add_friendship(){

    }
    //2
    public Friendship del_friendship(){

    }
    //3
    public Friendship update_friendship(){

    }
    //4
    public Iterable<Friendship> getAllFriendships(){
        return friendshipsRepo.findAll();
    }
}
*/