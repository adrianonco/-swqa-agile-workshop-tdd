package edu.upc.talent.swqa.util.test;

import static edu.upc.talent.swqa.test.utils.Asserts.assertEquals;
import static edu.upc.talent.swqa.util.Utils.union;
import org.junit.jupiter.api.Test;

import java.util.Set;

public final class UtilsTest {

  @Test
  public void testSetUnion() {
    // Arrange
    final var set1 = Set.of(1, 3, 5);
    final var set2 = Set.of(3, 4, 6);
    // Act
    final var result = union(set1, set2);
    // Assert
    final var expected = Set.of(1,3,4,5,6);
    assertEquals(expected, result);
  }

}
