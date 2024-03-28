package edu.upc.talent.swqa.test.utils.test;

import edu.upc.talent.swqa.test.utils.Asserts;
import edu.upc.talent.swqa.test.utils.Diff;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;


public final class AssertsTest {

  record Record1(int a, String b) {}


  @Test
  public void testAtomicDiff() {
    assertEquals(new Diff.AtomicDiff(1, 4), Asserts.diff(1, 4));
    assertNull(Asserts.diff("Hi", "Hi"));
  }

  @Test
  public void testRecordDiff() {
    final var objExpected = new Record1(1, "Hi");
    final var objActual = new Record1(4, "Bye");
    final var expected = new Diff.RecordDiff(Map.of(
          "a", new Diff.AtomicDiff(1, 4),
          "b", new Diff.AtomicDiff("Hi", "Bye")
    ));
    assertEquals(expected, Asserts.diff(objExpected, objActual));
    assertNull(Asserts.diff(objExpected, objExpected));
  }

  @Test
  public void testMapDiff() {
    final var objExpected = Map.of("a", 1, "b", "Hi");
    final var objActual = Map.of("a", 4, "b", "Bye");
    final var expected = new Diff.RecordDiff(Map.of(
          "a", new Diff.AtomicDiff(1, 4),
          "b", new Diff.AtomicDiff("Hi", "Bye")
    ));
    assertEquals(expected, Asserts.diff(objExpected, objActual));
    assertNull(Asserts.diff(objExpected, objExpected));
  }

  @Test
  public void testSequenceDiff() {
    final var objExpected = List.of(1, "Hi");
    final var objActual = List.of(4, "Bye");
    final var expected = new Diff.SequenceDiff(Map.of(
          0, new Diff.AtomicDiff(1, 4),
          1, new Diff.AtomicDiff("Hi", "Bye")
    ));
    assertEquals(expected, Asserts.diff(objExpected, objActual));
    assertNull(Asserts.diff(objExpected, objExpected));
  }

  @Test
  public void testSetDiff() {
    final var expected = new Diff.SetDiff(Set.of(1, 3), Set.of(5, 7));
    final var objExpected = Set.of(1, 2, 3, 4);
    final var objActual = Set.of(2, 4, 5, 7);
    assertEquals(expected, Asserts.diff(objExpected, objActual));
    assertNull(Asserts.diff(objExpected, objExpected));
  }

  @Test
  public void testNestedDiff() {
    final var objExpected = List.of(new Record1(2, "Hi"), new Record1(3, "Hi"));
    final var objActual = List.of(new Record1(2, "Hi"), new Record1(5, "Hi"));
    final var expected = new Diff.SequenceDiff(Map.of(1, new Diff.RecordDiff(Map.of("a", new Diff.AtomicDiff(3, 5)))));
    final var actual = Asserts.diff(objExpected, objActual);
    assertEquals(expected, actual);
    assertNull(Asserts.diff(objExpected, objExpected));
  }

}
