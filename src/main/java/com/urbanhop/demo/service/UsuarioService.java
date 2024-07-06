package com.urbanhop.demo.service;

import java.util.List;

import org.springframework.ui.Model;

import com.urbanhop.demo.entity.UsuarioEntity;

import jakarta.servlet.http.HttpSession;

public interface UsuarioService {
	List<UsuarioEntity> listarUsuario();
	void crearUsuario(UsuarioEntity usuarioEntity, Model model);
	UsuarioEntity actualizarProducto(UsuarioEntity productoEntity);
	void eliminarUsuario(String correo);
	boolean validarUsuario(UsuarioEntity usuarioEntity, HttpSession session);
	UsuarioEntity buscarUsuarioPorCorreo(String correo);
}
