package com.be.app.controller;

import com.be.app.dto.request.CategoryInsertRequest;
import com.be.app.dto.request.CategoryUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping
    private BaseResponse saveCategory(@RequestBody CategoryInsertRequest request) {
        return categoryService.saveCategory(request);
    }

    @PutMapping(value = "/{uuid}")
    private BaseResponse updateCategory(@PathVariable("uuid") String uuid, @RequestBody CategoryUpdateRequest request) {
        return categoryService.updateCategoryByUUID(uuid, request);
    }

    @DeleteMapping(value = "/{uuid}")
    private BaseResponse deleteCategoryByUUID(@PathVariable("uuid") String uuid) {
        return categoryService.deleteCategoryByUUID(uuid);
    }

    @GetMapping
    private BaseResponse getCategory(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
        return categoryService.getCategory(page, limit);
    }

    @GetMapping(value = "/{uuid}")
    private BaseResponse getCategoryByUUID(@PathVariable("uuid") String uuid) {
        return categoryService.getCategoryByUUID(uuid);
    }

}
