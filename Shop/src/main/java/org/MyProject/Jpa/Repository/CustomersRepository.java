package org.MyProject.Jpa.Repository;


import org.MyProject.Jpa.Entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {


    Optional<Customers> findCustomersByUsername(String username);
}
