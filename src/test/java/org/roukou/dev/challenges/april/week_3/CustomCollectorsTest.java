package org.roukou.dev.challenges.april.week_3;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomCollectorsTest {

  @Nested
  class SolutionByThomas {

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


    @Test
    void stream_ex_library_by_thomas() {}

    @Test
    @EnabledOnJre(JRE.JAVA_12)
    void teeing_collectors_by_java_12() {}
  }
}
