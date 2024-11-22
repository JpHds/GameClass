package com.game_class.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Matter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matter_id")
    private Long matterId;

    @Column(name = "matter_name", nullable = false, unique = true)
    private String matterName;
}
