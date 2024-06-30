package com.be.app.repository;

import com.be.app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUuid(String uuid);

    UserEntity findByUsername(String username);

    UserEntity findByUsernameAndStatus(String username, String status);

}
