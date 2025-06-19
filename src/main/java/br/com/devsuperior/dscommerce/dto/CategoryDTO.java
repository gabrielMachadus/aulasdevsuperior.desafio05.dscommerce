package br.com.devsuperior.dscommerce.dto;

import br.com.devsuperior.dscommerce.entities.Category;

public class CategoryDTO {

	private Long id;
	private String name;
	
	public CategoryDTO() {
	}
	
	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CategoryDTO(Category entity) {
		id = entity.getId();
		name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
