package com.example.sop_final_63070067_lnw;

import org.springframework.stereotype.Service;

@Service
public class CalculatorPriceService {
    public double getPrice(double cost, double profit) {
        return cost + profit;
    }
}