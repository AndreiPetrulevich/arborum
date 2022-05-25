package ru.arborum.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.arborum.config.ShopConfig;
import ru.arborum.entity.Manufacturer;

import java.util.List;

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

    @Test
    public void findAllTest() {
        manufacturerDao.save(Manufacturer.builder()
                .id(1L)
                .name(APPLE_COMPANY_NAME)
                .build());
        manufacturerDao.save(Manufacturer.builder()
                .id(2L)
                .name(MICROSOFT_COMPANY_NAME)
                .build());

        List<Manufacturer> manufacturerList = manufacturerDao.findAll();

        assertAll(
                () -> assertEquals(2, manufacturerList.size()),

                () -> assertEquals(1L, manufacturerList.get(0).getId()),
                () -> assertEquals(APPLE_COMPANY_NAME, manufacturerList.get(0).getName()),
                () -> assertEquals(0, manufacturerList.get(0).getVersion()),
                () -> assertEquals("User", manufacturerList.get(0).getCreatedBy()),
                () -> assertNotNull(manufacturerList.get(0).getCreatedDate()),
                () -> assertEquals("User", manufacturerList.get(0).getLastModifiedBy()),
                () -> assertNotNull(manufacturerList.get(0).getLastModifiedDate()),

                () -> assertEquals(2L, manufacturerList.get(1).getId()),
                () -> assertEquals(MICROSOFT_COMPANY_NAME, manufacturerList.get(1).getName()),
                () -> assertEquals(0, manufacturerList.get(1).getVersion()),
                () -> assertEquals("User", manufacturerList.get(1).getCreatedBy()),
                () -> assertNotNull(manufacturerList.get(1).getCreatedDate()),
                () -> assertEquals("User", manufacturerList.get(1).getLastModifiedBy()),
                () -> assertNotNull(manufacturerList.get(1).getLastModifiedDate())
        );
    }

    @Test
    public void deleteTest() {
        manufacturerDao.save(Manufacturer.builder()
                .name(APPLE_COMPANY_NAME)
                .build());

        //manufacturerDao.deleteById(1L);
        manufacturerDao.deleteAll();
    }

    @Test
    public void updateTest() {
        Manufacturer savedAppleManufacturer = manufacturerDao.save(Manufacturer.builder()
                .id(1L)
                .name(APPLE_COMPANY_NAME)
                .build());

        assertAll(
                () -> assertEquals(1L, savedAppleManufacturer.getId()),
                () -> assertEquals(APPLE_COMPANY_NAME, savedAppleManufacturer.getName())
        );

        Manufacturer savedMicrosoftManufacturer = manufacturerDao.save(Manufacturer.builder()
                .id(1L)
                .name(MICROSOFT_COMPANY_NAME)
                .build());

        assertAll(
                () -> assertEquals(1L, savedMicrosoftManufacturer.getId()),
                () -> assertEquals(MICROSOFT_COMPANY_NAME, savedMicrosoftManufacturer.getName())
        );
    }

    // TODO* попробовать через persistenceContext(либо через Autowired) (либо через EntityManager)
}