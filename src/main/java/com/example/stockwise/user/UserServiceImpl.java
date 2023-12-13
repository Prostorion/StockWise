package com.example.stockwise.user;

import com.example.stockwise.role.Role;
import com.example.stockwise.role.RoleRepository;
import com.example.stockwise.task.Task;
import com.example.stockwise.task.TaskRepository;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    TaskRepository taskRepository;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, WarehouseRepository warehouseRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.warehouseRepository = warehouseRepository;
        this.taskRepository = taskRepository;
    }




    @Override
    public void addAdmin(User user, PasswordEncoder passwordEncoder) throws Exception {
        saveOneRoleUser(user, "ADMIN", passwordEncoder);
    }

    @Override
    public void addManager(User user, PasswordEncoder passwordEncoder) throws Exception {
        saveOneRoleUser(user, "MANAGER", passwordEncoder);
    }

    @Override
    public void addWorker(User user, PasswordEncoder passwordEncoder) throws Exception {
        saveOneRoleUser(user, "WORKER", passwordEncoder);
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
    public void addWorkerToWarehouse(User user, Long id, PasswordEncoder passwordEncoder) throws Exception {
        addWorker(user, passwordEncoder);
        User worker = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new Exception("user not found"));
        Set<Warehouse> warehouseSet = new HashSet<>();
        warehouseSet.add(warehouseRepository.findById(id).get());
        user.setWarehouses(warehouseSet);
        userRepository.save(user);
    }

    @Override
    public Set<User> getWarehouseUsers(Long id) {
        return userRepository.findAllByWarehousesIdOrderByFirstname(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteWorker(Long userId, Long warehouse_id) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        if (user.getWarehouses().stream().noneMatch(w -> w.getId().equals(warehouse_id))) {
            throw new Exception("User is not allowed to this warehouse");
        }
        if (user.getRoles().stream().map(Role::getName).noneMatch(n -> n.equals("WORKER"))) {
            throw new Exception("Can't delete not WORKER");
        }
        Set<Task> tasks = taskRepository.findAllByAssignee(user);

        reassignToDefaultUser(tasks);

        userRepository.delete(user);
    }

    private void reassignToDefaultUser(Set<Task> tasks) {
        User defaultUser = userRepository.findById(5L).get();
        tasks.forEach(t-> t.setAssignee(defaultUser));
        taskRepository.saveAll(tasks);
    }

    @Override
    public void updateCurrentUser(User user) throws Exception {
        User oldUser = getUser().orElseThrow(() -> new Exception("User not found"));
        if (!user.getUsername().equals(oldUser.getUsername())){
            usernameValidation(user.getUsername());
        }
        nameValidation(user.getFirstname());
        nameValidation(user.getLastname());
        oldUser.setUsername(user.getUsername());
        oldUser.setFirstname(user.getFirstname());
        oldUser.setLastname(user.getLastname());
        userRepository.save(oldUser);
    }


    private void saveOneRoleUser(User user, String role, PasswordEncoder passwordEncoder) throws Exception {
        validateUser(user);
        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(roleRepository.findByName(role).get());
        user.setRoles(rolesSet);
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
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
