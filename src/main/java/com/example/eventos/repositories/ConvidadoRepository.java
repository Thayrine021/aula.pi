package com.example.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventos.models.Convidado;

public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {

}
