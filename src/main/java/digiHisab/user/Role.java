package digiHisab.user;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN( "admin" ),
    USER( "user" );

    final String value;

    Role( String value ) {
        this.value = value;
    }
}
