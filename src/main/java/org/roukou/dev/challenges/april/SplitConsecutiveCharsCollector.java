package org.roukou.dev.challenges.april;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class SplitConsecutiveCharsCollector implements Collector<String, List<String>, List<String>> {
	
    public Supplier<List<String>> supplier() {
    	return ArrayList::new;
    }
    
    public BiConsumer<List<String>, String> accumulator() {
    	return (list, val) -> {
    		if (list.isEmpty()) {
    			list.add(val);
    		} else if (list.get(list.size()-1).contains(val)) {
    			list.set(list.size()-1, list.get(list.size()-1).concat(val));
    		} else {
    			list.add(val);
    		}
    	};
    }
    
    public Function<List<String>, List<String>> finisher() {    	
    	return (list) -> list;
    }
    
    public BinaryOperator<List<String>> combiner() {
    	return (list1, list2) -> {
    		list1.addAll(list2); // This will not always work correctly when parallel stream is used. Extra handling is needed. 
     		return list1;
    	};
    }
    
    public Set<Characteristics> characteristics() {
    	return Collections.singleton(Characteristics.UNORDERED);
    }
	
    public static SplitConsecutiveCharsCollector getCollector() {
        return new SplitConsecutiveCharsCollector();
    }
    
}
