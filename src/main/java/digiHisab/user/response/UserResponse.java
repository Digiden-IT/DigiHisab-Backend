package digiHisab.user.response;

import digiHisab.user.Role;
import digiHisab.user.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private Long salary;
    private String assignedToProject;
    private String role;

    public UserResponse( User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.salary = user.getSalary();
        this.role = user.getRole().getValue();
    }
}
