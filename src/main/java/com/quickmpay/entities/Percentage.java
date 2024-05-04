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
@Entity
public class Percentage {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
private String percentageId;
private  String accountType;
private double percentage;


	
}
