package org.example.project.objects.dto;

public class Animal {
	private String name;
	private String weight;
	private String growth;
	private String type;

	public Animal(String name, String weight, String growth, String type) {
		this.name = name;
		this.weight = weight;
		this.growth = growth;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getGrowth() {
		return growth;
	}

	public void setGrowth(String growth) {
		this.growth = growth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Animal{" +
				"name='" + name + '\'' +
				", weight='" + weight + '\'' +
				", growth='" + growth + '\'' +
				", type='" + type + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Animal)) return false;

		Animal animal = (Animal) o;

		if (name != null ? !name.equals(animal.name) : animal.name != null) return false;
		if (weight != null ? !weight.equals(animal.weight) : animal.weight != null) return false;
		if (growth != null ? !growth.equals(animal.growth) : animal.growth != null) return false;
		return type != null ? type.equals(animal.type) : animal.type == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (weight != null ? weight.hashCode() : 0);
		result = 31 * result + (growth != null ? growth.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}
}
