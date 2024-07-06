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
            UsuarioEntity usuarioAutenticado = usuarioService.buscarUsuarioPorCorreo(usuarioEntity.getCorreo());
            if (usuarioAutenticado != null && usuarioAutenticado.getTipo() != null) {
                String tipoUsuario = usuarioAutenticado.getTipo().getNombre();
                if ("Administrador".equals(tipoUsuario)) {
                    return "redirect:/mantenimientos"; // Redirigir a mantenimientos.html si es administrador
                }
                return "redirect:/menu"; // Redirigir a menu.html para otros tipos de usuarios
            } else {
                model.addAttribute("loginInvalido", "El usuario no tiene un tipo definido");
                model.addAttribute("usuario", new UsuarioEntity());
                return "login"; // Volver a la página de login si el usuario no tiene un tipo definido
            }
        }
        model.addAttribute("loginInvalido", "No existe el usuario");
        model.addAttribute("usuario", new UsuarioEntity());
        return "login"; // Volver a la página de login si el usuario no es válido
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/mantenimientos")
    public String ShowMantenimientos(HttpSession session, Model model) {
    	
    	if (session.getAttribute("usuario") == null) {
			return "redirect:/";
		}
    	
        return "mantenimientos"; 
    }
}
