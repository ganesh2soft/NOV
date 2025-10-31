package com.hcl.springecomapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hcl.springecomapp.payload.ProductDTO;

public interface ProductService {
	public ProductDTO addProduct(ProductDTO product);

	public List<ProductDTO> getAllProducts();

	public ProductDTO updateProduct(Long productId, ProductDTO productDTO);

	public ProductDTO deleteProduct(Long productId);

	public List<ProductDTO> searchProductByKeyword(String keyword);
	
	public ProductDTO getProductById(Long productId);
	public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
	
}


