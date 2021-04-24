package org.roukou.dev.challenges.april;

import java.util.EnumSet;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

public class CustomCollectors {

  static <T, A1, A2, R1, R2, R> Collector<T, ?, R> pairing(
      Collector<T, A1, R1> c1, Collector<T, A2, R2> c2, BiFunction<R1, R2, R> finisher) {

    EnumSet<Characteristics> c = EnumSet.noneOf(Characteristics.class);

    c.addAll(c1.characteristics());
    c.retainAll(c2.characteristics());
    c.remove(Characteristics.IDENTITY_FINISH);

    return Collector.of(
        () -> new Object[] {c1.supplier().get(), c2.supplier().get()},
        (acc, v) -> {
          c1.accumulator().accept((A1) acc[0], v);
          c2.accumulator().accept((A2) acc[1], v);
        },
        (acc1, acc2) -> {
          acc1[0] = c1.combiner().apply((A1) acc1[0], (A1) acc2[0]);
          acc1[1] = c2.combiner().apply((A2) acc1[1], (A2) acc2[1]);
          return acc1;
        },
        acc -> {
          R1 r1 = c1.finisher().apply((A1) acc[0]);
          R2 r2 = c2.finisher().apply((A2) acc[1]);
          return finisher.apply(r1, r2);
        },
        c.toArray(new Characteristics[c.size()]));
  }
}
