package tacos;

import lombok.Data;

@Data
public class Ingredient {
	
	private String id;
	private String name;
	private Type type;

	public enum Type {
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
	}

	public Ingredient(String id, String name, Type type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
}