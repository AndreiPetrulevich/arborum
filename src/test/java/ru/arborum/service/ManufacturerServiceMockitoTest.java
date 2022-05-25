package ru.arborum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.arborum.dao.ManufacturerDao;
import ru.arborum.entity.Manufacturer;
import ru.arborum.web.dto.ManufacturerDto;
import ru.arborum.web.dto.mapper.ManufacturerMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceMockitoTest {


    public static final String APPLE_COMPANY_NAME = "Apple";
    public static final String MICROSOFT_COMPANY_NAME = "Microsoft";

    @Mock
    ManufacturerDao manufacturerDao;

    @Mock
    ManufacturerMapper manufacturerMapper;

    @Mock
    ManufacturerDto manufacturerDto;

    @InjectMocks
    ManufacturerService manufacturerService;

    List<Manufacturer> manufacturers;

    @BeforeEach
    void setup() { //заполняем сервис какими-то данными
        manufacturers = new ArrayList<>();
        manufacturers.add(Manufacturer.builder()
                .id(1L)
                .name(APPLE_COMPANY_NAME)
                .build());
        manufacturers.add(Manufacturer.builder()
                .id(2L)
                .name(MICROSOFT_COMPANY_NAME)
                .build());
    }

    @Test
    void findAllTest() {
        //given
        given(manufacturerDao.findAll()).willReturn(manufacturers);
        given(manufacturerMapper.toManufacturerDto(any())).will(
                (invocation) -> {
                    Manufacturer manufacturer = (Manufacturer) invocation.getArgument(0);

                    if (manufacturer == null) {
                        return null;
                    }

                    return ManufacturerDto.builder()
                            .id(manufacturer.getId())
                            .name(manufacturer.getName())
                            .build();
                });
        final int manufacturersSize = manufacturers.size();

        //when
        List<ManufacturerDto> manufacturerList = manufacturerService.findAll();

        //then
        then(manufacturerDao).should().findAll();
        assertAll( // для того, что бы показывал какие тесты упали
                () -> assertEquals(manufacturersSize, manufacturers.size(), "Size must be equals " + manufacturersSize),
                () -> assertEquals(APPLE_COMPANY_NAME, manufacturerList.get(0).getName())
        );
    }

    @Test
    void deleteManufacturerByIdTest() {
        //when
        manufacturerService.deleteById(1L);

        //then
        then(manufacturerDao).should().deleteById(1L);
    }

    @Test
    void saveManufacturerTest() {
        //given
        given(manufacturerDao.save(any(Manufacturer.class))).willReturn(Manufacturer.builder()
                .id(1L)
                .name(APPLE_COMPANY_NAME)
                .build());

        given(manufacturerMapper.toManufacturerDto(any())).will(
                (invocation) -> {
                    Manufacturer manufacturer = (Manufacturer) invocation.getArgument(0);

                    if (manufacturer == null) {
                        return null;
                    }

                    return ManufacturerDto.builder()
                            .id(manufacturer.getId())
                            .name(manufacturer.getName())
                            .build();
                });

        given(manufacturerMapper.toManufacturer(any())).will(
                (invocation) -> {
                    ManufacturerDto manufacturerDto = (ManufacturerDto) invocation.getArgument(0);

                    if (manufacturerDto == null) {
                        return null;
                    }

                    return Manufacturer.builder()
                            .id(manufacturerDto.getId())
                            .name(manufacturerDto.getName())
                            .build();
                });

        //when
        ManufacturerDto savedManufacturerDto = manufacturerService.save(manufacturerDto);

        //then
        then(manufacturerDao).should().save(any(Manufacturer.class));

        assertAll(
                () -> assertEquals(1L, savedManufacturerDto.getId()),
                () -> assertEquals(APPLE_COMPANY_NAME, savedManufacturerDto.getName())
        );
    }
}