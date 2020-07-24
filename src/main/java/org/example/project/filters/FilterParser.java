package org.example.project.filters;

import org.example.project.objects.dto.Animal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterParser {

	public FilterParser() {
	}

	/**
	 * @param rawFilterSource источник запросов в формате String
	 * @return Список распарсенных Predicate для класса Animal, полученных из источника
	 */
	public List<Predicate<Animal>> parseRawFilterSource(RawFilterSource rawFilterSource) {
		return rawFilterSource.getRawFilters().stream().map(this::parseQuery).collect(Collectors.toList());
	}

	/**
	 * @param command запрос для парсинга
	 * @return Распарсенный Predicate для класса Animal
	 */
	public Predicate<Animal> parseQuery(String command) {
		command = command.substring("SELECT COUNT".length());
		//Смотрим, есть ли условие для фильтра
		if ((command.length() == 0)) return animal -> true;
		List<String> words = Arrays.asList(command.replace(";", "").split(" "));
		if (words.get(1).equals("WHERE")) {
			command = command.substring(" WHERE ".length());
			words = Arrays.asList(command.split(" "));
		}
		//Если есть, разбиваем их
		final List<String> parameters = Arrays.asList(String.join(" ", words).split(" AND "));
		return parameters.stream().map(this::parsePredicate).reduce(x -> true, Predicate::and);
	}

	private Predicate<Animal> parsePredicate(String query) {
		final List<String> words = Arrays.asList(query.split(" "));
		//Смотри знак и отправляем условия на построения Predicate
		switch (words.get(1)) {
			case "=" -> {
				List<String> keyAndValue = Arrays.asList(query.split(" = "));
				return buildPredicate(keyAndValue.get(0).replace(" ", ""),
						keyAndValue.get(1).replace("\"", ""), true);
			}
			case "!=" -> {
				List<String> keyAndValue = Arrays.asList(query.split(" != "));
				return buildPredicate(keyAndValue.get(0).replace(" ", ""),
						keyAndValue.get(1).replace("\"", ""), false);
			}
			default -> throw new IllegalArgumentException("Must be = or !=");
		}
	}

	private Predicate<Animal> buildPredicate(String key, String value, boolean isPositive) {
		//Разбиваем возможные аргументы
		final List<String> arguments = Arrays.asList(value.split(" OR "));
		//Находим необходимый геттер, по результату которому будем фильтровать
		//Можно не изменять этот класс, при изменений класса Animal
		final Method getter = Arrays.stream(Animal.class.getDeclaredMethods())
				.filter(it -> it.getName().equalsIgnoreCase("get" + key))
				.findFirst()
				.orElse(null);
		if (getter != null)
			return arguments.stream().map(argument ->
					(Predicate<Animal>) (animal -> {
						try {
							if (isPositive)
								return getter.invoke(animal).equals(argument);
							else
								return !(getter.invoke(animal).equals(argument));
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
						return false;
					})
			).reduce(x -> false, Predicate::or);
		else throw new IllegalArgumentException("Can'not find getter for " + key.toLowerCase());
	}
}
