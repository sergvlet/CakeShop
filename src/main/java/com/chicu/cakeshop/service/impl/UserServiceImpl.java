package com.chicu.cakeshop.service.impl;



import com.chicu.cakeshop.enums.Authority;
import com.chicu.cakeshop.enums.MailType;
import com.chicu.cakeshop.exception.PancakeException;
import com.chicu.cakeshop.exception.UserAlreadyExistException;
import com.chicu.cakeshop.exception.WrongFieldException;
import com.chicu.cakeshop.model.Role;
import com.chicu.cakeshop.model.User;
import com.chicu.cakeshop.repositories.RoleRepository;
import com.chicu.cakeshop.repositories.UserRepository;
import com.chicu.cakeshop.service.MailService;
import com.chicu.cakeshop.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void updateUserRoles(Integer userId, List<Integer> roleIds) throws WrongFieldException {
        if (userId == null || roleIds == null || roleIds.isEmpty()) {
            throw new WrongFieldException(Collections.singletonList("User ID or role IDs cannot be null or empty"));
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new WrongFieldException(Collections.singletonList("User not found")));

        if (roleIds.isEmpty()) {
            throw new WrongFieldException(Collections.singletonList("Role IDs cannot be empty"));
        }

        Set<Role> roles = roleIds.stream()
                .map(roleId -> {
                    try {
                        return roleRepository.findById(roleId)
                                .orElseThrow(() -> new WrongFieldException(Collections.singletonList("Role not found for ID " + roleId)));
                    } catch (WrongFieldException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

        user.setAuthorities(roles);
        userRepository.save(user);
    }

    @Override
    public User updateUser(User user) throws PancakeException {
        checkBeforeSave(user);

        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            Role userRole = roleRepository.findByAuthority(Authority.ROLE_USER.toString())
                    .orElseThrow(() -> new WrongFieldException(Collections.singletonList("Role USER not found")));
            user.setAuthorities(Set.of(userRole));
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with this email already exists");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        mailService.sendMail(user, MailType.REGISTRATION);

        return user;
    }

    private void checkBeforeSave(User user) throws WrongFieldException {
        if (user == null) {
            throw new WrongFieldException(Collections.singletonList("User cannot be null"));
        }

        if (StringUtils.isEmpty(user.getEmail())) {
            throw new WrongFieldException(Collections.singletonList("Email cannot be empty"));
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new WrongFieldException(Collections.singletonList("User with this email already exists"));
        }

        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new WrongFieldException(Collections.singletonList("Password must be at least 8 characters long"));
        }

        if (user.getFirstname() == null) {
            throw new WrongFieldException(Collections.singletonList("First name cannot be empty"));
        }
    }
}
