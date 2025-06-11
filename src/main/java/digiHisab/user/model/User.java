package digiHisab.user.model;

import digiHisab.user.Role;
import digiHisab.user.request.AddUserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Table(
        name = "users",
        indexes = {
                @Index( name = "idx_phone_number", columnList = "phone_number" )
        },
        uniqueConstraints = {
                @UniqueConstraint( columnNames = "phone_number" )
        }
)
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

    @Column( name = "phone_number" )
    private String phoneNumber;

    @Column( name = "email" )
    private String email;

    @Column( name = "password" )
    private String password;

    @Column( name = "is_active", columnDefinition = "boolean default true" )
    private Boolean isActive;

    @Enumerated( EnumType.STRING )
    @Column( name = "role" )
    private Role role;

    @Column( name = "refresh_token" )
    private String refreshToken;

    public User( AddUserRequest addUserRequest ) {
        this.name = addUserRequest.getName();
        this.phoneNumber = addUserRequest.getPhoneNumber();
        this.email = addUserRequest.getEmail();
        this.role = addUserRequest.getRole();
    }


    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || Hibernate.getClass( this ) != Hibernate.getClass( o ) ) return false;
        User user = (User) o;
        return id != null && Objects.equals( id, user.id );
    }

    @Override
    public int hashCode() {
        if ( this.id == null )
            return System.identityHashCode( this );
        return Objects.hash( this.id );
    }
}
