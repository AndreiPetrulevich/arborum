package ru.arborum.web.dto;

import lombok.*;
import ru.arborum.entity.Category;
import ru.arborum.entity.enums.Status;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal cost;
    @PastOrPresent
    private LocalDate manufactureDate;
    @NotNull
    private Status status;
    private String manufacturer;
    private Set<CategoryDto> categories;
}
