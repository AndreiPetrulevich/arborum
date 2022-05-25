package ru.arborum.web.dto.mapper;

import org.mapstruct.Mapper;
import ru.arborum.entity.Manufacturer;
import ru.arborum.web.dto.ManufacturerDto;

@Mapper
public interface ManufacturerMapper {
    Manufacturer toManufacturer(ManufacturerDto manufacturerDto);

    ManufacturerDto toManufacturerDto(Manufacturer manufacturer);
}
