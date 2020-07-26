package org.example.project.filters;

import java.util.List;
import java.util.function.Predicate;

public interface FilterParser<T> {

  /**
   * @param rawFilterSource источник запросов в формате String
   * @return Список распарсенных Predicate для класса Animal, полученных из источника
   */
  List<Predicate<T>> parseRawFilterSource(RawFilterSource rawFilterSource);

  /**
   * @param command запрос для парсинга
   * @return Распарсенный Predicate для класса Animal
   */
  Predicate<T> parseQuery(String command);
}
