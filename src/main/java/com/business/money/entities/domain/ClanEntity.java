package com.business.money.entities.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "clans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clan_id_seq")
    @SequenceGenerator(name = "clan_id_seq", sequenceName = "clan_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "points_amount")
    private Integer pointsAmount;

    @OneToMany(mappedBy = "clan")
    private Set<UserEntity> members;
}
