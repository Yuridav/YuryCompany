package org.MyProject.Jpa.Service;


import org.MyProject.Jpa.Entity.Customers;
import org.MyProject.Jpa.Entity.Role;
import org.MyProject.Jpa.Repository.CustomersRepository;
import org.MyProject.Jpa.Repository.OrdersRepository;
import org.MyProject.Jpa.Repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    CustomersRepository customersRepository;


    public boolean saveCustomers(Customers customers, BCryptPasswordEncoder bCryptPasswordEncoder) {
        Optional<Customers> customers1 = customersRepository.findCustomersByUsername(customers.getUsername());
        if (customers1.isPresent()) {
            System.out.println("\tError save user");
            throw new UsernameNotFoundException("User already register");
        }
        System.out.println("passwordEncoder" + customers.getPassword());
        customers.setPassword(bCryptPasswordEncoder.encode(customers.password));
        customersRepository.save(customers);
        System.out.println("\tSave user");
        return true;
    }

    public boolean updateCustomers(Customers customers){
        Optional<Customers> customers1 = customersRepository.findCustomersByUsername(customers.getUsername());
        if(customers1.isPresent()){
            customersRepository.save(customers);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Customers> customers = customersRepository.findCustomersByUsername(username);
        if(customers.isEmpty()){
            throw new UsernameNotFoundException("Not Found User by Username");
        }
        return customers.get();
    }

}
