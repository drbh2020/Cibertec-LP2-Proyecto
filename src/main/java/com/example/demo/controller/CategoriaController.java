package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.CategoriaEntity;
import com.example.demo.entity.ProductoEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.TipoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.impl.PdfService;

@Controller
public class CategoriaController {
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private TipoRepository tipoRepository;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	
	@GetMapping("/mantenimiento_categoria")
	public String showMantenimientoCategoria(HttpSession session, Model model) {
		if (session.getAttribute("usuario") == null) {
			return "redirect:/";
		}

		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("nombre", usuarioEntity.getNombre());
		model.addAttribute("apellidos", usuarioEntity.getApellidos());

		List<CategoriaEntity> categorias = categoriaService.buscarTodasCategorias();
		model.addAttribute("categorias", categorias);

		return "mantenimiento_categoria";
	}
	
	@GetMapping("/nueva_categoria")
	public String showRegistrarCategoria(HttpSession session, Model model) {
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("nombre", usuarioEntity.getNombre());
		model.addAttribute("apellidos", usuarioEntity.getApellidos());

		model.addAttribute("categoria", new CategoriaEntity());
		
		return "registrar_categoria";
	}
	
	@PostMapping("/nueva_categoria")
	public String registrarCategoria(@ModelAttribute CategoriaEntity categoria, Model model) {
		categoriaService.save(categoria);
		return "redirect:/mantenimiento_categoria";
	}
	
	@GetMapping("/detalle_categoria/{id}")
	public String verProducto(HttpSession session, Model model, @PathVariable("id") Integer id) {
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("nombre", usuarioEntity.getNombre());
		model.addAttribute("apellidos", usuarioEntity.getApellidos());
		
	    CategoriaEntity categoriaEncontrada = categoriaService.buscarCategoriaPorId(id);
	    model.addAttribute("categoria", categoriaEncontrada);
	    return "detalle_categoria";
	}
	
	@GetMapping("/eliminar_categoria/{id}")
	public String eliminarCategoria(@PathVariable("id") Integer id) {
	    categoriaRepository.deleteById(id);
	    return "redirect:/mantenimiento_categoria";
	}
	
	@GetMapping("/editar_categoria/{id}")
    public String mostrarFormularioEditar(HttpSession session, @PathVariable("id") Integer id, Model model) {

		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		model.addAttribute("nombre", usuarioEntity.getNombre());
		model.addAttribute("apellidos", usuarioEntity.getApellidos());
		
		CategoriaEntity categoriaEncontrada = categoriaService.buscarCategoriaPorId(id);
        model.addAttribute("categoria", categoriaEncontrada);
        return "editar_categoria";
    }
	
	@PostMapping("/actualizar_categoria")
    public String actualizarProducto(@ModelAttribute("categoria") CategoriaEntity categoria, Model model) {
        try {
            categoriaService.save(categoria);
            return "redirect:/mantenimiento_categoria";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al actualizar el producto: " + e.getMessage());
            model.addAttribute("categoria", categoria);
            return "editar_categoria";
        }
    }

}
