package com.urbanhop.demo.controller;

import com.urbanhop.demo.entity.CategoriaEntity;
import com.urbanhop.demo.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listCategorias(Model model) {
        List<CategoriaEntity> categorias = categoriaService.lstCategorias();
        model.addAttribute("categorias", categorias);
        return "mantener-categoria/listar_categoria";
    }

    @GetMapping("/new")
    public String showCategoriaForm(Model model) {
        CategoriaEntity categoria = new CategoriaEntity();
        model.addAttribute("categoria", categoria);
        return "mantener-categoria/registrar_categoria";
    }

    @PostMapping
    public String saveCategoria(@ModelAttribute("categoria") CategoriaEntity categoria) {
        categoriaService.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        CategoriaEntity categoria = categoriaService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid categoria Id:" + id));
        model.addAttribute("categoria", categoria);
        return "mantener-categoria/actualizar_categoria";
    }

    @PostMapping("/update/{id}")
    public String updateCategoria(@PathVariable("id") Long id, @ModelAttribute("categoria") CategoriaEntity categoria) {
        categoriaService.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategoria(@PathVariable("id") Long id) {
        categoriaService.deleteById(id);
        return "redirect:/categorias";
    }
}
