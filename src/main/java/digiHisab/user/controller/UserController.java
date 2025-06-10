package digiHisab.user.controller;

import digiHisab.user.request.AddUserRequest;
import digiHisab.user.response.UserResponse;
import digiHisab.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping( "/users" )
@RequiredArgsConstructor( onConstructor_ = @Autowired )
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> addUser( @RequestBody AddUserRequest request ) {
        UserResponse response = userService.addUser( request );
        return ResponseEntity.ok( response );
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> userResponses = userService.getUsers();
        return ResponseEntity.ok( userResponses );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<UserResponse> getUser( @PathVariable Long id ) {
        UserResponse user = userService.getUser( id );
        return ResponseEntity.ok( user );
    }
}
