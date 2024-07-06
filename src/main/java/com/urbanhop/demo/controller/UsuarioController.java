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
import com.urbanhop.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
    private TipoRepository tipoRepository;
	

	
	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("usuario", new UsuarioEntity());
		String rutaVista = "/login/login";
		return rutaVista;
	}
	
	@PostMapping("/login")
	public String login(UsuarioEntity usuario, Model model, HttpSession session ) {
		boolean usuarioValidado = usuarioService.validarUsuario(usuario, session);
		
		if (usuarioValidado) {
			return "redirect:/";
		}
		
		model.addAttribute("loginIncorrecto", "Este usuario no existe");
		model.addAttribute("usuario", new UsuarioEntity());
		String rutaVista = "/login/login";
		return rutaVista;
	}
	
	// Registrar
	@GetMapping("/registrate")
	public String showRegistrate(Model model) {
		model.addAttribute("usuario", new UsuarioEntity());
		String rutaVista = "/login/registrate";
		return rutaVista;
	}
	
	@PostMapping("/registrate")
	public String registrate(@ModelAttribute UsuarioEntity usuario, Model model ) {
		
		String rutaVista = "/login/registrate";
		
		if(usuarioService.buscarUsuarioPorCorreo(usuario.getCorreo()) != null) {
			model.addAttribute("errorMessage", "El correo electrónico ya está en uso");
			model.addAttribute("usuario", new UsuarioEntity());
			return rutaVista;
		}
		
		usuario.setTipo(tipoRepository.findByNombre("cliente"));
		usuario.setEstado(true);
		
		usuarioService.crearUsuario(usuario, model);
		
		return "redirect:/login";
	}
	

	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	
	// Listar
	@GetMapping("/listar_usuario")
	public String showListarUsuarios(Model model) {
		List<UsuarioEntity> listaUsuario = usuarioService.listarUsuario();
		String rutaVista = "/mantener-usuario/listar_usuario";
		
		model.addAttribute("listaUsuario", listaUsuario);
		return rutaVista;
	}
	
	// Registrar
	@GetMapping("/registrar_usuario")
	public String showRegistrarUsuario(Model model) {
		List<TipoEntity> listaTipo = tipoRepository.findAll();
		model.addAttribute("usuario", new UsuarioEntity());
		model.addAttribute("tiposUsuario", listaTipo);
		String rutaVista = "/mantener-usuario/registrar_usuario";
		return rutaVista;
	}
	
	@PostMapping("/registrar_usuario")
	public String registrarUsuario(@ModelAttribute UsuarioEntity usuario, Model model ) {
		List<TipoEntity> listaTipo = tipoRepository.findAll();
		String rutaVista = "/mantener-usuario/registrar_usuario";
		
		if(usuarioService.buscarUsuarioPorCorreo(usuario.getCorreo()) != null) {
			model.addAttribute("errorMessage", "El correo electrónico ya está en uso");
			model.addAttribute("usuario", new UsuarioEntity());
	        model.addAttribute("tiposUsuario", listaTipo);
			return rutaVista;
		}
		
	    
	    if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
	        model.addAttribute("errorMessage", "El nombre y la contraseña no pueden estar vacíos");
	        model.addAttribute("usuario", new UsuarioEntity());
	        model.addAttribute("tiposUsuario", listaTipo);
	        return rutaVista;
	    }
		
		usuario.setEstado(true);
		
		usuarioService.crearUsuario(usuario, model);
		
		return "redirect:/listar_usuario";
	}
	
	// Detalle
	@GetMapping("/detalle_usuario/{id}")
	public String verUsuario(Model model, @PathVariable("id") String correo) {
		UsuarioEntity usuarioEncontrado= usuarioService.buscarUsuarioPorCorreo(correo);
		
		model.addAttribute("usuario", usuarioEncontrado);
		
		String rutaVista = "/mantener-usuario/detalle_usuario";
		return rutaVista;
	}
	
	// Eliminar
	@GetMapping("/eliminar_usuario/{id}")
	public String eliminarUsuario( @PathVariable("id") String correo) {
		usuarioService.eliminarUsuario(correo);
		return "redirect:/listar_usuario";
	}
	
	// Editar
	@GetMapping("/editar_usuario/{id}")
	public String showeditarUsuario(Model model, @PathVariable("id") String correo) {
		List<TipoEntity> listaTipo = tipoRepository.findAll();
		UsuarioEntity usuarioEncontrado = usuarioService.buscarUsuarioPorCorreo(correo);
		String rutaVista = "/mantener-usuario/actualizar_usuario";
		
		if (usuarioEncontrado == null) {
	        model.addAttribute("errorMessage", "Usuario no encontrado");
	        return "redirect:/listar_usuario";
	    }
		
		usuarioEncontrado.setPassword("");
		model.addAttribute("usuario", usuarioEncontrado);
		model.addAttribute("tiposUsuario", listaTipo);
		
		return rutaVista;
	}
	
	@PostMapping("/editar_usuario/{id}")
	public String editarUsuario(@ModelAttribute UsuarioEntity usuario, Model model, @PathVariable("id") String correo ) {
		
		UsuarioEntity usuarioExistente = usuarioService.buscarUsuarioPorCorreo(correo);
		String rutaVista = "/mantener-usuario/actualizar_usuario";
		UsuarioEntity usuarioEditado = usuario;
		usuarioEditado.setCorreo(correo);
	    
	    if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
	        model.addAttribute("errorMessage", "El nombre y la contraseña no pueden estar vacíos");
	        model.addAttribute("usuario", usuarioExistente);
	        model.addAttribute("correo", usuarioExistente.getCorreo());
	        model.addAttribute("tiposUsuario", tipoRepository.findAll());
	        return rutaVista;
	    }
	    
    
		usuarioService.actualizarProducto(usuarioEditado);
		
		return "redirect:/listar_usuario";
	}
}

