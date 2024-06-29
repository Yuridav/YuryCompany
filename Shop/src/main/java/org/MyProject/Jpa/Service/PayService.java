package org.MyProject.Jpa.Service;


import org.MyProject.Jpa.Entity.Customers;
import org.MyProject.Jpa.Entity.Orders;
import org.MyProject.Jpa.Entity.Products;
import org.MyProject.Jpa.Repository.CustomersRepository;
import org.MyProject.Jpa.Repository.OrdersRepository;
import org.MyProject.Jpa.Repository.ProductsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayService {

    @Autowired
    CustomersRepository customersRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    CustomerService customerService;

    public final Log loger = LogFactory.getLog(getClass());

    public Customers loadCustomers(String username){
        Optional<Customers> customers = customersRepository.findCustomersByUsername(username);
        System.out.println("\tloadCustomers");
        return customers.get();
    }

    public Products loadProduct(String product_name){
        System.out.println("\tloadProduct");
        return productsRepository.findProductsByProductname(product_name).get();
    }

    public boolean Buy(String username, String product_name){
        Customers customers = loadCustomers(username);
        Products products = loadProduct(product_name);

        double result = customers.getMoney() - products.getPrice();

        if(result >= 0){
            customers.setMoney(result);
            customerService.updateCustomers(customers);
            ordersRepository.save(new Orders(products, customers));
            loger.info(String.format("PayService Method: Buy {\t Username: %s Product name: %s result = %f \t} - success",
                    customers.getUsername(), products.getProductname(), result));
            return true;
        }
        loger.info(String.format("PayService Method: Buy {\t Username: %s Product name: %s result = %f \t} -    error",
                customers.getUsername(), products.getProductname(), result));
        return false;

    }

    public boolean Buy(Customers customers , Products products){
        double result = customers.getMoney() - products.getPrice();
        if(result >= 0){
            customers.setMoney(result);
            customerService.updateCustomers(customers);
            ordersRepository.save(new Orders(products, customers));
            loger.info(String.format("PayService Method: Buy {\t Username: %s Product name: %s result = %f \t} - success",
                                                            customers.getUsername(), products.getProductname(), result));
            return true;
        }
        loger.info(String.format("PayService Method: Buy {\t Username: %s Product name: %s result = %f \t} -    error",
                customers.getUsername(), products.getProductname(), result));
        return false;
    }
}
