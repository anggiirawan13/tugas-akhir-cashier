package com.be.app.impl;

import com.be.app.dto.request.ProductInsertRequest;
import com.be.app.dto.request.ProductUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.entity.ProductEntity;
import com.be.app.repository.ProductRepository;
import com.be.app.service.ProductService;
import com.be.constanta.ResponseMessagesConst;
import com.be.constanta.StatusConst;
import com.be.handler.InternalServerErrorHandler;
import com.be.helper.DateHelper;
import com.be.helper.NullEmptyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public BaseResponse saveProduct(ProductInsertRequest productRequest, MultipartFile file) {
        try {
            File uploadDir = new File("./");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            String fullPath = "./" + fileName;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fullPath);
            Files.write(path, bytes);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/files/download/")
                    .path(fileName)
                    .toUriString();

            ProductEntity newProduct = new ProductEntity();
            newProduct.setUuid(UUID.randomUUID().toString());
            newProduct.setProductCode(productRequest.getProductCode());
            newProduct.setProductName(productRequest.getProductName());
            newProduct.setPrice(productRequest.getPrice());
            newProduct.setStock(productRequest.getStock());
            newProduct.setThumbnail(fileDownloadUri);
            newProduct.setCategoryID(productRequest.getCategoryID());

            if (productRequest.getStatus().equalsIgnoreCase("active")) {
                newProduct.setStatus(StatusConst.ACTIVE.toString());
            } else {
                newProduct.setStatus(StatusConst.INACTIVE.toString());
            }

            Timestamp dateNow = DateHelper.getTimestampNow();

            newProduct.setCreatedAt(dateNow);
            newProduct.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            newProduct.setModifiedAt(dateNow);
            newProduct.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

            ProductEntity listNewProduct = productRepository.save(newProduct);

            return new BaseResponse(true, ResponseMessagesConst.INSERT_SUCCESS.toString(), listNewProduct);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse updateProductByUUID(String uuid, ProductUpdateRequest productRequest, MultipartFile file) {
        try {
            ProductEntity oldProduct = productRepository.findByUUID(uuid);
            if (NullEmptyChecker.isNullOrEmpty(oldProduct)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString(), null);
            }

            File uploadDir = new File("./");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            String fullPath = "./" + fileName;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fullPath);
            Files.write(path, bytes);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/files/download/")
                    .path(fileName)
                    .toUriString();

            ProductEntity updateProduct = productRepository.findByUUID(uuid);
            updateProduct.setProductCode(productRequest.getProductCode());
            updateProduct.setProductName(productRequest.getProductName());
            updateProduct.setPrice(productRequest.getPrice());
            updateProduct.setThumbnail(fileDownloadUri);
            updateProduct.setStock(productRequest.getStock());
            updateProduct.setCategoryID(productRequest.getCategoryID());

            if (productRequest.getStatus().equalsIgnoreCase("active")) {
                updateProduct.setStatus(StatusConst.ACTIVE.toString());
            } else {
                updateProduct.setStatus(StatusConst.INACTIVE.toString());
            }

            Timestamp dateNow = DateHelper.getTimestampNow();

            updateProduct.setModifiedAt(dateNow);
            updateProduct.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

            ProductEntity product = productRepository.save(updateProduct);

            return new BaseResponse(true, ResponseMessagesConst.UPDATE_SUCCESS.toString(), product);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse deleteProductByUUID(String uuid) {
        try {
            ProductEntity oldProduct = productRepository.findByUUID(uuid);
            if (NullEmptyChecker.isNullOrEmpty(oldProduct)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString(), null);
            }

            productRepository.delete(oldProduct);

            return new BaseResponse(true, ResponseMessagesConst.UPDATE_SUCCESS.toString(), null);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getProduct(int page, int limit, String search) {
        try {
            List<ProductEntity> listProduct;
            HashMap<String, Object> addEntity = new HashMap<>();
            if (page < 0 || NullEmptyChecker.isNullOrEmpty(limit)) {
                listProduct = productRepository.findAll();
            } else if (NullEmptyChecker.isNullOrEmpty(search)) {
                Pageable pageable = PageRequest.of(page, limit);
                Page<ProductEntity> pageProduct = productRepository.findAll(pageable);
                listProduct = pageProduct.toList();

                addEntity.put("totalPage", pageProduct.getTotalPages());
                addEntity.put("totalData", pageProduct.getTotalElements());
                addEntity.put("numberOfData", pageProduct.getNumberOfElements());
                addEntity.put("number", pageProduct.getNumber());
            } else {
                listProduct = productRepository.findByProductCodeOrName(search);
            }

            if (NullEmptyChecker.isNotNullOrEmpty(listProduct)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listProduct, addEntity);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getProductByUUID(String uuid) {
        try {
            ProductEntity listProduct = productRepository.findByUUID(uuid);

            if (NullEmptyChecker.isNotNullOrEmpty(listProduct)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listProduct);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }
}
