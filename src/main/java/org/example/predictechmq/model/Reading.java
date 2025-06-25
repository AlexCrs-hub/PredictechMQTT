package org.example.predictechmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Reading {
    private String sensorName;
    private Double measurement;
}
