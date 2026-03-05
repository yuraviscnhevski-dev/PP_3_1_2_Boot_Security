package ru.kata.spring.boot_security.demo.configs;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        // Проверяем, есть ли уже пользователи
        if (userService.findByUsername("admin") != null || userService.findByUsername("user") != null) {
            System.out.println("===== Пользователи уже существуют, пропускаем инициализацию =====");
            return;
        }

        // 1. Сначала создаем и сохраняем роли в БД
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        System.out.println("===== Роли созданы =====");

        // 2. Получаем сохраненные роли из БД (с ID)
        Role savedAdminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        Role savedUserRole = roleRepository.findByName("ROLE_USER").orElseThrow();

        // 3. Создаем админа
        User admin = new User("Admin", "Adminov", 30, "admin", "admin");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(savedAdminRole);
        adminRoles.add(savedUserRole);
        admin.setRoles(adminRoles);

        // 4. Создаем пользователя
        User user = new User("User", "Userov", 25, "user", "user");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(savedUserRole);
        user.setRoles(userRoles);

        // 5. Сохраняем пользователей (роли уже есть в БД)
        userService.save(admin);
        userService.save(user);

        System.out.println("===== Данные инициализированы =====");
        System.out.println("Admin: admin/admin (роли: ADMIN, USER)");
        System.out.println("User: user/user (роль: USER)");
    }
}