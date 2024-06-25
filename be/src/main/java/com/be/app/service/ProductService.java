package com.be.app.service;

import com.be.app.dto.request.ProductInsertRequest;
import com.be.app.dto.request.ProductUpdateRequest;
import com.be.app.dto.response.BaseResponse;

import java.util.List;

public interface ProductService {

    BaseResponse saveProduct(ProductInsertRequest request);

    BaseResponse updateProductByUUID(String uuid, ProductUpdateRequest request);

    BaseResponse deleteProductByUUID(String uuid);

    BaseResponse getProduct(int page, int limit, String search);

    BaseResponse getProductByUUID(String uuid);

}
