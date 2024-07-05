package com.example.demo.service.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;
import com.example.demo.utils.Utilitarios;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public void crearUsuario(UsuarioEntity usuarioEntity, Model model, MultipartFile foto) {
		String nombreFoto = Utilitarios.guardarImagen(foto);
		usuarioEntity.setUrlImagen(nombreFoto);
		 
		String passwordHash = Utilitarios.extraerHash(usuarioEntity.getPassword());
		usuarioEntity.setPassword(passwordHash);
		
		usuarioRepository.save(usuarioEntity);
		

				model.addAttribute("registroCorrecto", "Registro Correcto");
				model.addAttribute("usuario", new UsuarioEntity());
		
	}

	@Override
	public boolean validarUsuario(UsuarioEntity usuarioEntity, HttpSession session) {
		UsuarioEntity usuarioEncontradoPorcCorreo = 
				usuarioRepository.findByCorreo(usuarioEntity.getCorreo());
		

		if(usuarioEncontradoPorcCorreo == null) {
			return false;
		}
	
		if(!Utilitarios.checkPassword(usuarioEntity.getPassword(), 
				usuarioEncontradoPorcCorreo.getPassword())) {
			return false;
		}
		
		session.setAttribute("usuario", usuarioEncontradoPorcCorreo.getCorreo());
		
		
		return true;
	}

	@Override
	public UsuarioEntity buscarUsuarioPorCorreo(String correo) {
		return usuarioRepository.findByCorreo(correo);
	}
	

	
	
}
