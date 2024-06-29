package org.MyProject.Jpa.Repository;

import org.MyProject.Jpa.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

    Optional<Products> findProductsByProductname(String product_name);
    Optional<Products> findProductsById(Long id);

}
