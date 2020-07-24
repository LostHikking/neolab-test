package org.example.project.objects.database;

import org.example.project.objects.dto.Animal;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AnimalReaderTest {

	final List<Animal> testSet = List.of(
			new Animal("Корова", "ТЯЖЕЛОЕ", "НЕВЫСОКОЕ", "ТРАВОЯДНОЕ"),
			new Animal("Муравей", "ЛЕГКОЕ", "МАЛЕНЬКОЕ", "ВСЕЯДНОЕ"),
			new Animal("Кошка", "СРЕДНЕЕ", "НЕВЫСОКОЕ", "ВСЕЯДНОЕ"),
			new Animal("Медведь", "ТЯЖЕЛОЕ", "НЕВЫСОКОЕ", "ВСЕЯДНОЕ"),
			new Animal("Слон", "ТЯЖЕЛОЕ", "ВЫСОКОЕ", "ТРАВОЯДНОЕ"),
			new Animal("Волк", "СРЕДНЕЕ", "НЕВЫСОКОЕ", "ПЛОТОЯДНОЕ"),
			new Animal("Жираф", "ТЯЖЕЛОЕ", "ВЫСОКОЕ", "ТРАВОЯДНОЕ"),
			new Animal("Лягушка", "ЛЕГКОЕ", "МАЛЕНЬКОЕ", "ПЛОТОЯДНОЕ"));


	@Test
	void readObjectsTest() {
		final AnimalReader animalReader = new AnimalReader(new CSVReader("db.csv"));
		final List<Animal> actualList = animalReader.readObjects();
		final List<Animal> unexpectedList = Collections.emptyList();
		assertEquals(testSet, actualList);
		assertNotEquals(unexpectedList, actualList);
	}
}