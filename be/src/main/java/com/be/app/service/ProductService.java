package com.be.app.service;

import com.be.app.dto.request.ProductInsertRequest;
import com.be.app.dto.request.ProductUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    BaseResponse saveProduct(ProductInsertRequest request, MultipartFile file);

    BaseResponse updateProductByUUID(String uuid, ProductUpdateRequest request, MultipartFile file);

    BaseResponse deleteProductByUUID(String uuid);

    BaseResponse getProduct(int page, int limit, String search);

    BaseResponse getProductByUUID(String uuid);

}
