package edu.upc.talent.swqa.jdbc.test.utils;

import java.util.ArrayList;
import java.util.List;

public final class ConsoleUtils {

  private ConsoleUtils() {}
  public static void printAlignedTable(final List<List<String>> table) {
    final var columnWidths = new ArrayList<Integer>();
    for (final var row : table) {
      for (int i = 0; i < row.size(); i++) {
        final var cell = row.get(i);
        if (columnWidths.size() <= i) {
          columnWidths.add(cell.length());
        } else {
          columnWidths.set(i, Math.max(columnWidths.get(i), cell.length()));
        }
      }
    }
    for (final var row : table) {
      for (var i = 0; i < row.size(); i++) {
        final var cell = row.get(i);
        System.out.print(cell);
        for (var j = 0; j < columnWidths.get(i) - cell.length(); j++) System.out.print(" ");
        System.out.print(" ");
      }
      System.out.println();
    }
  }
}
