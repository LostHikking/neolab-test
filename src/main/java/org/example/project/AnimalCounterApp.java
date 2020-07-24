package org.example.project;

import org.example.project.filters.FilterParser;
import org.example.project.filters.FilterReader;
import org.example.project.objects.database.AnimalReader;
import org.example.project.objects.database.CSVReader;
import org.example.project.objects.dto.Animal;

import java.util.List;
import java.util.function.Predicate;

class AnimalCounterApp {
	public static void main(String[] args) {
		//Создаём список объектов из CSV-файла
		final List<Animal> animalsFromFile = new AnimalReader(new CSVReader("db.csv")).readObjects();

		//Создаём список Predicate для применения их в вильтрах далее
		final List<Predicate<Animal>> predicates = new FilterParser()
				.parseRawFilterSource(new FilterReader("animal.filter"));

		//Применяем все фильтры по очереди и выводим количество на экран
		predicates.forEach(predicate ->
				System.out.println(animalsFromFile
						.stream()
						.filter(predicate).count())
		);
	}
}
