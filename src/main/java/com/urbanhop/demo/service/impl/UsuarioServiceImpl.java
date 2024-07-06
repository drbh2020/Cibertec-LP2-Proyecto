package com.urbanhop.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.urbanhop.demo.entity.UsuarioEntity;
import com.urbanhop.demo.repository.UsuarioRepository;
import com.urbanhop.demo.service.UsuarioService;
import com.urbanhop.demo.utils.Utilitarios;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public void crearUsuario(UsuarioEntity usuarioEntity, Model model) {
		
		String passwordHash = Utilitarios.extraerHash(usuarioEntity.getPassword());
		usuarioEntity.setPassword(passwordHash);
		
		usuarioRepository.save(usuarioEntity);
		
		model.addAttribute("registroCorrecto", "Registro Correcto");
		model.addAttribute("usuario", new UsuarioEntity());
	}

	@Override
	public boolean validarUsuario(UsuarioEntity usuarioEntity, HttpSession session) {
		
		UsuarioEntity usuarioEncontradoPorCorreo = usuarioRepository.findById(usuarioEntity.getCorreo()).get();
		
		if(usuarioEncontradoPorCorreo == null) {
			return false;
		}
		
		if(!Utilitarios.checkPassword(usuarioEntity.getPassword(), usuarioEncontradoPorCorreo.getPassword())) {
			return false;
		}
		
		session.setAttribute("usuario", usuarioEncontradoPorCorreo.getCorreo());
		
		return true;
		
	}

	@Override
	public UsuarioEntity buscarUsuarioPorCorreo(String correo) {
		return usuarioRepository.findById(correo).isEmpty() ? null : usuarioRepository.findById(correo).get();
	}

	@Override
	public UsuarioEntity actualizarProducto(UsuarioEntity usuarioEntity) {
		UsuarioEntity usuarioBuscado = buscarUsuarioPorCorreo(usuarioEntity.getCorreo());
		
		if(usuarioBuscado != null) { 
			if (usuarioEntity.getPassword() != null && !usuarioEntity.getPassword().isEmpty()) {
	            String passwordHash = Utilitarios.extraerHash(usuarioEntity.getPassword());
	            usuarioBuscado.setPassword(passwordHash);
	        }
			usuarioBuscado.setNombre(usuarioEntity.getNombre());
			usuarioBuscado.setApellido(usuarioEntity.getApellido());
			usuarioBuscado.setTipo(usuarioEntity.getTipo());
			usuarioBuscado.setEstado(usuarioEntity.getEstado());
			return usuarioRepository.save(usuarioBuscado);
		}
		return null;
	}

	@Override
	public void eliminarUsuario(String correo) {
		usuarioRepository.deleteById(correo);
	}

	@Override
	public List<UsuarioEntity> listarUsuario() {
		List<UsuarioEntity> listaUsuarios = new ArrayList<>();
	    return usuarioRepository.findAll() != null ? usuarioRepository.findAll() : listaUsuarios;
	}

}
