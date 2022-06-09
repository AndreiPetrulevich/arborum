package ru.arborum.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.arborum.dao.CategoryDao;
import ru.arborum.dao.ManufacturerDao;
import ru.arborum.dao.ProductDao;
import ru.arborum.entity.Category;
import ru.arborum.entity.Manufacturer;
import ru.arborum.entity.enums.Status;
import ru.arborum.web.dto.ProductDto;
import ru.arborum.web.dto.mapper.CategoryMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRestControllerIntegrationTest {

    public static final String NOTEBOOK_CATEGORY_NAME = "Notebook";
    public static final String APPLE_COMPANY_NAME = "Apple";
    public static final String APPLE_PRODUCT_NAME = "MacBook Pro";


    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    ManufacturerDao manufacturerDao;
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void saveTest() throws Exception {
        //given
        Category savedCategory = categoryDao.save(Category.builder()
                .title(NOTEBOOK_CATEGORY_NAME)
                .build());
        Manufacturer savedManufacturer = manufacturerDao.save(Manufacturer.builder()
                .name(APPLE_COMPANY_NAME)
                .build());

        mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ProductDto.builder()
                        .title(APPLE_PRODUCT_NAME)
                        .cost(new BigDecimal("1000.00"))
                        .status(Status.ACTIVE)
                        .manufactureDate(LocalDate.now())
                        .manufacturer(savedManufacturer.getName())
                        .categories(Set.of(categoryMapper.toCategoryDto(savedCategory)))
                        .build())))
                .andExpect(status().isCreated());

        assertEquals(1, productDao.findAll().size());
    }

    @Test
    @Order(2)
    public void findAllTest() throws Exception {
        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id")))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value(APPLE_PRODUCT_NAME));
    }

    @Test
    @Order(3)
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/v1/product/1"))
                .andExpect(status().isNoContent());
    }
}