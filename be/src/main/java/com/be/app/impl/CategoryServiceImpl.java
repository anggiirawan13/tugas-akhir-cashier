package com.be.app.impl;

import com.be.app.dto.request.CategoryInsertRequest;
import com.be.app.dto.request.CategoryUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.entity.CategoryEntity;
import com.be.app.repository.CategoryRepository;
import com.be.app.service.CategoryService;
import com.be.constanta.ResponseMessagesConst;
import com.be.constanta.StatusConst;
import com.be.handler.InternalServerErrorHandler;
import com.be.helper.DateHelper;
import com.be.helper.NullEmptyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public BaseResponse saveCategory(CategoryInsertRequest request) {
        try {
            CategoryEntity oldCategory = categoryRepository.findByCategoryCode(request.getCategoryCode());
            if (NullEmptyChecker.isNotNullOrEmpty(oldCategory)) {
                return new BaseResponse(false, ResponseMessagesConst.ALREADY_EXIST.toString(), null);
            }

            CategoryEntity newCategory = new CategoryEntity();
            newCategory.setUuid(UUID.randomUUID().toString());
            newCategory.setCategoryCode(request.getCategoryCode());
            newCategory.setCategoryName(request.getCategoryName());

            if (request.getStatus() != null && request.getStatus().equalsIgnoreCase("active")) {
                newCategory.setStatus(StatusConst.ACTIVE.toString());
            } else {
                newCategory.setStatus(StatusConst.INACTIVE.toString());
            }

            Timestamp dateNow = DateHelper.getTimestampNow();

            newCategory.setCreatedAt(dateNow);
            newCategory.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            newCategory.setModifiedAt(dateNow);
            newCategory.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

            CategoryEntity category = categoryRepository.save(newCategory);

            return new BaseResponse(true, ResponseMessagesConst.INSERT_SUCCESS.toString(), category);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse updateCategoryByUUID(String uuid, CategoryUpdateRequest request) {
        try {
            CategoryEntity oldCategory = categoryRepository.findByUUID(uuid);
            if (NullEmptyChecker.isNullOrEmpty(oldCategory)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString(), null);
            }

            CategoryEntity updateCategory = categoryRepository.findByUUID(uuid);
            updateCategory.setCategoryCode(request.getCategoryCode());
            updateCategory.setCategoryName(request.getCategoryName());

            if (request.getStatus().equalsIgnoreCase(StatusConst.ACTIVE.toString())) {
                updateCategory.setStatus(StatusConst.ACTIVE.toString());
            } else {
                updateCategory.setStatus(StatusConst.INACTIVE.toString());
            }

            Timestamp dateNow = DateHelper.getTimestampNow();

            updateCategory.setModifiedAt(dateNow);
            updateCategory.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

            CategoryEntity category = categoryRepository.save(updateCategory);

            return new BaseResponse(true, ResponseMessagesConst.UPDATE_SUCCESS.toString(), category);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse deleteCategoryByUUID(String uuid) {
        try {
            CategoryEntity oldCategory = categoryRepository.findByUUID(uuid);
            if (NullEmptyChecker.isNullOrEmpty(oldCategory)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString(), null);
            }

            categoryRepository.delete(oldCategory);

            return new BaseResponse(true, ResponseMessagesConst.UPDATE_SUCCESS.toString(), null);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getCategory(int page, int limit) {
        try {
            List<CategoryEntity> listCategory;
            HashMap<String, Object> addEntity = new HashMap<>();
            if (page < 0 || NullEmptyChecker.isNullOrEmpty(limit)) {
                listCategory = categoryRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page, limit);
                Page<CategoryEntity> pageCategory = categoryRepository.findAll(pageable);
                listCategory = pageCategory.toList();

                addEntity.put("totalPage", pageCategory.getTotalPages());
                addEntity.put("totalData", pageCategory.getTotalElements());
                addEntity.put("numberOfData", pageCategory.getNumberOfElements());
                addEntity.put("number", pageCategory.getNumber());
            }

            if (NullEmptyChecker.isNotNullOrEmpty(listCategory)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listCategory, addEntity);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getCategoryByUUID(String uuid) {
        try {
            CategoryEntity listCategory = categoryRepository.findByUUID(uuid);

            if (NullEmptyChecker.isNotNullOrEmpty(listCategory)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listCategory);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }
}
