package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.ClientEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.CategoriaEntity;
import com.example.demo.entity.DetallePedidoEntity;
import com.example.demo.entity.ProductoEntity;
import com.example.demo.entity.TipoEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.model.Pedido;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.TipoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.impl.PdfService;
import com.example.demo.service.impl.UsuarioServiceImpl;

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
	private TipoRepository tipoRepository;

	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("usuario", new UsuarioEntity());
        // Crear categorías si no existen
        List<CategoriaEntity> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            CategoriaEntity categoriaHombre = new CategoriaEntity(null, "Hombre");
            CategoriaEntity categoriaNiños = new CategoriaEntity(null, "Niños");
            CategoriaEntity categoriaMujer = new CategoriaEntity(null, "Mujer");
            categoriaRepository.saveAll(Arrays.asList(categoriaHombre, categoriaNiños, categoriaMujer));
            categorias = Arrays.asList(categoriaHombre, categoriaNiños, categoriaMujer);
        }
        model.addAttribute("categorias", categorias);

        // Crear productos si no existen
        List<ProductoEntity> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            // Obtener las categorías creadas para asociarlas correctamente
            CategoriaEntity categoriaHombre = categorias.get(0);
            CategoriaEntity categoriaNiños = categorias.get(1);
            CategoriaEntity categoriaMujer = categorias.get(2);

            ProductoEntity productoPoloHombre = new ProductoEntity(null, "Polo Básico Hombre", 40.50, 100, categoriaHombre, "https://realplaza.vtexassets.com/arquivos/ids/22654364-800-auto?v=637853060342370000&width=800&height=auto&aspect=true");
            ProductoEntity productoPoloNiño = new ProductoEntity(null, "Polo Básico Niño", 36.50, 100, categoriaNiños, "https://realplaza.vtexassets.com/arquivos/ids/22654364-800-auto?v=637853060342370000&width=800&height=auto&aspect=true");
            ProductoEntity productoPoloMujer = new ProductoEntity(null, "Polo Básico Mujer", 52.00, 100, categoriaMujer, "https://realplaza.vtexassets.com/arquivos/ids/22654364-800-auto?v=637853060342370000&width=800&height=auto&aspect=true");
            
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
        
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            TipoEntity tipoAdministrador = tipos.get(0);

            UsuarioEntity usuarioAdmin = new UsuarioEntity("admin@urbanhop", "123", "Rodrigo", "Pereda", tipoAdministrador);
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
	public String agregarProducto(HttpSession session,@RequestParam("prodId") String prod,
			@RequestParam("cant") String cant) {

		List<Pedido> productos = null;
		if(session.getAttribute("carrito") == null) {
			productos = new ArrayList<>();
		}else {
			productos = (List<Pedido>) session.getAttribute("carrito");
		}

		Integer cantidad = Integer.parseInt(cant);
		Long prodId = Long.parseLong(prod);
		Pedido pedido = new Pedido(cantidad, prodId);
		productos.add(pedido);
		session.setAttribute("carrito", productos);
		return "redirect:/menu";

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
		String apellido = usuarioEntity.getApellidos();
		
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
