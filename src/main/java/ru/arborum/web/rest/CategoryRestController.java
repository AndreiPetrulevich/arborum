package ru.arborum.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.arborum.service.CategoryService;
import ru.arborum.web.dto.CategoryDto;

import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllProducts() {
        return categoryService.findAll();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable("categoryId") Long id) {
        CategoryDto categoryDto;
        if (id != null) {
            categoryDto = categoryService.findById(id);
            if (categoryDto != null) {
                return new ResponseEntity<>(categoryDto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> handlePost(@Validated @RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategoryDto = categoryService.save(categoryDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/category/" + savedCategoryDto.getId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> handleUpdate(@PathVariable("categoryId") Long id, @Validated @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        CategoryDto savedCategoryDto = categoryService.save(categoryDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/category/" + savedCategoryDto.getId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("categoryId") Long id) {
        categoryService.deleteById(id);
    }
}
