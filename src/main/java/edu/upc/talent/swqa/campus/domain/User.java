package edu.upc.talent.swqa.campus.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public record User(
      String id,
      String name,
      String surname,
      String email,
      String role,
      String groupName,
      Instant createdAt
) {
  public LocalDate createdDate() {
    return LocalDate.ofInstant(createdAt, ZoneId.systemDefault());
  }
}
