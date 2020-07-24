package org.example.project.objects.database;

import org.example.project.objects.dto.Animal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimalReader {
	private final Reader reader;

	/**
	 * @param reader источник сырого представления данных
	 */
	public AnimalReader(Reader reader) {
		this.reader = reader;
	}

	/**
	 * @return список объектов, прочитанных из reader
	 */
	public List<Animal> readObjects() {
		//Читаем заголовок, чтобы понимать в каком порядке находятся столбцы в файле.
		final Map<String, Integer> header = reader.getHeader();
		//Создаём из строк объекты
		return reader.getRows().stream().map( it -> createObject(it, header)).collect(Collectors.toList());
	}

	private Animal createObject(List<String> metadata, Map<String, Integer> header) {
		return
				new Animal(
						metadata.get(header.get("name")),
						metadata.get(header.get("weight")),
						metadata.get(header.get("growth")),
						metadata.get(header.get("type")));
	}
}
