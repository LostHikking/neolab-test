package org.example.project.objects.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CSVReader implements Reader {
	private final String fileName;

	public CSVReader(String fileName) {
		this.fileName = fileName;
	}


	public List<List<String>> getRows() {
		return readLines()
				.stream()
				.map(s -> Arrays.asList(s.split(",")))
				.collect(Collectors.toList());
	}


	public Map<String, Integer> getHeader() {
		Map<String, Integer> nameToNumber = new HashMap<>();
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line = br.readLine();
			String[] headers = line.split(",");
			int i = 0;
			for (String header : headers) {
				nameToNumber.put(header, i);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nameToNumber;
	}

	private List<String> readLines() {
		List<String> lines = new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			//пропускаем строку, потому что там заголовок
			br.readLine();
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
}
