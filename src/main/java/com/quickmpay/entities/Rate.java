package com.quickmpay.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "rate")
public class Rate {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int rateId;
private String rateAmount;
}
