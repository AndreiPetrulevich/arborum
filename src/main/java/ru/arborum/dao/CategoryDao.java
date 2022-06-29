package ru.arborum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arborum.entity.Category;


public interface CategoryDao extends JpaRepository<Category, Long> {

}
