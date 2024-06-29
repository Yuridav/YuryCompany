package org.MyProject.Jpa.Controllers;


import com.mysql.cj.conf.PropertyDefinitions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.MyProject.Jpa.Entity.Customers;
import org.MyProject.Jpa.Entity.Products;
import org.MyProject.Jpa.Entity.Role;
import org.MyProject.Jpa.Repository.CustomersRepository;
import org.MyProject.Jpa.Service.CustomerService;
import org.MyProject.Jpa.Service.PayService;
import org.MyProject.Jpa.Service.ProductService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
import java.awt.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerService customerService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ProductService productService;

    @Autowired
    PayService payService;


    public final Log loger = LogFactory.getLog(getClass());
    public static SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();


    @PostMapping(value = "/reg")
    public String Register(@RequestBody RequestReg customers1) {
        Customers customers = new Customers(customers1.getUsername(), customers1.getPassword(),
                                        new Role(2L,"USER"), Customers.Statuses.WORKED);

        boolean save =customerService.saveCustomers(customers, passwordEncoder);

        loger.info(String.format("Registe{\t Username: %s register:%b \t}", customers.getUsername(), save));
        return customers.username;
    }

    @GetMapping("/login1")
    public ResponseEntity<Map<String,?>> Login(@RequestBody RequestReg log, HttpServletResponse response, HttpServletRequest request){
        System.out.println("Login");
        Customers customers = (Customers) customerService.loadUserByUsername(log.getUsername());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(customers.getUsername(), log.getPassword());

        securityContext.setAuthentication(authenticationManager.authenticate(authentication));
        strategy.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);

        loger.info(String.format("Login{\t Username: %s \t}", customers.getUsername()));

        return new ResponseEntity(Map.of("Authorize", authentication.getPrincipal()), HttpStatus.OK);
    }

    @PutMapping("/addMoney")
    public ResponseEntity<Map<String,?>> addMoneyInCustomer(HttpServletRequest request, @RequestParam double money){
       String username =  securityContextRepository.loadDeferredContext(request).get().getAuthentication().getName();

       Customers customers = (Customers) customerService.loadUserByUsername(username);
       customers.setMoney(customers.getMoney() + money);
       customerService.updateCustomers(customers);
       loger.info(String.format("Update Money{\tCustomer: %s \tMoney add: %f \tMoney: %f\t}", customers.getUsername(), money, customers.getMoney()));
       return new ResponseEntity(Map.of("Money", ((Customers) customerService.loadUserByUsername(username)).getMoney()), HttpStatus.OK);

    }



    @GetMapping("/getName")
    public Map<String,String> getName( HttpServletRequest request){
        return Map.of("Name", "hello");
    }

    @GetMapping("/buy/{id}")
    public Map<String, ?> productPage(@PathVariable String id){
        Products products = productService.loadProductsById(Long.parseLong(id));
        return Map.of("Product", products);
    }

    @PostMapping("/products/buy/{id}/{username}")
    public Map<String, ?> ProcessBuy(@PathVariable String id, @PathVariable String username){
        Customers customers = (Customers) customerService.loadUserByUsername(username);
        Products products = productService.loadProductsById(Long.parseLong(id));
        boolean status = payService.Buy(customers, products);
        return Map.of("Status", status, "Customers", List.of(customers.getUsername(),
                customers.getMoney(),
                customers.getStatus()), "Products", List.of(products.getProductname(),
                products.getPrice()));
    }

    @GetMapping("/products")
    public Map<String, ?> getAllProducts(){
        return productService.getAll();
    }




}
class RequestReg{
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String username;
    public String password;
}