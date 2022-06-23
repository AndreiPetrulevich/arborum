package ru.arborum.web.dto.mapper;

import org.mapstruct.Mapper;
import ru.arborum.entity.Category;
import ru.arborumapi.category.dto.CategoryDto;

@Mapper
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);
    Category toCategory(CategoryDto categoryDto);
}
