package ru.itis.migrants.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.migrants.userservice.jpa.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
