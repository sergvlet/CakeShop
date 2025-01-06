package com.chicu.cakeshop.service;




import com.chicu.cakeshop.exception.PancakeException;
import com.chicu.cakeshop.exception.WrongFieldException;
import com.chicu.cakeshop.model.User;

import java.util.List;
import java.util.Optional;



public interface UserService {

    List<User> getAll();

    Optional<User> getUserById(int id);

    void deleteUser(int id);

    boolean userExistsByEmail(String email);

    void updateUserRoles(Integer userId, List<Integer> roleIds) throws WrongFieldException;

    User updateUser(User user) throws PancakeException;

}
