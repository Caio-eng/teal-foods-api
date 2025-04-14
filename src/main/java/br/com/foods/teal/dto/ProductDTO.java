package br.com.foods.teal.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.foods.teal.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Classe para informações do produto
 * 
 * @param id Identificador do produto
 * @param name Nome do produto
 * @param categories Lista de categorias (para frontend)
 * @param description Descrição do produto
 * @param quantity Quantidade disponível
 * @param price preço do produto
 * @param image Imagem do produto
 * @param createDate Data de criação
 * @param updateDate Data de atualização
 * @param userId identidicador do usuário
 * 
 * @author Caio Pereira Leal
 */
public record ProductDTO(
        Long id,
        @NotBlank(message = "O campo nome é obrigatório") String name,
        @NotBlank(message = "O campo categoria é obrigatório") String categories,
        String description,
        @NotNull @PositiveOrZero(message = "A quantidade não pode ser negativa") Integer quantity,
        @NotBlank(message = "Pelo menos uma imagem deve ser fornecida") List<String> images,
        @NotNull @PositiveOrZero(message = "O valor não pode ser negativo") Double price,
        String userId,
        @JsonIgnore LocalDateTime createDate,
        @JsonIgnore  LocalDateTime updateDate) {

    /**
     * Retorna uma instância preenchida de ProductDTO a partir de um Product
     * 
     * @param product Entidade Product
     * @return novo ProductDTO
     */
    public static ProductDTO fromModel(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getCategory().toString(),
                product.getDescription(),
                product.getQuantity(),
                product.getImages(),
                product.getPrice(),
                product.getUser().getId(),
                product.getCreateDate(),
                product.getUpdateDate());
    }
}
