package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.auth.JwtUtil;
import com.pri.petcationbackend.model.ConfirmationToken;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Tag(name = "User")
@RequiredArgsConstructor
@CrossOrigin
public class RegistrationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Log in")
    public ResponseEntity<LoginResponseDto> authenticateUser(@RequestBody @Valid LoginDto loginDto){
        return getLoginResponseDtoResponseEntity(loginDto.getEmail(), loginDto.getPassword());
    }

    private ResponseEntity<LoginResponseDto> getLoginResponseDtoResponseEntity(String mail,  String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                mail, password));

        String email = authentication.getName();
        User user = new User(email,"");
        User user2 = userService.findByEmail(email);
        boolean isHotel = user2.getRoles().stream().anyMatch(role -> RoleEnum.ROLE_HOTEL.name().equals(role.getName()));
        String token = jwtUtil.createToken(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(new LoginResponseDto(isHotel ? "hotel" : "user", token), HttpStatus.OK);
    }

    @PostMapping("/signup")
    @Operation(summary = "Register user account")
    public ResponseEntity<?> registerUserAccount(@RequestBody @Valid SignUpDto signUpDto) {

        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(signUpDto.getEmail()))
            return new ResponseEntity<>("Email is not valid!", HttpStatus.BAD_REQUEST);

        if(userService.findByEmail(signUpDto.getEmail()) != null)
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);

        if(!Objects.equals(signUpDto.getPassword(), signUpDto.getMatchingPassword()))
            return new ResponseEntity<>("Passwords do not match!", HttpStatus.BAD_REQUEST);

        ConfirmationTokenDto confirmationTokenDto = userService.registerNewUserAccount(signUpDto);

        return ResponseEntity.ok().body(confirmationTokenDto);
    }

    @PostMapping("/confirmEmail")
    @Operation(summary = "Confirm email")
    public ResponseEntity<?> confirmEmail(@RequestParam(value = "confirmationToken") String confirmationToken) {
        ConfirmationToken token = userService.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            userService.confirmEmail(token.getUser().getEmail());
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Couldn't verify email");
    }

    @PostMapping("/changePassword")
    @Operation(summary = "Change password")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String>changePassword(@RequestBody ChangePasswordDto changePasswordDto){

        User user = userService.getCurrentUser();
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())){
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
        if (Objects.equals(changePasswordDto.getNewPassword(), changePasswordDto.getOldPassword())){
            return new ResponseEntity<>("New password is same as the old one", HttpStatus.BAD_REQUEST);
        }
        if(!Objects.equals(changePasswordDto.getNewPassword(), changePasswordDto.getMatchingNewPassword()))
            return new ResponseEntity<>("Passwords do not match!", HttpStatus.BAD_REQUEST);
        userService.changePassword(user, changePasswordDto.getNewPassword());

        return new ResponseEntity<>("Password is changed successfully!", HttpStatus.OK);
    }

    @PostMapping("/modifyUser")
    @Operation(summary = "ModifyUser")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserDto>modifyUser(@RequestBody ModifyUserDto modifyUserDto){

        UserDto userDto = userService.modifyUser(modifyUserDto);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Logout.")
    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @GetMapping("/user")
    @Operation(summary = "User details.")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDto getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return  userService.loadUserByUsername(
                principal instanceof UserDetails userDetails
                        ? userDetails.getUsername()
                        : principal.toString());
    }

    @GetMapping("/resetPasswordToken")
    @Operation(summary = "Reset password token")
    public ResponseEntity<?> resetPasswordToken(@RequestParam(value = "email") String email) {
        User user = userService.findByEmail(email);

        if(user != null)
        {
            ConfirmationTokenDto confirmationTokenDto = userService.getTokenToResetPassword(user);
            return ResponseEntity.ok().body(confirmationTokenDto);
        }
        return ResponseEntity.badRequest().body("Couldn't find user with given email");
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Reset password token")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        ConfirmationToken confirmationToken = resetPasswordDto != null && resetPasswordDto.getToken() != null
                ? userService.findByConfirmationToken(resetPasswordDto.getToken())
                : null;

        if(confirmationToken != null)
        {
            if(!Objects.equals(resetPasswordDto.getNewPassword(), resetPasswordDto.getMatchingNewPassword()))
                return ResponseEntity.badRequest()
                        .body("Passwords do not match!");
            userService.changePassword(confirmationToken.getUser(), resetPasswordDto.getNewPassword());
            return getLoginResponseDtoResponseEntity(confirmationToken.getUser().getEmail(), resetPasswordDto.getNewPassword());
        }
        return ResponseEntity.badRequest().body("Couldn't reset password");
    }

}
