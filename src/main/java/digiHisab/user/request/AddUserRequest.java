package digiHisab.user.request;

import digiHisab.user.Role;
import lombok.Data;

@Data
public class AddUserRequest {
    private String name;
    private String password;
    private String email;
    private Role role;
}
