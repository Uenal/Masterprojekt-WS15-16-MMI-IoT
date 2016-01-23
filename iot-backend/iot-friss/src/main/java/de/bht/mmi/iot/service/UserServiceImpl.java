package de.bht.mmi.iot.service;

import de.bht.mmi.iot.constants.RoleConstants;
import de.bht.mmi.iot.dto.UserPutDto;
import de.bht.mmi.iot.model.rest.User;
import de.bht.mmi.iot.repository.SensorRepository;
import de.bht.mmi.iot.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Override
    public User loadUserByUsername(String username) {
        final User user = userRepository.findOne(username);
        if (user == null) {
            throw new EntityNotFoundException(String.format("User with username '%s' not found",username));
        }
        return user;
    }

    @Override
    public User loadUserByUsername(String username, UserDetails authenticatedUser) {
        if (!(isRolePresent(authenticatedUser, RoleConstants.ROLE_ADMIN) ||
                authenticatedUser.getUsername().equals(username))) {
            // TODO: More meaningfuel exception message
            throw new AccessDeniedException("Operation not permitted");
        }
        return loadUserByUsername(username);
    }

    @Override
    public Iterable<User> loadAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(@Validated User user) {
        final String username = user.getUsername();
        if (isUsernameAlreadyInUse(username)) {
            throw new EntityExistsException(String.format("Username '%s' already in use", username));
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(@Validated User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, UserDetails authenticatedUser) {

        if (!(isRolePresent(authenticatedUser, RoleConstants.ROLE_ADMIN) ||
                authenticatedUser.getUsername().equals(user.getUsername()))) {
            // TODO: More meaningfuel exception message
            throw new AccessDeniedException("Operation not permitted");
        }
        return updateUser(user);
    }

    @Override
    public User updateUser(String username, @Validated UserPutDto dto, UserDetails authenticatedUser) {
        // Role admin can change all users, other roles can only change their own data
        if (!(isRolePresent(authenticatedUser, RoleConstants.ROLE_ADMIN) ||
                authenticatedUser.getUsername().equals(username))) {
            // TODO: More meaningfuel exception message
            throw new AccessDeniedException("Operation not permitted");
        }

        final User user = loadUserByUsername(username);
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setPassword(dto.getPassword());
        // TODO: Prevent that admin user leave role ADMIN
        // TODO: Prevent that user can change their roles
        user.setRoles(dto.getRoles());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        final User user = loadUserByUsername(username);
        userRepository.delete(username);
    }

    @Override
    public User updateUserSensors(String username, List<String> sensorList) {
        final User user = loadUserByUsername(username);
        if (!(isRolePresent(user, RoleConstants.ROLE_ADMIN) || user.getUsername().equals(username))) {
            // TODO: More meaningfuel exception message
            throw new AccessDeniedException("Operation not permitted");
        }

        // Check if every sensorId references a sensor
        final List<String> notFoundSensorIds = new ArrayList<>(sensorList.size());
        for (String sensorId : sensorList) {
            if (sensorRepository.findOne(sensorId) == null) {
                notFoundSensorIds.add(sensorId);
            }
        }
        if (!notFoundSensorIds.isEmpty()) {
            throw new EntityNotFoundException(String.format(
                    "The following sensorIds do not reference known sensors: %s",
                    StringUtils.join(notFoundSensorIds, ", ")));
        }

        user.setSensorList(sensorList);
        return userRepository.save(user);
    }

    @Override
    public boolean isRolePresent(UserDetails userDetails, String role) {
        boolean isRolePresent = false;
        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            isRolePresent = grantedAuthority.getAuthority().equals(role);
            if (isRolePresent)
                break;
        }
        return isRolePresent;
    }

    public boolean isUsernameAlreadyInUse(String username) {
        return userRepository.findOne(username) != null ? true: false;
    }

}
