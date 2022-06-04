package ru.netology.cloud_storage_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloud_storage_backend.model.user.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
}
