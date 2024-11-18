package resource.artifact.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String username;
    private final List<Long> friends;

    public User(String firstName, String lastName, String password, String username) {
        this.password = password;
        this.username = username;
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
        if(friends.contains(aLong))
            return;
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
        return firstName.equals(user.getFirstName()) && lastName.equals(user.getLastName()) &&
                password.equals(user.getPassword()) && username.equals(user.getUsername());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
