package org.example.taskpilot_taskmanager.user.controller;


import org.example.taskpilot_taskmanager.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user-management")
public class UserController {

@Autowired
private UserService userService;



// delete user
    @DeleteMapping ("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);

    }


   /* TOdo
    //ADMIN only API
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<ResponseDTO<List<User>>> getAllUsers() {
        return userService.findAll(); // Note abstract it from service
    }
*/


}
