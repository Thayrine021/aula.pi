package com.example.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.eventos.models.Convidado;
import com.example.eventos.models.Evento;
import com.example.eventos.repositories.ConvidadoRepository;
import com.example.eventos.repositories.EventosRepository;

@Controller
@RequestMapping("/eventos")
public class EventosController {

	@Autowired
	private EventosRepository er;
	@Autowired
	private ConvidadoRepository cr;

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
		return "redirect:/eventos";
	}

	@GetMapping
	public ModelAndView listar() {
		List<Evento> eventos = er.findAll();
		ModelAndView mv = new ModelAndView("eventos/lista");
		mv.addObject("eventos", eventos);
		return mv;
	}

	@GetMapping("/{id}")
	public ModelAndView detalhar(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Evento> opt = er.findById(id);
		
		if (opt.isEmpty()) {
			md.setViewName("redirect:/eventos");
			return md;
		}

		md.setViewName("eventos/detalhes");
		Evento evento = opt.get();

		md.addObject("evento", evento);
		
		List<Convidado> convidados = cr.findByEvento(evento);
		md.addObject("convidados", convidados);
		
		return md;
		

	}
	
	@PostMapping("/{idEvento}")
	public String salvarConvidado(@PathVariable Long idEvento, Convidado convidado) {
		
		System.out.println("Id do evento: " + idEvento);
		System.out.println(convidado);
		
		Optional<Evento> opt = er.findById(idEvento);
			if(opt.isEmpty()) {
				return "redirect:/eventos";
			}
			Evento evento=opt.get();
			convidado.setEvento(evento);
			
			cr.save(convidado);
			
		return "redirect:/eventos/{idEvento}";
		
	}
	
	@GetMapping("/{id}/remover")
	public String apagarEvento(@PathVariable Long id) {
		
		Optional<Evento> opt = er.findById(id);
		
		if(!opt.isEmpty()) {
			Evento evento = opt.get();
			
			List<Convidado> convidados = cr.findByEvento(evento);
			
			cr.deleteAll(convidados);
		
			er.delete(evento);
		}
		return "redirect:/eventos";
	}
	
	@GetMapping("/{id}/removerConvidado")
	public String apagarConvidado(@PathVariable Long id) {
		
		Optional<Convidado> opt = cr.findById(id);
		
		if(!opt.isEmpty()) {
			Convidado convidado = opt.get();
			cr.delete(convidado);
			}
			return "redirect:/eventos/detalhes";
	}
}