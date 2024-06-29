package org.MyProject.Jpa.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "right")
    public String rights;

    @Transient
    @OneToMany(mappedBy = "role")
    public List<Customers> customers;

    public Role(){}

    public Role(Long id, String rights){
        this.id = id;
        this.rights = rights;
    }

    @Override
    public String getAuthority() {
        return rights;
    }
}
