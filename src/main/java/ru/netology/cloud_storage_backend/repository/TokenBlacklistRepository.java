package ru.netology.cloud_storage_backend.repository;

import org.springframework.data.repository.CrudRepository;
import ru.netology.cloud_storage_backend.model.token.TokenBlacklist;


public interface TokenBlacklistRepository extends CrudRepository<TokenBlacklist, String> {
}
