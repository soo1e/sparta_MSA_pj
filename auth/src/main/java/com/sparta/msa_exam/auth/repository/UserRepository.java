package com.sparta.msa_exam.auth.repository;

import com.sparta.msa_exam.auth.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username); // Optional<User>가 아닌 User로 반환
}
