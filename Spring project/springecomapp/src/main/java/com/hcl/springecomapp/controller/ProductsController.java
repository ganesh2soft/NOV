package com.hcl.springecomapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.springecomapp.payload.ProductDTO;
import com.hcl.springecomapp.service.ProductService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductsController {

	@Autowired
	ProductService productService;

	@GetMapping("/public/hello")
	public String helloMethod() {
		return "Hello  All ";
	}

	@PostMapping("/admin/product")
	@PreAuthorize(value = "ROLE_ADMIN")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
		System.out.println("To Add" + productDTO);
		ProductDTO savedProductDTO = productService.addProduct(productDTO);
		return new ResponseEntity<ProductDTO>(savedProductDTO, HttpStatus.CREATED);

	}

	@GetMapping("/public/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts() {
		List<ProductDTO> prodList = productService.getAllProducts();
		return new ResponseEntity<List<ProductDTO>>(prodList, HttpStatus.OK);
	}
	//localhost:8082/api/admin/products/1/image
	@PreAuthorize(value = "ROLE_ADMIN")
	@PutMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
		ProductDTO savedProductDTO = productService.updateProduct(productId, productDTO);
		return new ResponseEntity<ProductDTO>(savedProductDTO, HttpStatus.OK);
	}
	
	
	//localhost:8082/api/products/image/22809cb8-4cd8-4514-b879-0b0da7a29ea1.jpg
	@PreAuthorize(value = "ROLE_ADMIN")
	@PutMapping(value="/admin/products/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
			@RequestParam("image") MultipartFile image) throws IOException {
		ProductDTO updatedProduct = productService.updateProductImage(productId, image);

		return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
	}
	
	@GetMapping(value="/public/products/image/{filename}" , produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
	    Path imagePath = Paths.get("images", filename);

	    if (!Files.exists(imagePath)) {
	        return ResponseEntity.notFound().build();
	    }

	    Resource resource = new UrlResource(imagePath.toUri());
	    String contentType = Files.probeContentType(imagePath);
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .body(resource);
	}
	//localhost:8082/api/public/products/3
	@GetMapping("/public/products/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
		ProductDTO prod = productService.getProductById(productId);
		return new ResponseEntity<ProductDTO>(prod, HttpStatus.OK);
	}
	@PreAuthorize(value = "ROLE_ADMIN")
	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {

		ProductDTO prod = productService.deleteProduct(productId);
		return new ResponseEntity<>(prod, HttpStatus.OK);
	}
	//localhost:8082/api/public/products/search?keyword=spin
	@GetMapping("/public/products/search")
	public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam("keyword") String keyword) {
	    List<ProductDTO> result = productService.searchProductByKeyword(keyword);
	    return ResponseEntity.ok(result);
	}
}
