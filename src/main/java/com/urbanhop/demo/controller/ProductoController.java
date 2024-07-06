package com.urbanhop.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.urbanhop.demo.entity.DetallePedidoEntity;
import com.urbanhop.demo.entity.UsuarioEntity;
import com.urbanhop.demo.model.Pedido;
import com.urbanhop.demo.repository.CategoriaRepository;
import com.urbanhop.demo.repository.ProductoRepository;
import com.urbanhop.demo.repository.TipoRepository;
import com.urbanhop.demo.entity.TipoEntity;
import com.urbanhop.demo.service.impl.PdfService;
import com.urbanhop.demo.entity.CategoriaEntity;
import com.urbanhop.demo.entity.ProductoEntity;
import com.urbanhop.demo.entity.TallaEntity;
import com.urbanhop.demo.service.CategoriaService;
import com.urbanhop.demo.service.ProductoService;
import com.urbanhop.demo.service.TallaService;
import com.urbanhop.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired	
	private TallaService tallaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private TipoRepository tipoRepository;
	
	@Autowired
	private PdfService pdfService;
	
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("usuario", new UsuarioEntity());
        // Crear categorías si no existen
        List<CategoriaEntity> categorias = categoriaService.lstCategorias();
        if (categorias.isEmpty()) {
            CategoriaEntity categoriaHombre = new CategoriaEntity(null, "Hombre");
            CategoriaEntity categoriaNiños = new CategoriaEntity(null, "Niños");
            CategoriaEntity categoriaMujer = new CategoriaEntity(null, "Mujer");
            categoriaRepository.saveAll(Arrays.asList(categoriaHombre, categoriaNiños, categoriaMujer));
            categorias = Arrays.asList(categoriaHombre, categoriaNiños, categoriaMujer);
        }
        model.addAttribute("categorias", categorias);

        // Crear productos si no existen
        List<ProductoEntity> productos = productoService.lstProductos();
        
        if (productos.isEmpty()) {
        	List<TallaEntity> tallas = tallaService.lstTallas();
        	
            // Obtener las categorías creadas para asociarlas correctamente
            CategoriaEntity categoriaHombre = categorias.get(0);
            CategoriaEntity categoriaNiños = categorias.get(1);
            CategoriaEntity categoriaMujer = categorias.get(2);
            
            TallaEntity tallaL = tallas.get(0);
            TallaEntity tallaM = tallas.get(1);
            TallaEntity tallaS = tallas.get(2);

            ProductoEntity productoPoloHombre = new ProductoEntity(1, "Polo Básico Hombre",  100, 40.50, true, tallaL, categoriaHombre, "https://realplaza.vtexassets.com/arquivos/ids/22654364-800-auto?v=637853060342370000&width=800&height=auto&aspect=true");
            ProductoEntity productoPoloNiño = new ProductoEntity(2, "Polo Básico Niño", 100, 36.50, true, tallaS, categoriaNiños, "https://realplaza.vtexassets.com/arquivos/ids/22654364-800-auto?v=637853060342370000&width=800&height=auto&aspect=true");
            ProductoEntity productoPoloMujer = new ProductoEntity(3, "Polo Básico Mujer", 100, 52.00, true, tallaM, categoriaMujer, "https://realplaza.vtexassets.com/arquivos/ids/22654364-800-auto?v=637853060342370000&width=800&height=auto&aspect=true");
            
            productoRepository.saveAll(Arrays.asList(productoPoloHombre, productoPoloNiño, productoPoloMujer));
            productos = Arrays.asList(productoPoloHombre, productoPoloNiño, productoPoloMujer);
        }
        
        model.addAttribute("productos", productos);
        
        List<TipoEntity> tipos = tipoRepository.findAll();
        if(tipos.isEmpty()) {
        	TipoEntity tipoAdministrador = new TipoEntity(null, "Administrador");
        	TipoEntity tipoCliente = new TipoEntity(null, "Cliente");
        	tipoRepository.saveAll(Arrays.asList(tipoAdministrador,tipoCliente));
        	tipos =  Arrays.asList(tipoAdministrador, tipoCliente);
        }
        
        model.addAttribute("tipos", tipos);
        
        List<UsuarioEntity> usuarios = usuarioService.listarUsuario();
        if (usuarios.isEmpty()) {
            TipoEntity tipoAdministrador = tipos.get(0);

            UsuarioEntity usuarioAdmin = new UsuarioEntity("admin@urbanhop", "123", "Rodrigo", "Pereda", true, tipoAdministrador);
            usuarioService.crearUsuario(usuarioAdmin, model);
            model.addAttribute("registroCorrecto", "Se creó el usuario administrador");
        }
        return "home";
	}
	
	@GetMapping("/menu")
	public String showMenu(HttpSession session, Model model) {
	    if(session.getAttribute("usuario") == null) {
	        return "redirect:/";
	    }

	    String correo = session.getAttribute("usuario").toString();
	    UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
	    
	    List<CategoriaEntity> categorias = categoriaRepository.findAll();
	    model.addAttribute("categorias", categorias);
	    // Obtener y agregar productos al modelo
	    List<ProductoEntity> productos = productoRepository.findAll();
	    

	    model.addAttribute("productos", productos);

	    List<Pedido> productoSession = null;
	    if(session.getAttribute("carrito") == null) {
	        productoSession = new ArrayList<Pedido>();
	    } else {
	        productoSession = (List<Pedido>) session.getAttribute("carrito");
	    }
	    model.addAttribute("cant_carrito", productoSession.size());

	    // ver carrito con datos
	    List<DetallePedidoEntity> detallePedidoEntityList = new ArrayList<DetallePedidoEntity>();
	    Double total = 0.0;

	    for(Pedido pedido: productoSession) {
	        DetallePedidoEntity detallePedidoEntity = new DetallePedidoEntity();
	        ProductoEntity productoEntity = productoService.buscarProductoPorId(pedido.getProductoId());
	        detallePedidoEntity.setProductoEntity(productoEntity);
	        detallePedidoEntity.setCantidad(pedido.getCantidad());
	        detallePedidoEntityList.add(detallePedidoEntity);
	        total += pedido.getCantidad() * productoEntity.getPrecio();
	    }
	    model.addAttribute("carrito", detallePedidoEntityList);
	    model.addAttribute("total", total);

	    return "menu";
	}
	
	@PostMapping("/agregar_producto")
	public String agregarProducto(HttpSession session, @RequestParam("prodId") String prod,
			@RequestParam("cant") String cant) {

		List<Pedido> productos = null;
		if(session.getAttribute("carrito") == null) {
			productos = new ArrayList<>();
		}else {
			productos = (List<Pedido>) session.getAttribute("carrito");
		}

		Integer cantidad = Integer.parseInt(cant);
		Integer prodId = Integer.parseInt(prod);
		Pedido pedido = new Pedido(cantidad, prodId);
		productos.add(pedido);
		session.setAttribute("carrito", productos);
		return "redirect:/menu";

	}
	
	
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
	
	
	@GetMapping("/generar_pdf")
	public ResponseEntity<InputStreamResource>generarPdf(HttpSession session) throws IOException{
		List<Pedido>productoSession = null;
		if(session.getAttribute("carrito") == null) {
			productoSession = new ArrayList<Pedido>();
		}else {
			productoSession = (List<Pedido>) session.getAttribute("carrito");
		}
		List<DetallePedidoEntity> detallePedidoEntityList = new ArrayList<DetallePedidoEntity>();
		Double total = 0.0;

		for(Pedido pedido: productoSession) {
			DetallePedidoEntity detallePedidoEntity = new DetallePedidoEntity();
			ProductoEntity productoEntity = productoService.buscarProductoPorId(pedido.getProductoId());
			detallePedidoEntity.setProductoEntity(productoEntity);
			detallePedidoEntity.setCantidad(pedido.getCantidad());
			detallePedidoEntityList.add(detallePedidoEntity);
			total += pedido.getCantidad() * productoEntity.getPrecio();
		}
		
		String correo = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorCorreo(correo);
		String nombre = usuarioEntity.getNombre();
		String apellido = usuarioEntity.getApellido();
		
		String nombreCompletoUsuario = nombre+" "+apellido;

		Map<String, Object>datosPdf = new HashMap<String, Object>();
		datosPdf.put("factura", detallePedidoEntityList);
		datosPdf.put("precio_total", total);
		datosPdf.put("nombreCompletoUsuario", nombreCompletoUsuario);
		

		ByteArrayInputStream pdfBytes = pdfService.generarPdfDeHtml("template_pdf", datosPdf);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "inline; filename=productos.pdf");

		session.removeAttribute("carrito");
		detallePedidoEntityList.clear();
		productoSession.clear();
		
		return ResponseEntity.ok()
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdfBytes));
		
		
		
	}

	
}
