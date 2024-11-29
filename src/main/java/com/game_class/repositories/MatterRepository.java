package com.game_class.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.game_class.models.Matter;

@Repository
public interface MatterRepository extends JpaRepository<Matter, Long> {
    List<Matter> findAllByOrderByMatterNameAsc();

    Matter findByMatterId(Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO matter (matter_name) VALUES " +
            "('Artes'), ('Biologia'), ('Ciências'), ('Educação Física'), ('Espanhol'), " +
            "('Filosofia'), ('Física'), ('Geografia'), ('História'), ('Língua Portuguesa'), " +
            "('Matemática'), ('Química'), ('Sociologia')", nativeQuery = true)
    void insertMatters();
}
