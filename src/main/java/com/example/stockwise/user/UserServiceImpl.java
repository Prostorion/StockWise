package com.example.stockwise.user;

import com.example.stockwise.user.model.Role;
import com.example.stockwise.user.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()))

                .build();
    }

    @Override
    public void saveUser(User user) throws Exception {
        validateUser(user);
        Set<Role> rolesSet = new HashSet<>();
        var roles = user.getRoles();
        for (var role: roles) {
            rolesSet.add(roleRepository.findByName(role.getName()).orElseThrow(RoleNotFoundException::new));
        }
        user.setRoles(rolesSet);
        userRepository.save(user);
    }

    @Override
    public void saveAdmin(User user) throws Exception {
        saveOneRoleUser(user, "ADMIN");
    }

    @Override
    public void saveManager(User user) throws Exception {
        saveOneRoleUser(user, "MANAGER");
    }

    @Override
    public void saveWorker(User user) throws Exception {
        saveOneRoleUser(user, "WORKER");
    }

    private void saveOneRoleUser(User user, String role) throws Exception {
        validateUser(user);
        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(roleRepository.findByName(role).get());
        user.setRoles(rolesSet);
        userRepository.save(user);
    }

    private void validateUser(User user) throws Exception {
        usernameValidation(user.getUsername());
        nameValidation(user.getFirstname());
        nameValidation(user.getLastname());
        passwordValidation(user.getPassword());
    }

    private void passwordValidation(String password) throws Exception {
        String regex = "^[a-zA-Z0-9$_#@%*?!.,]{4,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if(!matcher.matches()){
            throw new Exception("Password is invalid (4-30 characters: letters, numbers or $_#@%*?!.,)");
        }
    }

    private void nameValidation(String name) throws Exception {

        String regex = "^[A-Z][a-zA-Z]{3,29}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        if(!matcher.matches()){
            throw new Exception("Name is invalid (4-30 characters, letters only, starts with capital)");
        }
    }

    private void usernameValidation(String username) throws Exception {

        if (userRepository.findByUsername(username).isPresent()){
            throw new Exception("Username is already taken");
        }

        String regex = "^[a-zA-Z0-9]{4,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        if(!matcher.matches()){
            throw new Exception("Username is invalid (4-30 characters, letters and numbers only)");
        }
    }
}
