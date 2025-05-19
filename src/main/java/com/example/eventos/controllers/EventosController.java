package com.example.eventos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.eventos.models.Evento;
import com.example.eventos.repositories.EventosRepository;

@Controller
@RequestMapping("/eventos")
public class EventosController {
	
	@Autowired
	private EventosRepository er;

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
        er.save(evento);
        return "eventos/eventoConfirmado";
    }
    @GetMapping
    public ModelAndView listar() {
    	List<Evento> eventos = er.findAll();
    	ModelAndView mv = new ModelAndView("eventos/lista");
    	mv.addObject("eventos", eventos);
    	return mv;
    }
}