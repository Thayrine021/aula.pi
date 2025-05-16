package com.example.eventos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventos.models.Evento;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @GetMapping("/form")
    public String form() {
        return "eventos/formevento";
    }

    @PostMapping
    public String salvar(Evento evento) {
        System.out.println("Nome: " + evento.getNome());
        System.out.println("Local: " + evento.getLocal());
        System.out.println("Data: " + evento.getData());
        System.out.println("Horário: " + evento.getHorario());
        return "eventos/eventoConfirmado";
    }
}