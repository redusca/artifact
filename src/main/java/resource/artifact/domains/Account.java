package resource.artifact.domains;

import java.util.Objects;

public class Account extends Entity<Long> {
    private String username;
    private String password;
    private User user;

    public Account(String username,String password,User user){
        this.username = username;
        this.password = password;
        setId(user.getId());
        this.user = user;
    }

    public Account() {

    }

    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User getUser(){
        return user;
    }

    public String getFirstName(){
        return user.getFirstName();
    }

    public String getLastName(){
        return user.getLastName();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public  void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString(){
        return username;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Account acc)) return false;
        return Objects.equals(username, acc.username) && Objects.equals(user.getId(), acc.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}

