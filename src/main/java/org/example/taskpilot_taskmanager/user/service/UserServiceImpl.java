package org.example.taskpilot_taskmanager.user.service;


import org.example.taskpilot_taskmanager.user.repositories.UserRepository;
import org.example.taskpilot_taskmanager.task.utils.TaskEntityDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    public void deleteUser(Long userId){
        //Todo ideally only authenticated user Or admin should be able to delete
        userRepository.deleteById(userId);
    }

}
