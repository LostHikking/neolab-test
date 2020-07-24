package org.example.project.objects.database;

import java.util.List;
import java.util.Map;

public interface Reader {
	/**
	 * @return таблицу в виде набора строк из файла, переданного по пути в конструктор
	 */
	List<List<String>> getRows();
	/**
	 * @return заголовок из CSV-файла, переданного по пути в конструктор
	 */
	Map<String, Integer> getHeader();
}
