package digiHisab.user;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN( "Admin" ),
    EMPLOYEE( "Employee" );

    final String value;

    Role( String value ) {
        this.value = value;
    }
}
