package edu.upc.talent.swqa.test.utils;


import static edu.upc.talent.swqa.util.Utils.minus;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class Asserts {

  private Asserts() {}

  public static void assertEquals(final Object expected, final Object actual) {
    final var diffs = diff(expected, actual);
    if (diffs != null) {
      throw new AssertionError("Actual value did not match the expected value:" + diffs.toString(true, 2));
    }
  }

  @SuppressWarnings("unchecked")
  public static Diff diff(final Object expected, final Object actual) {
    try {
      if (expected == null && actual == null) {
        return null;
      } else if (expected == null || actual == null) {
        return new Diff.AtomicDiff(expected, actual);
      } else if (expected.equals(actual)) {
        return null;
      } else if (expected instanceof Map<?, ?> && actual instanceof Map<?, ?>) {
        return mapDiff((Map<Object, Object>) expected, (Map<Object, Object>) actual);
      } else if (expected instanceof Set<?> && actual instanceof Set<?>) {
        return setDiff((Set<Object>) expected, (Set<Object>) actual);
      } else if (expected instanceof Iterable<?> && actual instanceof Iterable<?>) {
        return iterableDiff((Iterable<Object>) expected, (Iterable<Object>) actual);
      } else {
        return mapDiff(asMap(expected), asMap(actual));
      }
    } catch (final Exception e) {
      return new Diff.AtomicDiff(expected, actual);
    }
  }

  private static Diff mapDiff(final Map<Object, Object> expected, final Map<Object, Object> actual) {
    if (expected.equals(actual)) {
      return null;
    } else {
      final var diffs = new HashMap<String, Diff>();
      for (final var key : expected.keySet()) {
        final var diff = diff(expected.get(key), actual.get(key));
        if (diff != null) {
          diffs.put(key.toString(), diff);
        }
      }
      for (final var key : actual.keySet()) {
        if (!expected.containsKey(key)) {
          diffs.put(key.toString(), new Diff.AtomicDiff(null, actual.get(key)));
        }
      }
      return new Diff.RecordDiff(diffs);
    }
  }

  private static Diff setDiff(final Set<Object> expected, final Set<Object> actual) {
    if (expected.equals(actual)) {
      return null;
    } else {
      return new Diff.SetDiff(minus(expected, actual), minus(actual, expected));
    }
  }

  private static Diff iterableDiff(final Iterable<Object> expected, final Iterable<Object> actual) {
    if (expected.equals(actual)) {
      return null;
    } else {
      var i = 0;
      final var itExpected = expected.iterator();
      final var itActual = actual.iterator();
      final var diffs = new HashMap<Integer, Diff>();
      while (itExpected.hasNext() && itActual.hasNext()) {
        final var diff = diff(itExpected.next(), itActual.next());
        if (diff != null) {
          diffs.put(i, diff);
        }
        i++;
      }
      while (itExpected.hasNext()) {
        diffs.put(i, new Diff.AtomicDiff(itExpected.next(), null));
        i++;
      }
      while (itActual.hasNext()) {
        diffs.put(i, new Diff.AtomicDiff(null, itActual.next()));
        i++;
      }
      if(diffs.isEmpty()) return new Diff.AtomicDiff(expected, actual);
      return new Diff.SequenceDiff(diffs);
    }
  }

  private static Map<Object, Object> asMap(final Object object) {
    return Arrays.stream(object.getClass().getDeclaredFields()).collect(Collectors.toMap(
          Field::getName,
          field -> {
            field.setAccessible(true);
            try {
              return field.get(object);
            } catch (final IllegalAccessException e) {
              throw new RuntimeException(e);
            }
          }
    ));
  }

}
