package com.hcl.springecomapp.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "STOCKSERVICE")
public interface StockClient {

    @GetMapping("/api/stock/generate")
    Integer generateStock();
}


