package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.model.CreateUserDto;
import com.example.userservice.model.UpdateUserDto;
import com.example.userservice.model.ViewUserDto;
import com.example.userservice.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Создание пользователя (должна быть синхронная проверка из company-service, на существование компании. Если компании не существует –
    // кидать 404 ошибку с соответствующим сообщением)
    public Long createUser(CreateUserDto createUserDto) {
        // check existing of company
        /*if (!companyService.companyExists(.getCompanyId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company does not exist");
        }*/


        var user = new User();
        user.setName(createUserDto.getName());
        user.setLogin(createUserDto.getLogin());
        user.setPassword(createUserDto.getPassword());
        user.setEmail(createUserDto.getEmail());
        user.setEnabled(true);
        user.getCompanyId();

        userRepository.save(user);
        return user.getId();
    }

    // Получение списка всех пользователей (информация о компании должна подтягиваться синхронное из company-service)
    public List<ViewUserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            var userDto = new ViewUserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setLogin(user.getLogin());
            userDto.setEmail(user.getEmail());
            userDto.setEnabled(user.getEnabled());
            userDto.setCompanyId(user.getCompanyId());

            //userDto.setCompanyName(user.getCompanyName());
            return userDto;
        }).collect(Collectors.toList());
    }

    // Активация/деактивация пользователя
    public void setUserEnabledState(Long id, boolean enabled) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    public void updateUserInfo(UpdateUserDto updateUserDto) {
        User user = userRepository.findByIdAndEnabledTrue(updateUserDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or inactive"));

        // check existing company
        /*if (!companyService.companyExists(userDto.getCompanyName())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company does not exist");
        }*/

        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());
        user.setCompanyId(updateUserDto.getCompanyId());

        userRepository.save(user);
    }

    // Проверка существования пользователя по идентификатору (если пользователь существует, но неактивен – тоже кидаем 404 ошибку)
    public boolean checkUserExisting(Long id) {
        return userRepository.findByIdAndEnabledTrue(id).isPresent();
    }
}