package org.example.project.filters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterReader implements RawFilterSource {
	private final String fileName;

	public FilterReader(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<String> getRawFilters() {
		List<String> lines = new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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
