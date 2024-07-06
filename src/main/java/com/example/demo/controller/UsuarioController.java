package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.TipoEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.TipoRepository;
import com.example.demo.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
	@Autowired
	private TipoRepository tipoRepository;

    @GetMapping("/registrar")
    public String showRegistrarUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioEntity());
        List<TipoEntity> tipos = tipoRepository.findAll();
        model.addAttribute("tipos", tipos);
        return "registrar_usuario";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(UsuarioEntity usuarioEntity, Model model) {
    	
    	List<TipoEntity> tipos = tipoRepository.findAll();
        model.addAttribute("tipos", tipos);
    	
        usuarioService.crearUsuario(usuarioEntity, model);
        return "registrar_usuario";
    }
    
    @GetMapping("/login")
    public String showlogin(Model model) {
        model.addAttribute("usuario", new UsuarioEntity());
        return "login";
    }

    @PostMapping("/login")
    public String login(UsuarioEntity usuarioEntity, Model model, HttpSession session) {
        boolean usuarioValido = usuarioService.validarUsuario(usuarioEntity, session);
        if (usuarioValido) {
            return "redirect:/menu"; 
        }
        model.addAttribute("loginInvalido", "No existe el usuario");
		model.addAttribute("usuario", new UsuarioEntity());
        return "login"; 
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
