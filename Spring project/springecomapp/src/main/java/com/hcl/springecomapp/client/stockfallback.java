package com.hcl.springecomapp.client;

import com.hcl.springecomapp.exception.InventoryServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class stockfallback implements StockClient{
    @Override
    public Integer generateStock() {
        throw new InventoryServiceUnavailableException("Inventory unavailable.");
    }
}
