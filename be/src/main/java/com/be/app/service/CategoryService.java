package com.be.app.service;

import com.be.app.dto.request.CategoryInsertRequest;
import com.be.app.dto.request.CategoryUpdateRequest;
import com.be.app.dto.response.BaseResponse;

public interface CategoryService {

    BaseResponse saveCategory(CategoryInsertRequest request);

    BaseResponse updateCategoryByUUID(String uuid, CategoryUpdateRequest request);

    BaseResponse deleteCategoryByUUID(String uuid);

    BaseResponse getCategory(int page, int limit);

    BaseResponse getCategoryByUUID(String uuid);

}
