package resource.artifact.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private Long id;
    private String firstName;
    private String lastName;
    private final List<Long> friends;

    public User(String firstName,String lastName) {
        friends = new ArrayList<>();
        setId(0L);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
        setId(0L);
        friends = new ArrayList<>();
    }

    //names
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    //Friends list
    public List<Long> getFriends() {
        return friends;
    }
    public void addFriend(Long aLong) {
        friends.add(aLong);
    }
    public void removeFriend(Long aLong) {
        friends.remove(aLong);
    }
    public void emptyFriends(){
        friends.clear();
    }

    //override
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof User user)) return false;
        return firstName.equals(user.getFirstName()) && lastName.equals(user.getLastName());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}
