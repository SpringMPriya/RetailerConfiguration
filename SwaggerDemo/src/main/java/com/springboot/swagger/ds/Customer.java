package com.springboot.swagger.ds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "customer")
public class Customer {
    @Id
    private String id;

    private String name;

    private int rewards;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer",fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();

    public Customer() {
    }
}
