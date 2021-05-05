package org.roukou.dev.challenges.april;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class LongestWordsCollector implements Collector<String, Map<Integer, List<String>>, List<String>> {
	
    public Supplier<Map<Integer, List<String>>> supplier() {
    	return HashMap::new;
    }
    
    public BiConsumer<Map<Integer, List<String>>, String> accumulator() {
    	return (map, val) -> map.merge(val.length(), List.of(val), (v1, v2) -> Stream.concat(v1.stream(), v2.stream()).collect(Collectors.toList()));
    }
    
    public Function<Map<Integer, List<String>>, List<String>> finisher() {    	
    	return (map) -> map.get(Collections.max(map.keySet()));
    }
    
    public BinaryOperator<Map<Integer, List<String>>> combiner() {
    	return (map1, map2) -> {
            map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> Stream.concat(v1.stream(), v2.stream()).collect(Collectors.toList())));
            return map1;
        };
    }
    
    public Set<Characteristics> characteristics() {
    	return Collections.singleton(Characteristics.UNORDERED);
    }
	
    public static LongestWordsCollector getCollector() {
        return new LongestWordsCollector();
    }
    
}