package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.serviceInterface.user.UserService;


@RestController
@RequestMapping("api/admin")//pre-path
public class AdminController
{
    @Autowired
    private UserService userService;

    @GetMapping("all")//api/admin/all
    public ResponseEntity<?> findAllUsers()
    {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    
    @PutMapping("change/{role}")//api/user/change/{role}
    public ResponseEntity<?> changeRole(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Role role)
    {
        userService.changeRole(role, userPrincipal.getUsername());

        return ResponseEntity.ok(true);
    }
    
    @PutMapping("makeAdmin/{username}")
    public ResponseEntity<?> makeAdmin(@PathVariable(value="username") String username) {
    	userService.makeAdmin(username);
    	return ResponseEntity.ok(true);
    }
}
