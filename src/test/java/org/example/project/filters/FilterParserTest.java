package org.example.project.filters;

import org.example.project.objects.dto.Animal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FilterParserTest {
	FilterParser filterParser = new FilterParser();

	final String queryCountAll = "SELECT COUNT";
	final String queryCountHerbivore = "SELECT COUNT WHERE TYPE = \"ТРАВОЯДНОЕ\"";
	final String queryWithOrAndAnd = "SELECT COUNT " +
			"WHERE TYPE = \"ТРАВОЯДНОЕ\" OR \"ПЛОТОЯДНОЕ\" AND GROWTH = \"МАЛЕНЬКОЕ\"";
	final String queryWithNot = "SELECT COUNT WHERE TYPE = \"ВСЕЯДНОЕ\" AND GROWTH != \"ВЫСОКОЕ\"";
	final String someRandomString = "asdf asdfwer523 dfgty;ji sdfgl;k";


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
	@DisplayName(queryCountAll)
	void parseQueryAllTest() {
		//Парсим Predicate из String
		Predicate<Animal> predicateCountAll = filterParser.parseQuery(queryCountAll);
		Predicate<Animal> expectedPredicate = animal -> true;
		//Создаём список для проверки
		List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		List<Animal> actualList = testSet.stream().filter(predicateCountAll).collect(Collectors.toList());
		//Проверяем
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName(queryCountHerbivore)
	void parseQueryHerbivoreTest() {
		//Парсим Predicate из String
		Predicate<Animal> actualPredicate = filterParser.parseQuery(queryCountHerbivore);
		Predicate<Animal> expectedPredicate = animal -> animal.getType().equals("ТРАВОЯДНОЕ");
		Predicate<Animal> unexpectedPredicate = filterParser.parseQuery(queryCountAll);
		//Создаём список для проверки
		List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		List<Animal> actualList = testSet.stream().filter(actualPredicate).collect(Collectors.toList());
		List<Animal> unexpectedList = testSet.stream().filter(unexpectedPredicate).collect(Collectors.toList());
		//Проверяем
		assertNotEquals(unexpectedList, actualList);
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName(queryWithOrAndAnd)
	void parseQueryWithOrAndAndTest() {
		//Парсим Predicate из String
		Predicate<Animal> actualPredicate = filterParser.parseQuery(queryWithOrAndAnd);

		Predicate<Animal> firstPredicate = animal -> animal.getGrowth().equals("МАЛЕНЬКОЕ");
		Predicate<Animal> secondPredicate = animal -> animal.getType().equals("ТРАВОЯДНОЕ");
		Predicate<Animal> thirdPredicate = animal -> animal.getType().equals("ПЛОТОЯДНОЕ");

		Predicate<Animal> expectedPredicate = (secondPredicate.or(thirdPredicate)).and(firstPredicate);

		Predicate<Animal> unexpectedPredicate = filterParser.parseQuery(queryCountAll);
		//Создаём список для проверки
		List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		List<Animal> actualList = testSet.stream().filter(actualPredicate).collect(Collectors.toList());
		List<Animal> unexpectedList = testSet.stream().filter(unexpectedPredicate).collect(Collectors.toList());
		//Проверяем
		assertNotEquals(unexpectedList, actualList);
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName(queryWithNot)
	void parseQueryWithNotTest() {
		//Парсим Predicate из String
		Predicate<Animal> predicateCountAll = filterParser.parseQuery(queryWithNot);

		Predicate<Animal> firstPredicate = animal -> animal.getType().equals("ВСЕЯДНОЕ");
		Predicate<Animal> secondPredicate = animal -> !animal.getGrowth().equals("ВЫСОКОЕ");
		Predicate<Animal> expectedPredicate = firstPredicate.and(secondPredicate);

		Predicate<Animal> unexpectedPredicate = filterParser.parseQuery(queryCountAll);
		//Создаём список для проверки
		List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		List<Animal> actualList = testSet.stream().filter(predicateCountAll).collect(Collectors.toList());
		List<Animal> unexpectedList = testSet.stream().filter(unexpectedPredicate).collect(Collectors.toList());
		//Проверяем
		assertNotEquals(unexpectedList, actualList);
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName("Парсинг случайных символов")
	void parseWrongSyntax() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
				{
					Predicate<Animal> wrongPredicate = filterParser.parseQuery(someRandomString);
				}
		);
	}
}