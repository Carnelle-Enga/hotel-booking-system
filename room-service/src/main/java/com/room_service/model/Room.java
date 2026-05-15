package com.room_service.model;

import com.room_service.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private int numero;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RoomType room_type;

  @Column(nullable = false)
 private Double prix_par_nuit;

  @Column(nullable = false)
 private boolean disponible;
  
}
