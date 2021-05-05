package org.roukou.dev.challenges.april;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ClassesAndInterfacesMapCollector implements Collector<Class<?>, Map<Class<?>, Map<Boolean, Set<Class<?>>>>,  Map<Class<?>, Map<Boolean, Set<Class<?>>>>> {
	
    public Supplier<Map<Class<?>, Map<Boolean, Set<Class<?>>>>> supplier() {
    	return HashMap::new;
    }
    
    public BiConsumer<Map<Class<?>, Map<Boolean, Set<Class<?>>>>, Class<?>> accumulator() {
    	return (map, cls) -> {
    		map.put(cls, getInterfacesAndClasses(cls, new HashMap<>()));
    	};
    }
    
    public Function<Map<Class<?>, Map<Boolean, Set<Class<?>>>>,  Map<Class<?>, Map<Boolean, Set<Class<?>>>>> finisher() {    	
    	return (map) -> map;
    }
    
    public BinaryOperator<Map<Class<?>, Map<Boolean, Set<Class<?>>>>> combiner() {
    	return (map1, map2) -> {
            map2.forEach((k, v) -> map1.merge(k, v, (m1, m2) ->  m1 ));
            return map1;
        };
    }
    
    public Set<Characteristics> characteristics() {
    	return Collections.singleton(Characteristics.UNORDERED);
    }
	
    public static ClassesAndInterfacesMapCollector getCollector() {
        return new ClassesAndInterfacesMapCollector();
    }
    
	private static Map<Boolean, Set<Class<?>>> getInterfacesAndClasses(Class<?> cls, Map<Boolean, Set<Class<?>>> map) {
		if (cls == null) return map;
		
		if (!Modifier.isAbstract(cls.getModifiers())) map.merge(false, Set.of(cls), (s1, s2) -> Stream.concat(s1.stream(), s2.stream()).collect(Collectors.toSet()));		
		map.merge(true, new HashSet<>(Arrays.asList(cls.getInterfaces())), (s1, s2) -> Stream.concat(s1.stream(), s2.stream()).collect(Collectors.toSet()));
		
		return getInterfacesAndClasses(cls.getSuperclass(), map);
	}
    
}