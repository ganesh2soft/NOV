package com.hcl.springecomapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hcl.springecomapp.entity.Products;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.springecomapp.exception.APIException;
import com.hcl.springecomapp.exception.ResourceAlreadyExistsException;
import com.hcl.springecomapp.exception.ResourceNotFoundException;
import com.hcl.springecomapp.payload.ProductDTO;
import com.hcl.springecomapp.repository.ProductsRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductsRepository productRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@Override
	public ProductDTO addProduct(ProductDTO productDTO) {
		if (productRepository.existsByProductNameIgnoreCase(productDTO.getProductName())) {
			throw new ResourceAlreadyExistsException("Product", "productName", productDTO.getProductName());
		}
		Products product = modelMapper.map(productDTO, Products.class);
		Products savedProduct = productRepository.save(product);
		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		List<Products> products = productRepository.findAll();
		List<ProductDTO> productDTOS = products.stream().map(product -> {
			return modelMapper.map(product, ProductDTO.class);
		}

		).collect(Collectors.toList());
		return productDTOS;
	}

	@Override
	public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
		Optional<Products> productTobeUpdated = productRepository.findById(productId);
		Products prod = null;
		if (productTobeUpdated.get() != null) {
			prod = productTobeUpdated.get();
			prod.setCategory(productDTO.getCategory());
			prod.setProductName(productDTO.getProductName());
			prod.setImage(productDTO.getImage());
			prod.setBrandName(productDTO.getBrandName());
			prod.setQuantity(productDTO.getQuantity());
			prod.setPrice(productDTO.getPrice());
			prod.setDiscount(productDTO.getDiscount());
			prod.setSpecialPrice(productDTO.getSpecialPrice());
		}

		Products savedProduct = productRepository.save(prod);
		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	@Override
	public ProductDTO deleteProduct(Long productId) {
		Products product = productRepository.findById(productId)
				// .orElseThrow(() -> new RuntimeException("PRoduct NOT found with ID :" +
				// productId));
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		productRepository.delete(product);
		System.out.println("Product " + productId + " deleted successfully !!!");
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO getProductById(Long productId) {

		Products product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		return modelMapper.map(product, ProductDTO.class);

	}

	@Override
	public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
		Products productFromDB = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}

		String fileName = fileService.uploadImage(path, image);

		productFromDB.setImage(fileName);

		Products updatedProduct = productRepository.save(productFromDB);

		return modelMapper.map(updatedProduct, ProductDTO.class);
	}

	public List<ProductDTO> searchProductByKeyword(String keyword) {
        List<Products> products = productRepository.findByProductNameContainingIgnoreCase(keyword);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
}
