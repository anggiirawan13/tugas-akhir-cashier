package com.be.app.controller;

import com.be.app.dto.request.ProductInsertRequest;
import com.be.app.dto.request.ProductUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;

@CrossOrigin
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping
    private BaseResponse saveProduct(@RequestPart ProductInsertRequest request, @RequestPart MultipartFile file) {
        return productService.saveProduct(request, file);
    }

    @PutMapping("/{uuid}")
    private BaseResponse updateProduct(@PathVariable("uuid") String uuid, @RequestPart ProductUpdateRequest request, @RequestPart MultipartFile file) {
        return productService.updateProductByUUID(uuid, request, file);
    }

    @DeleteMapping(value = "/{uuid}")
    private BaseResponse deleteProductByUUID(@PathVariable("uuid") String uuid) {
        return productService.deleteProductByUUID(uuid);
    }

    @GetMapping
    private BaseResponse getProduct(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "limit", required = false, defaultValue = "0") int limit, @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return productService.getProduct(page, limit, search);
    }

    @GetMapping(value = "/{uuid}")
    private BaseResponse getProductByUUID(@PathVariable("uuid") String uuid) {
        return productService.getProductByUUID(uuid);
    }

    @GetMapping("/download/{fileName:.+}")
    public BaseResponse downloadFile(@PathVariable String fileName) {
        File file = new File( "./" + fileName);
        Resource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + fileName);
        }

        return new BaseResponse(true, "DOWNLOAD_IMAGE_SUCCESSFULLY", resource);
    }

}
