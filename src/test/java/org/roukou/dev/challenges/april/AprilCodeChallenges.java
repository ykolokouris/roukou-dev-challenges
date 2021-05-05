package org.roukou.dev.challenges.april;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

public class AprilCodeChallenges {

  @Nested
  class WeekThreeChallenges {

    @Test
    void custom_solution_by_thomas() {
      List<String> input =
          List.of("alfa", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel");

      int max = input.stream().mapToInt(String::length).max().orElse(-1);
      int min = input.stream().mapToInt(String::length).min().orElse(-1);

      var result =
          input.stream()
              .collect(
                  CustomCollectors.pairing(
                      Collectors.filtering(s -> s.length() == max, toList()),
                      Collectors.filtering(s -> s.length() == min, toList()),
                      Pair::of));

      assertAll(
          "Longest and Shortest Words",
          () -> assertEquals(List.of("charlie", "foxtrot"), result.getLeft()),
          () -> assertEquals(List.of("alfa", "echo", "golf"), result.getRight()));
    }
  }

  @Nested
  class WeekFourChallenges {

    /**
     * Given a string, split it into a list of strings consisting of consecutive characters from the
     * original string.
     */
    @Test
    public void challenge_one_character_shorting() {
      String input = "aaaaabbccccdeeeeeeaaafff";

      List<String> result = input.chars().mapToObj(c -> String.valueOf((char)c)).collect(SplitConsecutiveCharsCollector.getCollector());

      assertEquals("[aaaaa, bb, cccc, d, eeeeee, aaa, fff]", result.toString());
    }

    /**
     * Select the longest words from an input stream. That is, select the words whose lengths are
     * equal to the maximum word length.
     */
    @Test
    public void challenge_two_select_longest_word_in_one_pass() {
      Stream<String> input =
          Stream.of("alfa", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel")
              .parallel();

      List<String> result = input.collect(LongestWordsCollector.getCollector());

      assertEquals(Arrays.asList("charlie", "foxtrot"), result);
    }

    /**
     * Given a Map<X, Set<Y>>, convert it to Map<Y, Set<X>>. Each set member of the input map's
     * values becomes a key in the result map. Each key in the input map becomes a set member of the
     * values of the result map. In the input map, an item may appear in the value set of multiple
     * keys. In the result map, that item will be a key, and its value set will be its corresponing
     * keys from the input map.
     *
     * <p>In this case the input is Map<String, Set<Integer>> and the result is Map<Integer,
     * Set<String>>.
     */
    @Test
    public void challege_three_invert_multimap() {
      Map<String, Set<Integer>> input = new HashMap<>();
      input.put("a", new HashSet<>(Arrays.asList(1, 2)));
      input.put("b", new HashSet<>(Arrays.asList(2, 3)));
      input.put("c", new HashSet<>(Arrays.asList(1, 3)));
      input.put("d", new HashSet<>(Arrays.asList(1, 4)));
      input.put("e", new HashSet<>(Arrays.asList(2, 4)));
      input.put("f", new HashSet<>(Arrays.asList(3, 4)));

      Map<Integer, Set<String>> result = input.entrySet().stream().collect((new InvertMapCollector<String, Integer>()));

      assertEquals(new HashSet<>(Arrays.asList("a", "c", "d")), result.get(1));
      assertEquals(new HashSet<>(Arrays.asList("a", "b", "e")), result.get(2));
      assertEquals(new HashSet<>(Arrays.asList("b", "c", "f")), result.get(3));
      assertEquals(new HashSet<>(Arrays.asList("d", "e", "f")), result.get(4));
      assertEquals(4, result.size());
    }

    /**
     * Write a method that extracts all the superclasses and their implemented classes. Filter out
     * the abstract classes, then create a map with two boolean keys, true is associated to the
     * interfaces and false with the concrete classes. Do that for the provided classes, and arrange
     * the result in a Map<Class, ...> with those classes as the keys.
     */
    @Test
    public void challenge_four_map_of_interfaces_and_classes() {

      List<Class<?>> origin = List.of(ArrayList.class, HashSet.class, LinkedHashSet.class);

      Map<Class<?>, Map<Boolean, Set<Class<?>>>> result = origin.stream().collect(ClassesAndInterfacesMapCollector.getCollector());

      assertEquals(
          Map.of(
              ArrayList.class,
              Map.of(
                  false, Set.of(ArrayList.class, Object.class),
                  true,
                      Set.of(
                          List.class,
                          RandomAccess.class,
                          Cloneable.class,
                          Serializable.class,
                          Collection.class)),
              HashSet.class,
              Map.of(
                  false, Set.of(HashSet.class, Object.class),
                  true,
                      Set.of(
                          Set.class, Cloneable.class,
                          Serializable.class, Collection.class)),
              LinkedHashSet.class,
              Map.of(
                  false, Set.of(LinkedHashSet.class, HashSet.class, Object.class),
                  true,
                      Set.of(
                          Set.class, Cloneable.class,
                          Serializable.class, Collection.class))),
          result);
    }
  }
}
