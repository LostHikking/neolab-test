package org.example.project.filters;

import java.util.List;

public interface RawFilterSource {
	/**
	 * @return возвращает фильтры в сыром формате (String)
	 */
	List<String> getRawFilters();
}
