package ru.arborum.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.arborum.config.ShopConfig;
import ru.arborum.entity.Manufacturer;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //подтягивает все конфиги, бины, которые относятся к слою работы с базой данны
@Import(ShopConfig.class)
class ManufacturerDaoDataJpaTest {

    public static final String APPLE_COMPANY_NAME = "Apple";
    public static final String MICROSOFT_COMPANY_NAME = "Microsoft";

    @Autowired
    ManufacturerDao manufacturerDao;

    @Test
    public void saveTest() {
        Manufacturer manufacturer = Manufacturer.builder()
                .name(APPLE_COMPANY_NAME)
                .build();

        Manufacturer savedManufacturer = manufacturerDao.save(manufacturer);

        assertAll(
                () -> assertEquals(1L, savedManufacturer.getId()),
                () -> assertEquals(APPLE_COMPANY_NAME, savedManufacturer.getName()),
                () -> assertEquals(0, savedManufacturer.getVersion()),
                () -> assertEquals("User", savedManufacturer.getCreatedBy()),
                () -> assertNotNull(savedManufacturer.getCreatedDate()),
                () -> assertEquals("User", savedManufacturer.getLastModifiedBy()),
                () -> assertNotNull(savedManufacturer.getLastModifiedDate())
        );
    }

    // TODO findAll, delete, update
    // TODO попробовать через persistenceContext(либо через Autowired) (либо через EntityManager)
}