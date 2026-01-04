package com.gocart.controller;

import com.gocart.service.ProductService;
import com.gocart.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("stores", storeService.getAllStores());
        return "index";
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "shop";
    }

    @GetMapping("/shop/{username}")
    public String storeProducts(@PathVariable String username, Model model) {
        model.addAttribute("store", storeService.getStoreByUsername(username));
        return "store-products";
    }

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable String productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        return "product-detail";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/pricing")
    public String pricing() {
        return "pricing";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/dashboard";
    }

    @GetMapping("/store")
    public String storeDashboard() {
        return "store/dashboard";
    }
}
