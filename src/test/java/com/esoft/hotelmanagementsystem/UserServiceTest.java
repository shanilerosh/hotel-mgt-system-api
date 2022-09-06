package com.esoft.hotelmanagementsystem;

import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.RoleRepo;
import com.esoft.hotelmanagementsystem.repo.UserRepo;
import com.esoft.hotelmanagementsystem.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author ShanilErosh
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private RoleRepo roleRepo;

    private UserMst testUser;

    private Role testRole;

    public static final String VALID_USER_NAME = "prathiba";
    public static final String VALID_ROLE = "ROLE_TEST";

    @BeforeEach
    void setup() {
        testUser = UserMst.builder().username(VALID_USER_NAME).id(1L)
                .password("prathiba@123").email("prathiba@gmail.com").build();

        testRole = Role.builder().name(VALID_ROLE).build();

        //mocking user repo
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepo.save(any(UserMst.class))).thenReturn(testUser);
        when(userRepo.findByUsername(VALID_USER_NAME)).thenReturn(Optional.of(testUser));
        when(userRepo.findAll()).thenReturn(Arrays.asList(testUser, testUser));

        //mocking role repo
        when(roleRepo.findByName(VALID_ROLE)).thenReturn(Optional.of(testRole));
        when(roleRepo.save(any(Role.class))).thenReturn(testRole);

    }


    @Test
    @DisplayName("Save User")
    void SaveUser() {

        UserMst savedUser = userService.saveUser(testUser);

        Assertions.assertNotNull(savedUser, "The saved user should not be null");

        //make sure incremented id works properly
        Assertions.assertEquals(1L, savedUser.getId(), "The saved ID should be 1L");

        assertThat(savedUser.getUsername()).isNotBlank();
    }

    @Test
    @DisplayName("Get User by Name success")
    void fetchUserName_success() {

        Assertions.assertDoesNotThrow(()-> {
            userService.getUser(VALID_USER_NAME);

        },"Get username should not throw Common Exception");

        UserMst user = userService.getUser(VALID_USER_NAME);

        //test if user is not null
        Assertions.assertNotNull(user, "User should not be null");

        //test if the valid user is returned
        Assertions.assertEquals(user.getUsername(), VALID_USER_NAME,  "User name should be "+VALID_USER_NAME);
    }

    @Test
    @DisplayName("Get User by Name failure")
    void fetchUserName_failure() {

        String invalidUserName = "nonExistingUser";

        //assert an exception is thrown
        CommonException commonException = Assertions.assertThrows(
                CommonException.class,
                () -> userService.getUser(invalidUserName));

        //assert the validation msg
        Assertions.assertEquals(commonException.getMessage(), "User not exist with username "+invalidUserName );
    }

    @Test
    @DisplayName("Save Role Success")
    void saveRole_success() {

        //valid role
        testRole.setName(VALID_ROLE.concat("1"));

        Role savedRole = Assertions.assertDoesNotThrow(() -> userService.saveRole(testRole),
                "Save role should not throw a common Exception");

        //test if user is not null
        Assertions.assertNotNull(savedRole, "User should not be null");

        //test if the valid user is returned
        Assertions.assertEquals(testRole.getName(), savedRole.getName(),  "User name should be "+testUser.getUsername());

    }

    @Test
    @DisplayName("Save Role Failure")
    void saveRole_failure() {
        CommonException commonException = Assertions.assertThrows(
                CommonException.class,
                () -> userService.saveRole(testRole));

        //assert the validation msg
        Assertions.assertEquals(commonException.getMessage(), "Role Already Exist with the name "+testRole.getName() );

    }

    @Test
    @DisplayName("Fetch All Users")
    void fetchAllUsers() {
        //assert the validation msg
        Assertions.assertEquals(userService.getUsers().size(),2);

    }

    @Test
    @DisplayName("Add Roles to User")
    void addRolesToUser_success() {
//        Assertions.assertDoesNotThrow(() -> userService.addRoleToUser(VALID_USER_NAME, VALID_ROLE),
//                "Save role should not throw a common Exception");



    }
}
