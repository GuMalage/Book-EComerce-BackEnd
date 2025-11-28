package br.edu.utfpr.pb.pw44s.server.dto.response;

import br.edu.utfpr.pb.pw44s.server.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String urlImage;
    private String longDescription;
    private String autorName;
    private CategoryDTO category;
}
