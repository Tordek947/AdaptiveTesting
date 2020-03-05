package net.atlassian.cmathtutor.adaptive.domain.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Mapper<CD, D, E> {

    E dataToEntity(CD data);

    D entityToData(E entity);

    static <T_FROM, T_TO_KEY, T_TO_VALUE> Map<T_TO_KEY, T_TO_VALUE> collectionToMap(Collection<T_FROM> collection,
	    Function<T_FROM, Entry<T_TO_KEY, T_TO_VALUE>> mapper) {
	return collection.stream().map(mapper)
		.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    static <T_FROM, T_TO> List<T_TO> collectionToList(Collection<T_FROM> collection, Function<T_FROM, T_TO> mapper) {
	return collection.stream().map(mapper).collect(Collectors.toList());
    }
}
