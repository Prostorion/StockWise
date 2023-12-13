package com.example.stockwise.warehouse;

import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    UserService userService;
    WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(UserService userService, WarehouseRepository warehouseRepository) {
        this.userService = userService;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void addWarehouse(Warehouse warehouse) throws Exception {
        User user = userService.getUser().orElseThrow(() -> new UsernameNotFoundException("there is no user SERIOUS EXCEPTION"));
        validateWarehouse(user, warehouse);
        warehouseRepository.save(warehouse);
        warehouse = warehouseRepository.findByName(warehouse.getName()).orElseThrow(() -> new Exception("warehouse is not found SERIOUS EXCEPTION"));
        user.getWarehouses().add(warehouse);
        userService.updateUser(user);
    }

    @Override
    public Set<Warehouse> getUserWarehouses(User user) {
        return warehouseRepository.findAllByUserSetContainsOrderByName(user);
    }

    @Override
    public void deleteWarehouseById(Long id) throws Exception {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new Exception("warehouse not found"));
        warehouse.getUserSet().forEach(user -> user.getWarehouses().remove(warehouse));
        warehouseRepository.deleteById(id);
    }

    @Override
    public Warehouse getWarehouseById(Long id) throws Exception {
        return warehouseRepository.findById(id).orElseThrow(() -> new Exception("warehouse not found"));
    }

    @Override
    public void updateWarehouse(Long id, Warehouse newWarehouse) throws Exception {
        User user = userService.getUser().orElseThrow(() -> new UsernameNotFoundException("there is no user SERIOUS EXCEPTION"));
        Warehouse oldWarehouse = getWarehouseById(id);
        if (!oldWarehouse.getName().equals(newWarehouse.getName())) {
            nameValidation(user, newWarehouse.getName());
        }
        addressValidation(newWarehouse.getAddress());
        oldWarehouse.setName(newWarehouse.getName());
        oldWarehouse.setAddress(newWarehouse.getAddress());
        warehouseRepository.save(oldWarehouse);
    }


    private void validateWarehouse(User user, Warehouse warehouse) throws Exception {
        nameValidation(user, warehouse.getName());
        addressValidation(warehouse.getAddress());
    }

    private void nameValidation(User user, String name) throws Exception {

        if (user.getWarehouses().stream().anyMatch(w -> w.getName().equals(name))) {
            throw new Exception(name + " is already exist");
        }

        String regex = "^[a-zA-Z0-9 .,-]{4,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new Exception("Name is invalid (4-30 characters, letters, numbers ',.-' only)");
        }
    }

    private void addressValidation(String address) throws Exception {
        String regex = "^[a-zA-Z0-9 .,-]{5,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);

        if (!matcher.matches()) {
            throw new Exception("Address is invalid (5-30 characters, letters, numbers ',.-' only)");
        }
    }
}
