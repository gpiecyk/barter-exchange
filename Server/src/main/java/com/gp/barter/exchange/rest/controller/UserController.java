package com.gp.barter.exchange.rest.controller;


import com.gp.barter.exchange.events.OnDeleteUserAccountEvent;
import com.gp.barter.exchange.events.OnResetPasswordRequestEvent;
import com.gp.barter.exchange.persistence.model.UserData;
import com.gp.barter.exchange.persistence.model.VerificationToken;
import com.gp.barter.exchange.persistence.service.UserService;
import com.gp.barter.exchange.events.OnRegistrationCompleteEvent;
import com.gp.barter.exchange.rest.exception.UserAlreadyExistsException;
import com.gp.barter.exchange.rest.exception.UserDoesNotExists;
import com.gp.barter.exchange.util.constants.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(ApplicationEventPublisher eventPublisher, UserDetailsService userDetailsService, UserService userService) {
        this.eventPublisher = eventPublisher;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public UserData getUserById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @RequestMapping(value = "/email/{email}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public UserData getUserByEmail(@PathVariable("email") String email) {
        Optional<UserData> user = userService.getUserByEmail(email);
        if (!user.isPresent()) {
            throw new UserDoesNotExists();
        }
        return user.get();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll")
    public void register(@RequestBody UserData user, HttpServletRequest request) {
        if (userService.exists(user)) {
            throw new UserAlreadyExistsException();
        } else {
            user.setStatus(UserStatus.INACTIVE);
            userService.create(user);
        }
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, getAppUrl(request)));
    }

    @RequestMapping(value = "/registration/confirm", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity confirmRegistration(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationToken = userService.getVerificationToken(token);

        if (!verificationToken.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        UserData user = verificationToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.get().getExpiryDate().getTime() - calendar.getTime().getTime() <= 0)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        userService.updateStatus(user, UserStatus.ACTIVE);
        userService.deleteToken(verificationToken.get());

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/registration/confirm/resend", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity resendConfirmationByToken(@RequestParam("token") String token, HttpServletRequest request) {
        Optional<VerificationToken> verificationToken = userService.getVerificationToken(token);

        if (!verificationToken.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        UserData user = verificationToken.get().getUser();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, getAppUrl(request)));
        userService.deleteToken(verificationToken.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/registration/confirm/resend/email", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity resendConfirmationByEmail(@RequestParam("email") String email, HttpServletRequest request) {
        Optional<UserData> user = userService.getUserByEmail(email);
        if (!user.isPresent()) {
            throw new UserDoesNotExists();
        }
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user.get(), getAppUrl(request)));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll")
    public void resetPassword(@RequestParam("email") String email, HttpServletRequest request) {
        Optional<UserData> user = userService.getUserByEmail(email);
        if (!user.isPresent()) {
            throw new UserDoesNotExists();
        }
        eventPublisher.publishEvent(new OnResetPasswordRequestEvent(user.get(), getAppUrl(request)));
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public ResponseEntity changePassword(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationToken = userService.getVerificationToken(token);

        if (!verificationToken.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        UserData user = verificationToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.get().getExpiryDate().getTime() - calendar.getTime().getTime() <= 0)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
                userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        userService.deleteToken(verificationToken.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/savePassword", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public void savePassword(@RequestParam("password") String password) {
        UserData user = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(user, password);
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }

    @RequestMapping(value = "/savePasswordFromSettings", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public void savePasswordFromSettings(@RequestParam("password") String password, @RequestParam("email") String email) {
        Optional<UserData> user = userService.getUserByEmail(email);
        user.ifPresent(userData -> userService.changePassword(userData, password));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public UserData updateUser(@RequestBody UserData user) {
        return userService.update(user);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public void deleteUser(@RequestParam("userId") Long userId) {
        UserData user;
        try {
            user = userService.getById(userId);
            userService.delete(user);
            eventPublisher.publishEvent(new OnDeleteUserAccountEvent(user));
        } catch (NoResultException e) {
            throw new UserDoesNotExists();
        }
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort()
                + "/app/app/#" + request.getContextPath();
    }
}
