package com.example.demo.service;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UsuarioEntity;

public interface UsuarioService {
	void crearUsuario(UsuarioEntity usuarioEntity, Model model, MultipartFile foto);
	boolean validarUsuario(UsuarioEntity usuarioEntity, HttpSession session);
	UsuarioEntity buscarUsuarioPorCorreo(String correo);
}
