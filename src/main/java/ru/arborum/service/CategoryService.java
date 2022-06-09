package ru.arborum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.arborum.dao.CategoryDao;
import ru.arborum.entity.Category;
import ru.arborum.web.dto.CategoryDto;
import ru.arborum.web.dto.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;

    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        if (category.getId() != null) {
            categoryDao.findById(category.getId()).ifPresent(
                    (p) -> category.setVersion(p.getVersion())
            );
        }
        return categoryMapper.toCategoryDto(category);
    }

    public CategoryDto findById(Long id) {
        return categoryMapper.toCategoryDto(categoryDao.findById(id).orElse(null));
    }

    public List<CategoryDto> findAll() {
        return categoryDao.findAll().stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        try{
            categoryDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }
    }
}
