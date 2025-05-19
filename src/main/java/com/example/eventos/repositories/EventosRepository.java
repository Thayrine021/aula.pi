package com.example.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventos.models.Evento;

public interface EventosRepository extends JpaRepository<Evento, Long> {
}


