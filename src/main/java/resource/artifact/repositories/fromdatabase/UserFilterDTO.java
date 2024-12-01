package resource.artifact.repositories.fromdatabase;

import resource.artifact.domains.User;

import java.util.ArrayList;
import java.util.List;

public class UserFilterDTO {
    private String name;
    private final List<Long> users;

    public UserFilterDTO(List<Long> users) {
        name ="";
        this.users = users;
    }

    public UserFilterDTO(List<Long> users,String name) {
        this.name = name;
        this.users = users;
    }

    public String getFirstName() {
        if(name.contains(" ")){
            return name.split(" ")[0];
        }
        return name;
    }

    public String getLastName() {
       if(name.contains(" ")){
            return name.split(" ")[1];
        }
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getUsers() {
        return users;
    }
}
