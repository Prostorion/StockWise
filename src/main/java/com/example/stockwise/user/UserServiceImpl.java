package com.example.stockwise.user;

import com.example.stockwise.role.Role;
import com.example.stockwise.role.RoleRepository;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    RoleRepository roleRepository;

    WarehouseRepository warehouseRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, WarehouseRepository warehouseRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.warehouseRepository = warehouseRepository;
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
    public void addUser(User user) throws Exception {
        validateUser(user);
        Set<Role> rolesSet = new HashSet<>();
        var roles = user.getRoles();
        for (var role : roles) {
            rolesSet.add(roleRepository.findByName(role.getName()).orElseThrow(RoleNotFoundException::new));
        }
        user.setRoles(rolesSet);
        userRepository.save(user);
    }

    @Override
    public void addAdmin(User user) throws Exception {
        saveOneRoleUser(user, "ADMIN");
    }

    @Override
    public void addManager(User user) throws Exception {
        saveOneRoleUser(user, "MANAGER");
    }

    @Override
    public void addWorker(User user) throws Exception {
        saveOneRoleUser(user, "WORKER");
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isWarehouseAccessibleByUser(String username, Long requestedWarehouseId) {
        Optional<User> userOptional = findUserByUsername(username);
        if (userOptional.isPresent()) {
            Set<Warehouse> warehouses = userOptional.get().getWarehouses();
            return warehouses.stream().anyMatch(w -> Objects.equals(w.getId(), requestedWarehouseId));
        }

        return false;
    }

    public Optional<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return findUserByUsername(username);
        }
        return Optional.empty();
    }

    @Override
    public void updateUser(User user) throws Exception {
        userRepository.save(user);
    }

    @Override
    public void addWorkerToWarehouse(User user, Long id) throws Exception {
        addWorker(user);
        User worker = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new Exception("user not found"));
        Set<Warehouse> warehouseSet = new HashSet<>();
        warehouseSet.add(warehouseRepository.findById(id).get());
        user.setWarehouses(warehouseSet);
        userRepository.save(user);
    }

    @Override
    public Set<User> getWarehouseUsers(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id).get();
        return userRepository.findAllByWarehousesContainingOrderByFirstname(warehouse);
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

        if (!matcher.matches()) {
            throw new Exception("Password is invalid (4-30 characters: letters, numbers or $_#@%*?!.,)");
        }
    }

    private void nameValidation(String name) throws Exception {

        String regex = "^[A-Z][a-zA-Z]{3,29}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new Exception("Name is invalid (4-30 characters, letters only, starts with capital)");
        }
    }

    private void usernameValidation(String username) throws Exception {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username is already taken");
        }

        String regex = "^[a-zA-Z0-9]{4,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        if (!matcher.matches()) {
            throw new Exception("Username is invalid (4-30 characters, letters and numbers only)");
        }
    }
}
