package ru.arborumapi.category.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.arborumapi.category.dto.CategoryDto;

import java.util.List;

public interface CategoryGateway {

    @GetMapping
    List<CategoryDto> getAllProducts();

    @GetMapping("/{categoryId}")
    ResponseEntity<?> getCategory(@PathVariable("categoryId") Long id);

    @PostMapping
    ResponseEntity<?> handlePost(@Validated @RequestBody CategoryDto categoryDto);

    @PutMapping("/{categoryId}")
    ResponseEntity<?> handleUpdate(@PathVariable("categoryId") Long id, @Validated @RequestBody CategoryDto categoryDto);

    @DeleteMapping("/{categoryId}")
    void deleteCategory(@PathVariable("categoryId") Long id);
}
