package com.be.app.controller;

import com.be.app.dto.request.ProductInsertRequest;
import com.be.app.dto.request.ProductUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping
    private BaseResponse saveProduct(@RequestPart("product") ProductInsertRequest request, @RequestPart("thumbnail") MultipartFile file) {
        return productService.saveProduct(request, file);
    }

    @PutMapping("/{uuid}")
    private BaseResponse updateProduct(@PathVariable("uuid") String uuid, @RequestPart("product") ProductUpdateRequest request, @RequestPart("thumbnail") MultipartFile file) {
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
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("./src/main/java/com/be/assets/images").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + fileName);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }

}
