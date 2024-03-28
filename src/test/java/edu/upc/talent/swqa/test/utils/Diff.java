package edu.upc.talent.swqa.test.utils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public sealed interface Diff {
  String toString(final boolean newLine, final int indent);

  record RecordDiff(Map<String, Diff> diffs) implements Diff {
    @Override
    public String toString() {
      return toString(false, 0);
    }

    public String toString(final boolean newLine, final int indent) {
      final var spaces = " ".repeat(indent);
      final var sep = (diffs.size() != 1 || newLine) ? "\n" + spaces : "";
      return sep + diffs.keySet()
                        .stream()
                        .sorted()
                        .map(k -> k + ": " + diffs.get(k).toString(false, indent + 2))
                        .reduce((a, b) -> a + "\n" + spaces + b)
                        .orElse("");
    }
  }

  record SequenceDiff(Map<Integer, Diff> diffs) implements Diff {

    public SequenceDiff {
      Objects.requireNonNull(diffs);
      assert (!diffs.isEmpty());
    }


    @Override
    public String toString() {
      return toString(false, 0);
    }

    public String toString(final boolean newLine, final int indent) {
      final var spaces = " ".repeat(indent);
      final var sep = (diffs.size() != 1 || newLine) ? "\n" + spaces : "";
      return sep + diffs.keySet()
                        .stream()
                        .sorted()
                        .map(k -> k + ": " + diffs.get(k).toString(false, indent + 2))
                        .reduce((a, b) -> a + "\n" + spaces + b)
                        .orElse("");
    }
  }

  record SetDiff(Set<Object> missing, Set<Object> unexpected) implements Diff {
    @Override
    public String toString(final boolean newLine, final int indent) {
      final var sMissing = missing.toString();
      final var sUnexpected = unexpected.toString();
      if (newLine || sMissing.length() + sUnexpected.length() > 100) {
        final var spaces = " ".repeat(indent);
        return "\n" + spaces + "missing: " + sMissing + "\n" + spaces + "unexpected: " + sUnexpected;
      } else {
        return "missing: " + missing + ", unexpected: " + unexpected;
      }
    }
  }

  record AtomicDiff(Object expected, Object actual) implements Diff {
    @Override
    public String toString() {
      return toString(false, 0);
    }

    @Override
    public String toString(final boolean newLine, final int indent) {
      final var sExpected = Objects.toString(expected);
      final var sActual = Objects.toString(actual);
      if (newLine || sExpected.length() + sActual.length() > 100) {
        final var spaces = " ".repeat(indent);
        return "\n" + spaces + "expected: " + sExpected + "\n" + spaces + "actual: " + sActual;
      } else {
        return "expected: " + expected + ", actual: " + actual;
      }
    }

  }

}