package org.example.project.filters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SQLFilterParser<T> implements FilterParser<T> {

	private final Class<T> tClass;

	public SQLFilterParser(Class<T> tClass) {
		this.tClass = tClass;
	}

	/**
	 * @param rawFilterSource источник запросов в формате SQL
	 * @return Список распарсенных Predicate для класса T, полученных из источника
	 */
	@Override
	public List<Predicate<T>> getAllCountQueries(RawFilterSource rawFilterSource) {
		return rawFilterSource.getRawFilters().stream().map(this::parseQuery)
				.collect(Collectors.toList());
	}

	/**
	 * @param command запрос для парсинга SQL
	 * @return Распарсенный Predicate
	 */
	@Override
	public Predicate<T> parseQuery(String command) {
		String substring = command.substring("SELECT COUNT".length());
		//Смотрим, есть ли условие для фильтра
		if ((substring.length() == 0)) {
			return animal -> true;
		}
		List<String> words = Arrays.asList(substring.replace(";", "").split(" "));
		if (words.get(1).equals("WHERE")) {
			substring = substring.substring(" WHERE ".length());
			words = Arrays.asList(substring.split(" "));
		}
		//Если есть, разбиваем их
		final List<String> parameters = Arrays.asList(String.join(" ", words).split(" AND "));
		return parameters.stream().map(this::parsePredicate).reduce(x -> true, Predicate::and);
	}

	private Predicate<T> parsePredicate(String query) {
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

	private Predicate<T> buildPredicate(String key, String value, boolean isPositive) {
		//Разбиваем возможные аргументы
		final List<String> arguments = Arrays.asList(value.split(" OR "));
		//Находим необходимый геттер, по результату которому будем фильтровать
		//Можно не изменять этот класс, при изменений класса Animal
		final Method getter = Arrays.stream(tClass.getDeclaredMethods())
				.filter(it -> it.getName().equalsIgnoreCase("get" + key))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(
						"Can'not find getter for " + key.toLowerCase() + " in " + tClass.getName()));
		return arguments.stream().map(argument ->
				(Predicate<T>) (object -> {
					try {
						if (isPositive) {
							return getter.invoke(object).equals(argument);
						} else {
							return !(getter.invoke(object).equals(argument));
						}
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					return false;
				})
		).reduce(x -> false, Predicate::or);
	}
}
