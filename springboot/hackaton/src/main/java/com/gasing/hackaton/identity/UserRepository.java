package com.gasing.hackaton.identity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Metodo definito nel diagramma: +findByEmail
    Optional<User> findByEmail(String email);

    // Metodo definito nel diagramma: +existsByEmail
    boolean existsByEmail(String email);
    
    // Il metodo save() è ereditato automaticamente da JpaRepository
}
