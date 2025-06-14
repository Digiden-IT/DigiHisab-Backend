package digiHisab.user.controller;

import digiHisab.user.request.AddUserRequest;
import digiHisab.user.response.UserResponse;
import digiHisab.user.service.UserService;
import digiHisab.utility.response.ApiResponse;
import digiHisab.utility.response.PaginatedApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping( "/users" )
@RequiredArgsConstructor( onConstructor_ = @Autowired )
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> addUser( @RequestBody AddUserRequest request ) {
        UserResponse response = userService.addUser( request );
        return new ApiResponse<>( "User added successfully", HttpStatus.CREATED.value(), response );
    }

    @GetMapping
    public PaginatedApiResponse<List<UserResponse>> getUsers( Pageable pageable ) {
        return userService.getUsers( pageable );
    }

    @GetMapping( "/{id}" )
    public ApiResponse<UserResponse> getUser( @PathVariable Long id ) {
        UserResponse user = userService.getUser( id );
        return new ApiResponse<>( "User fetched successfully", user );
    }

    @PatchMapping( "/{id}" )
    public ApiResponse<UserResponse> updateUser( @PathVariable Long id, @RequestBody AddUserRequest request ) {
        userService.updateUser( id, request );
        return new ApiResponse<>( "User updated successfully" );
    }
}
