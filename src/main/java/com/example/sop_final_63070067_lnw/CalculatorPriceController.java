package com.example.sop_final_63070067_lnw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorPriceController {
    @Autowired
    private CalculatorPriceService service;

    @GetMapping("/getPrice/{cost}/{profit}")
    public double serviceGetProducts(@PathVariable("cost") double cost, @PathVariable("profit") double profit) {
        return service.getPrice(cost, profit);
    }
}
