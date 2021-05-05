package org.roukou.dev.challenges.april;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class InvertMapCollector<X, Y> implements Collector<Map.Entry<X, Set<Y>>, Map<Y, Set<X>>, Map<Y, Set<X>>> {
	
    public Supplier<Map<Y, Set<X>>> supplier() {
    	return HashMap::new;
    }
    
    public BiConsumer<Map<Y, Set<X>>, Map.Entry<X, Set<Y>>> accumulator() {
    	return (map, entry) -> { 
    		entry.getValue().forEach(
    				y -> map.merge(
							y,  
							Set.of(entry.getKey()), 
							(v1, v2) -> Stream.concat(v1.stream(), v2.stream()).collect(Collectors.toSet()) 
					)); 
    	};
    }
    
    public Function<Map<Y, Set<X>>, Map<Y, Set<X>>> finisher() {    	
    	return (map) -> map;
    }
    
    public BinaryOperator<Map<Y, Set<X>>> combiner() {
    	return (map1, map2) -> {
            map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> Stream.concat(v1.stream(), v2.stream()).collect(Collectors.toSet()) ));
            return map1;
        };
    }
    
    public Set<Characteristics> characteristics() {
    	return Collections.singleton(Characteristics.UNORDERED);
    }
	    
}