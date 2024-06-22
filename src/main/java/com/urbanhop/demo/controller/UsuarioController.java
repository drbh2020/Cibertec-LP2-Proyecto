package com.urbanhop.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.urbanhop.demo.entity.TipoEntity;
import com.urbanhop.demo.entity.UsuarioEntity;
import com.urbanhop.demo.repository.TipoRepository;
import com.urbanhop.demo.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
    private TipoRepository tipoRepository;
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	
	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("usuario", new UsuarioEntity());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute UsuarioEntity usuario, Model model, HttpSession session ) {
		UsuarioEntity usuarioEncontradoEntity = usuarioRepository.findByCorreoAndPassword(usuario.getCorreo(), usuario.getPassword());
		
		if (usuarioEncontradoEntity != null) {
			session.setAttribute("usuario", usuarioEncontradoEntity.getCorreo());
			return "redirect:/menu";
		} else {
			model.addAttribute("loginIncorrecto", "Este usuario no existe");
			model.addAttribute("usuario", new UsuarioEntity());
			return "login";
		}
		
	}
	
	@GetMapping("/menu")
	public String showMenu(Model model, HttpSession session) {
		
		String correoString = (String) session.getAttribute("usuario");
		
		if(correoString == null) {
			return "return:/login";
		}
		
		model.addAttribute("correo", session.getAttribute("usuario"));
		
		return "menu";
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	
	// Listar
	@GetMapping("/listar_usuario")
	public String showListarUsuarios(Model model) {
		List<UsuarioEntity> listaUsuario = usuarioRepository.findAll();
		model.addAttribute("listaUsuario", listaUsuario);
		return "listar_usuario";
	}
	
	// Registrar
	@GetMapping("/registrar_usuario")
	public String showRegistrarUsuario(Model model) {
		model.addAttribute("user", new UsuarioEntity());
		return "registrar_usuario";
	}
	
	@PostMapping("/registrar_usuario")
	public String registrarUsuario(@ModelAttribute UsuarioEntity usuario, Model model ) {
		
		if(usuarioRepository.findByCorreo(usuario.getCorreo()) != null) {
			model.addAttribute("errorMessage", "El correo electrónico ya está en uso");
			model.addAttribute("user", new UsuarioEntity());
			return "registrar_usuario";
		}
		
		usuario.setTipo(tipoRepository.findByNombre("cliente"));
		usuario.setEstado(true);
		
		usuarioRepository.save(usuario);
		
		return "redirect:/";
	}
	
	// Detalle
	@GetMapping("/detalle_usuario/{id}")
	public String verUsuario(Model model, @PathVariable("id") Integer id) {
		UsuarioEntity usuarioEncontrado= usuarioRepository.findById(id).get();
		model.addAttribute("user", usuarioEncontrado);
		return "detalle_usuario";
	}
	
	// Eliminar
	@GetMapping("/delete/{id}")
	public String eliminarUsuario( @PathVariable("id") Integer id) {
		usuarioRepository.deleteById(id);
		return "redirect:/";
	}
	
	// Editar
	@GetMapping("/editar_usuario/{id}")
	public String showeditarUsuario(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("user", usuarioRepository.findById(id).get());
		return "editar_usuario";
	}
	
	@PostMapping("/editar_usuario/{id}")
	public String editarUsuario(@ModelAttribute UsuarioEntity usuario, Model model, @PathVariable("id") Integer id ) {
		
		UsuarioEntity usuarioExistente = usuarioRepository.findById(id).get();
		
		
		if(usuario.getNombre() == null && usuario.getPassword() == null) {
			model.addAttribute("errorMessage", "El usuaro o contraseña no pueden estar vacios");
			model.addAttribute("user", usuarioExistente);
			model.addAttribute("id", id);
			return "editar_usuario";
		}
		
		usuarioExistente.setNombre(usuario.getNombre());
		usuarioExistente.setPassword(usuario.getPassword());
		
		usuarioRepository.save(usuarioExistente);
		
		return "redirect:/";
	}
}

