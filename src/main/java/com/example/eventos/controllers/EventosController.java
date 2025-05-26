package com.example.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventos.models.Convidado;
import com.example.eventos.models.Evento;
import com.example.eventos.repositories.ConvidadoRepository;
import com.example.eventos.repositories.EventosRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @Autowired
    private EventosRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @GetMapping("/form")
    public String form(Evento evento) {
        return "eventos/formevento";
    }

    @GetMapping("/eventoConfirmado")
    public String eventoConfirmado() {
        return "eventos/eventoConfirmado";
    }

    @PostMapping
    public String salvar(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
    	
    	if(result.hasErrors()) {
    		return form(evento);
    	}
    	System.out.println(evento);
        er.save(evento);
        attributes.addFlashAttribute("mensagem", "Evento salvo com sucesso!");
        
        return "redirect:/eventos";
    }

    @GetMapping
    public ModelAndView listar() {
        List<Evento> eventos = er.findAll();
        ModelAndView mv = new ModelAndView("eventos/lista");
        mv.addObject("eventos", eventos);
        return mv;
    }

    @GetMapping("/detalhes/{id}")
    public ModelAndView detalhar(@PathVariable Long id, Convidado convidado) {
        ModelAndView md = new ModelAndView();
        Optional<Evento> opt = er.findById(id);

        if (opt.isEmpty()) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        Evento evento = opt.get();
        md.setViewName("eventos/detalhes");
        md.addObject("evento", evento);

        List<Convidado> convidados = cr.findByEvento(evento);
        md.addObject("convidados", convidados);

        md.addObject("convidado", convidado); 

        return md;
    }

    @PostMapping("/detalhes/{idEvento}")
    public String salvarConvidado(@PathVariable Long idEvento, Convidado convidado, BindingResult result, RedirectAttributes attributes) {
        Optional<Evento> opt = er.findById(idEvento);
        
        if(result.hasErrors()){
        	return apagarConvidado(idEvento, attributes);
        }
        
        if (opt.isEmpty()) {
            return "redirect:/eventos";
        }
        

        Evento evento = opt.get();
        convidado.setEvento(evento);

        cr.save(convidado);
        attributes.addFlashAttribute("mensagemConvidado", "Convidado(a) adicionado(a) com sucesso!");

        return "redirect:/eventos/detalhes/" + idEvento;
    }


    @GetMapping("/{id}/selecionar")
    public ModelAndView selecionarEvento(@PathVariable Long id) {
        ModelAndView md = new ModelAndView();
        Optional<Evento> opt = er.findById(id);

        if (opt.isEmpty()) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        Evento evento = opt.get();
        md.setViewName("eventos/formEvento");
        md.addObject("evento", evento);
        return md;
    }

    @GetMapping("/{idEvento}/convidados/{idConvidado}/selecionar")
    public ModelAndView selecionarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
        ModelAndView md = new ModelAndView();

        Optional<Evento> optEvento = er.findById(idEvento);
        Optional<Convidado> optConvidado = cr.findById(idConvidado);

        if (optEvento.isEmpty() || optConvidado.isEmpty()) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        Evento evento = optEvento.get();
        Convidado convidado = optConvidado.get();

        if (!evento.getId().equals(convidado.getEvento().getId())) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        md.setViewName("eventos/detalhes");
        md.addObject("convidado", convidado); 
        md.addObject("evento", evento);
        md.addObject("convidados", cr.findByEvento(evento));

        return md;
    }

    @GetMapping("/{id}/remover")
    public String apagarEvento(@PathVariable Long id, RedirectAttributes attributes) {
        Optional<Evento> opt = er.findById(id);

        if (opt.isPresent()) {
            Evento evento = opt.get();
            List<Convidado> convidados = cr.findByEvento(evento);
            cr.deleteAll(convidados);
            er.delete(evento);
            attributes.addFlashAttribute("mensagem", "Evento removido com sucesso!");
        }

        return "redirect:/eventos";
    }

    @GetMapping("/{id}/removerConvidado")
    public String apagarConvidado(@PathVariable Long id,  RedirectAttributes attributes) {
        Optional<Convidado> opt = cr.findById(id);

        if (opt.isPresent()) {
            Convidado convidado = opt.get();
            Long idEvento = convidado.getEvento().getId();
            cr.delete(convidado);
            attributes.addFlashAttribute("mensagem", "Convidado removido com sucesso!");
            return "redirect:/eventos/detalhes/" + idEvento;
        }

        return "redirect:/eventos";
    }
}
