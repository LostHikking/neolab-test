package org.example.project.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.example.project.objects.dto.Animal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FilterParserTest {

	final SQLFilterParser<Animal> filterParser = new SQLFilterParser<>(Animal.class);

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
		final Predicate<Animal> predicateCountAll = filterParser.parseQuery(queryCountAll);
		final Predicate<Animal> expectedPredicate = animal -> true;
		//Создаём список для проверки
		final List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		final List<Animal> actualList = testSet.stream().filter(predicateCountAll).collect(Collectors.toList());
		//Проверяем
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName(queryCountHerbivore)
	void parseQueryHerbivoreTest() {
		//Парсим Predicate из String
		final Predicate<Animal> actualPredicate = filterParser.parseQuery(queryCountHerbivore);
		final Predicate<Animal> expectedPredicate = animal -> animal.getType().equals("ТРАВОЯДНОЕ");
		final Predicate<Animal> unexpectedPredicate = filterParser.parseQuery(queryCountAll);
		//Создаём список для проверки
		final List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		final List<Animal> actualList = testSet.stream().filter(actualPredicate).collect(Collectors.toList());
		final List<Animal> unexpectedList = testSet.stream().filter(unexpectedPredicate).collect(Collectors.toList());
		//Проверяем
		assertNotEquals(unexpectedList, actualList);
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName(queryWithOrAndAnd)
	void parseQueryWithOrAndAndTest() {
		//Парсим Predicate из String
		final Predicate<Animal> actualPredicate = filterParser.parseQuery(queryWithOrAndAnd);

		final Predicate<Animal> firstPredicate = animal -> animal.getGrowth().equals("МАЛЕНЬКОЕ");
		final Predicate<Animal> secondPredicate = animal -> animal.getType().equals("ТРАВОЯДНОЕ");
		final Predicate<Animal> thirdPredicate = animal -> animal.getType().equals("ПЛОТОЯДНОЕ");

		final Predicate<Animal> expectedPredicate = (secondPredicate.or(thirdPredicate)).and(firstPredicate);

		final Predicate<Animal> unexpectedPredicate = filterParser.parseQuery(queryCountAll);
		//Создаём список для проверки
		final List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		final List<Animal> actualList = testSet.stream().filter(actualPredicate).collect(Collectors.toList());
		final List<Animal> unexpectedList = testSet.stream().filter(unexpectedPredicate).collect(Collectors.toList());
		//Проверяем
		assertNotEquals(unexpectedList, actualList);
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName(queryWithNot)
	void parseQueryWithNotTest() {
		//Парсим Predicate из String
		final Predicate<Animal> predicateCountAll = filterParser.parseQuery(queryWithNot);

		final Predicate<Animal> firstPredicate = animal -> animal.getType().equals("ВСЕЯДНОЕ");
		final Predicate<Animal> secondPredicate = animal -> !animal.getGrowth().equals("ВЫСОКОЕ");
		final Predicate<Animal> expectedPredicate = firstPredicate.and(secondPredicate);

		final Predicate<Animal> unexpectedPredicate = filterParser.parseQuery(queryCountAll);
		//Создаём список для проверки
		final List<Animal> expectedList = testSet.stream().filter(expectedPredicate).collect(Collectors.toList());
		final List<Animal> actualList = testSet.stream().filter(predicateCountAll).collect(Collectors.toList());
		final List<Animal> unexpectedList = testSet.stream().filter(unexpectedPredicate).collect(Collectors.toList());
		//Проверяем
		assertNotEquals(unexpectedList, actualList);
		assertEquals(expectedList, actualList);
	}

	@Test
	@DisplayName("Парсинг случайных символов")
	void parseWrongSyntax() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
				filterParser.parseQuery(someRandomString)
		);
	}
}