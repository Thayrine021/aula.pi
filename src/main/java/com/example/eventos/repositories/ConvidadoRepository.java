package com.example.eventos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventos.models.Convidado;
import com.example.eventos.models.Evento;

public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {

	List<Convidado> findByEvento(Evento evento);
}
