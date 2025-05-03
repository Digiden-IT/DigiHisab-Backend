package dev.jahid.user_auth_db_config_boilerplate.user.controller;

import dev.jahid.user_auth_db_config_boilerplate.user.request.AddUserRequest;
import dev.jahid.user_auth_db_config_boilerplate.user.response.UserResponse;
import dev.jahid.user_auth_db_config_boilerplate.user.service.UserService;
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
