package com.game_class.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game_class.models.Matter;

@Repository
public interface MatterRepository extends JpaRepository<Matter, Long> {
    List<Matter> findAllByOrderByMatterNameAsc();
    Matter findByMatterId(Long id);
}
