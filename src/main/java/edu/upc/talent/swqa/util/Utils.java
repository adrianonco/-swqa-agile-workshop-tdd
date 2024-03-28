package edu.upc.talent.swqa.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Utils {

  private Utils() {}

  public static boolean eq(final Object o1, final Object o2) {
    return Objects.equals(o1, o2);
  }

  public static <A> List<A> join(final List<A> list1, final List<A> list2) {
    final var res = new ArrayList<>(list1);
    res.addAll(list2);
    return res;
  }

  public static <A> Set<A> union(final Set<A> set1, final Set<A> set2) {
    final var res = new HashSet<>(set1);
    res.addAll(set2);
    return res;
  }

  public static <A> Set<A> plus(final Set<A> set1, final A elem) {
    final var res = new HashSet<>(set1);
    res.add(elem);
    return res;
  }

  public static <A> Set<A> minus(final Set<A> set1, final Set<A> elem) {
    final var res = new HashSet<>(set1);
    res.removeAll(elem);
    return res;
  }

  public static Instant now() {return Instant.now().truncatedTo(ChronoUnit.SECONDS);}

}

