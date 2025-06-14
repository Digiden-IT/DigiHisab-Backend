package digiHisab.auth.request;

import lombok.Data;

@Data
public class LoginRequest {
    public String phoneNumber;
    public String password;
}
