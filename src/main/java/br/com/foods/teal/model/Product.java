package br.com.foods.teal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import br.com.foods.teal.dto.ProductDTO;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 * Classe responsável pelo cadastro do produto
 * 
 * @author Caio Pereira Leal
 */
@Entity
@Table(name = "tb_product")
@Audited
@AuditTable(value = "tb_audit_product")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @Column(name = "create_date")
    private LocalDateTime createDate;
    
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public Product() {
    }

    /**
    * Classe para informações do produto
    * 
    * @param id Identificador do produto
    * @param name Nome do produto
    * @param categories Lista de categorias (para frontend)
    * @param description Descrição do produto
    * @param quantity Quantidade disponível
    */
    public Product(String name, Category category, String description, Integer quantity, List<String> images) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.images = images;
    }

    /**
    * Retorna uma instância preenchida de ProductDTO
    * 
    * @param product Entidade Produto
    * 
    * @return novo ProductDTO
    */
    public Product(ProductDTO productDTO) {
        this.id = productDTO.id();
        this.name = productDTO.name();
        this.category = Category.fromString(productDTO.categories());
        this.description = productDTO.description();
        this.quantity = productDTO.quantity();
        this.images = productDTO.images();
        this.createDate = productDTO.createDate();
        this.updateDate = productDTO.updateDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	@Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", category=" + category + ", description=" + description
                + ", quantity=" + quantity + "]";
    }
}
