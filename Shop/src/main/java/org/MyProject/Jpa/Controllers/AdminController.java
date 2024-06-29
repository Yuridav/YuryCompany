package org.MyProject.Jpa.Controllers;


import org.MyProject.Jpa.Entity.Customers;
import org.MyProject.Jpa.Entity.Products;
import org.MyProject.Jpa.Service.CustomerService;
import org.MyProject.Jpa.Service.ProductService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    CustomerService customerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ProductService productService;

    public final Log loger = LogFactory.getLog(getClass());

    @PutMapping("/ban")
    public Map<String , ?> BanCustomer(@RequestParam String username){
        Customers customers = (Customers) customerService.loadUserByUsername(username);
        customers.setStatus(Customers.Statuses.BAN);
        customerService.updateCustomers(customers);

        loger.info(String.format("BAN{\t Username: %s Status: %s\t}", customers.getUsername(), customers.getStatus()));
        return Map.of("Status:", ((Customers) customerService.loadUserByUsername(username)).getStatus());
    }

    @PutMapping("/unban")
    public Map<String , ?> UnbanCustomer(@RequestParam String username){
        Customers customers = (Customers) customerService.loadUserByUsername(username);
        customers.setStatus(Customers.Statuses.WORKED);
        customerService.updateCustomers(customers);
        loger.info(String.format("UNBAN{\t Username: %s Status: %s\t}", customers.getUsername(), customers.getStatus()));
        return Map.of("Status:", ((Customers) customerService.loadUserByUsername(username)).getStatus());
    }

    @PostMapping("/addProduct")
    public Map<String, ?> addProduct(@RequestBody Products products){
        productService.saveProduct(products);
        return Map.of("Save Product:", products);
    }

    @PostMapping("/deleteProduct")
    public Map<String, ?> deleteProduct(@RequestBody Products products){
        productService.deleteProduct(products.getProductname());
        return Map.of("delete Product:", products);
    }


}
