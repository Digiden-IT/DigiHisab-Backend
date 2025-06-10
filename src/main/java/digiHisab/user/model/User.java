package digiHisab.user.model;

import digiHisab.user.Role;
import digiHisab.user.request.AddUserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table( name = "users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( name = "name")
    private String name;

    @Column( name = "email" )
    private String email;

    @Column( name = "password" )
    private String password;

    @Column( name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive;

    @Enumerated( EnumType.STRING )
    @Column( name = "role" )
    private Role role;

    @Column( name = "refresh_token" )
    private String refreshToken;

    public User( AddUserRequest addUserRequest ) {
        this.name = addUserRequest.getName();
        this.email = addUserRequest.getEmail();
        this.role = addUserRequest.getRole();
    }
}
