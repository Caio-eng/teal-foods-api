package br.com.foods.teal.model;

import java.util.List;

/**
 * Responsável por clasificar as categorias
 * 
 * @author Caio Pereira Leal
 */
public enum Category {
	Frutas("Frutas"),
	Verduras("Verduras"),
	Legumes("Legumes"),
	Carnes("Carnes"),
	Outros("Outros");

    private final String displayName;

    /**
     * Construtor que tem o nome da categoria como parâmetro
     * 
     * @param displayName
     * 				Nome da Categoria
     */
    Category(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retorna no nome da categoria
     * 
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Responsável por transformar um enum em string
     * 
     * @param value
     * 			Valor em String
     * 
     * @return categoria
     */
    public static Category fromString(String value) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(value) || 
                category.getDisplayName().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + value);
    }

    /**
     * Responsável por transformar um enum em lista de string
     * 
     * @param categories
     * 			Lista de Categorias
     * 
     * @return lista de categorias
     */
    public static Category fromStringList(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("Pelo menos uma categoria deve ser fornecida");
        }
        return fromString(categories.get(0));
    }

    public static List<String> toStringList(Category category) {
        return List.of(category.name());
    }
}
