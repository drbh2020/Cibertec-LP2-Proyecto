package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.impl.PdfService;

@Controller
public class ProductoController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private PdfService pdfService;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("usuario", new UsuarioEntity());
		return "home";
	}
}
