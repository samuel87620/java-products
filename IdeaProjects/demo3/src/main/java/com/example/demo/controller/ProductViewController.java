package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;

    // 使用建構子注入
    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    // 顯示所有商品
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";  // 確保有一個名為 'products.html' 的 Thymeleaf 模板
    }

    // 顯示新增商品表單
    @GetMapping("/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new ProductDto());
        return "product_form";  // 確保有一個名為 'product_form.html' 的 Thymeleaf 模板
    }

    // 顯示更新商品表單
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        ProductDto productDto = productService.getProductById(id);
        model.addAttribute("product", productDto);
        return "product_form";
    }

    // 處理新增或更新商品的表單提交
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto) {
        if (productDto.getId() != null) {
            productService.updateProduct(productDto.getId(), productDto);
        } else {
            productService.createProduct(productDto);
        }
        return "redirect:/products";
    }

    // 刪除商品
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
