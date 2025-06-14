package digiHisab.user.service;

import digiHisab.user.model.User;
import digiHisab.user.repository.UserRepository;
import digiHisab.user.request.AddUserRequest;
import digiHisab.user.response.UserResponse;
import digiHisab.utility.exceptions.NotFoundException;
import digiHisab.utility.response.PaginatedApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor( onConstructor_ = {@Autowired} )
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse addUser( AddUserRequest addUserRequest ) {
        User user = new User( addUserRequest );
        user.setPassword( passwordEncoder.encode( addUserRequest.getPassword() ) );
        user = userRepository.save( user );
        return new UserResponse( user );
    }

    public PaginatedApiResponse<List<UserResponse>> getUsers( Pageable pageable ) {

        Page<User> users = userRepository.findAll( pageable );
        List<UserResponse> userResponses = users.getContent().stream().map( UserResponse::new ).toList();

        return new PaginatedApiResponse<>(
                userResponses, pageable.getPageNumber(),
                users.getTotalPages(), users.getTotalElements()
        );
    }

    public UserResponse getUser( Long id ) {
        User user = userRepository.findById( id )
                .orElseThrow( () -> new NotFoundException( "User", id ) );

        return new UserResponse( user );
    }

    public void updateUser(Long id, AddUserRequest request) {
    }
}
