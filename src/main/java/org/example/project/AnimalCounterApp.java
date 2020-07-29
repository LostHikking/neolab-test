package org.example.project;

import java.util.List;
import java.util.function.Predicate;
import org.example.project.filters.FilterReader;
import org.example.project.filters.SQLFilterParser;
import org.example.project.objects.database.AnimalReader;
import org.example.project.objects.database.CSVReader;
import org.example.project.objects.dto.Animal;

class AnimalCounterApp {
	public static void main(String[] args) {
		//Создаём список объектов из CSV-файла
		final List<Animal> animalsFromFile = new AnimalReader(new CSVReader("db.csv")).readObjects();

		//Создаём список Predicate для применения их в вильтрах далее
		final List<Predicate<Animal>> predicates = new SQLFilterParser<>(Animal.class)
        .getAllCountQueries(new FilterReader("animal.filter"));

		//Применяем все фильтры по очереди и выводим количество на экран
		predicates.forEach(predicate ->
				System.out.println(animalsFromFile
						.stream()
						.filter(predicate).count())
		);
	}
}
