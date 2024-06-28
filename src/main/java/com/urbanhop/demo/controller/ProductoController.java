package com.urbanhop.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.urbanhop.demo.entity.CategoriaEntity;
import com.urbanhop.demo.entity.ProductoEntity;
import com.urbanhop.demo.entity.TallaEntity;
import com.urbanhop.demo.service.CategoriaService;
import com.urbanhop.demo.service.ProductoService;
import com.urbanhop.demo.service.TallaService;

@Controller
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CategoriaService categoriaService;

	@Autowired	
	private TallaService tallaService;
	
	@GetMapping("/listar_producto")
	public String showListarProducto(Model model) {
		List<ProductoEntity> lstProductos = productoService.lstProductos();
		String rutaVista = "/mantener-producto/listar_producto";

		model.addAttribute("lstProductos", lstProductos);
		return rutaVista;
	}

	@GetMapping("/registrar_producto")
	public String viewRegistrarProducto(Model model) {
		List<CategoriaEntity> lstCategorias = categoriaService.lstCategorias();
		List<TallaEntity> lstTallas = tallaService.lstTallas();
		String rutaVista = "/mantener-producto/registrar_producto";
		
//		System.out.println(lstCategorias.get(0).getNombre());
		model.addAttribute("producto", new ProductoEntity());
		model.addAttribute("lstCategorias",lstCategorias);
		model.addAttribute("lstTallas",lstTallas);
		return rutaVista;
	}
	
	@PostMapping("registrar_producto")
	public String registrarProducto(ProductoEntity productoEntity,Model model) {
		String rutaVista = "/mantener-producto/registrar_producto";			
		List<CategoriaEntity> lstCategorias = categoriaService.lstCategorias();
		List<TallaEntity> lstTallas = tallaService.lstTallas();
		model.addAttribute("lstCategorias",lstCategorias);
		model.addAttribute("lstTallas",lstTallas);
		
		if (productoEntity.getCategoriaEntity().getCategoriaId() < 0) {
			model.addAttribute("message","No olvide elegir una categoria válida");
			model.addAttribute("classmessage","alert alert-danger");
			model.addAttribute("producto", productoEntity);
			return rutaVista;
		}
		
		if (productoEntity.getTallaEntity().getTallaId() < 0) {
			model.addAttribute("message","No olvide elegir una talla válida");
			model.addAttribute("classmessage","alert alert-danger");
			model.addAttribute("producto", productoEntity);
			return rutaVista;
		}
		
		productoService.crearProducto(productoEntity);
		
		model.addAttribute("message","Producto registrado correctamente");
		model.addAttribute("classmessage","alert alert-success");
		model.addAttribute("producto", new ProductoEntity());
		
		return rutaVista;
	}
	
	@GetMapping("/actualizar_producto/{id}")
	public String showActualizarProducto(@PathVariable("id") Integer id, Model model) {
		String rutaVista = "/mantener-producto/actualizar_producto";
		List<CategoriaEntity> lstCategorias = categoriaService.lstCategorias();
		List<TallaEntity> lstTallas = tallaService.lstTallas();
		ProductoEntity producto = productoService.findByProductoId(id);			
		
		model.addAttribute("lstCategorias",lstCategorias );
		model.addAttribute("lstTallas",lstTallas);
		model.addAttribute("producto",producto );
		
		return rutaVista;
	}
	
	@PostMapping("/actualizar_producto")
	public String actualizarProducto(ProductoEntity productoEntity, Model model) {
		String rutaVista = "/mantener-producto/actualizar_producto";
		
		
		//Añado estas dos listas para que se carguen cuando el programa va por la ruta 
		//de error en la seleccion de categoria y talla
		List<CategoriaEntity> lstCategorias = categoriaService.lstCategorias();
		List<TallaEntity> lstTallas = tallaService.lstTallas();
		model.addAttribute("lstCategorias",lstCategorias );
		model.addAttribute("lstTallas",lstTallas);
		
		if (productoEntity.getCategoriaEntity().getCategoriaId() < 0) {
			model.addAttribute("message","No olvide elegir una categoria válida");
			model.addAttribute("classmessage","alert alert-danger");
			model.addAttribute("producto", productoEntity);
			return rutaVista;
		}
		
		if (productoEntity.getTallaEntity().getTallaId() < 0) {
			model.addAttribute("message","No olvide elegir una talla válida");
			model.addAttribute("classmessage","alert alert-danger");
			model.addAttribute("producto", productoEntity);
			return rutaVista;
		}
		
		model.addAttribute("message","Producto actualizado correctamente");
		model.addAttribute("classmessage","alert alert-success");
		
		productoService.actualizarProducto(productoEntity);
		model.addAttribute("producto", productoEntity);
		
		return rutaVista;
	}
	
	
	
	@GetMapping("/eliminar_producto/{id}")
	public String eliminarProducto(@PathVariable("id") Integer id) {
	    productoService.eliminarProducto(id);
	    return "redirect:/listar_producto";
	}
	

	@GetMapping("/detalle_producto/{id}")
	public String verProducto(Model model, @PathVariable("id") Integer id) {
		ProductoEntity productoEncontrado= productoService.findByProductoId(id);
		
		
		model.addAttribute("producto", productoEncontrado);
		return "/mantener-producto/detalle_producto";
	}
	

	
}
