package org.MyProject.Jpa.Service;


import jakarta.persistence.Access;
import org.MyProject.Jpa.Entity.Products;
import org.MyProject.Jpa.Repository.ProductsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    public final Log log = LogFactory.getLog(getClass());

    @Autowired
    ProductsRepository productsRepository;

    public boolean saveProduct(Products products){
        Optional<Products> products1 = productsRepository.findProductsByProductname(products.getProductname());
        if(products1.isPresent()){
            log.info(String.format("ProductService{\t name: %s\t} - not saved", products.getProductname()));
            return false;
        }
        productsRepository.save(products);
        log.info(String.format("ProductService{\t name: %s\t} - save", products.getProductname()));

        return true;
    }

    public boolean deleteProduct(String name){
        Optional<Products> products = productsRepository.findProductsByProductname(name);
        if(products.isEmpty()){
            log.info(String.format("ProductService{\t name: %s\t} - not delete because not find", products.get().getProductname()));
            return false;
        }
        productsRepository.delete(products.get());

        log.info(String.format("ProductService{\t name: %s\t} - delete", products.get().getProductname()));
        return true;
    }

    public Map<String, Map<String, ?>> getAll(){
        List<Products> productsList = productsRepository.findAll();
        Map<String, Map<String, ?>> productMap = new HashMap<>();
        for (Products products : productsList){
            Map<String,?> stringMap = Map.of("id", products.getId(), "price", products.getPrice());
            productMap.put(products.getProductname(), stringMap);
        }
        return productMap;
    }

    public Products loadProductByName(String name){
        Products products = productsRepository.findProductsByProductname(name).get();
        return products;
    }
    public Products loadProductsById(Long id){
        Products products = productsRepository.findProductsById(id).get();
        return products;
    }

}
