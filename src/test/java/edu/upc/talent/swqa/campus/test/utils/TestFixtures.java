package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.domain.User;
import static edu.upc.talent.swqa.util.Utils.now;

import java.time.temporal.ChronoUnit;
import java.util.Set;

public final class TestFixtures {

  private TestFixtures() {}

  public static final CampusAppState defaultInitialState;
  public static final Group swqa = new Group("1", "swqa");
  public static final Group aiMl = new Group("2", "AI/ML");

  public static final User johnDoe = new User(
        "1",
        "John",
        "Doe",
        "john.doe@example.com",
        "student",
        swqa.name(),
        now().minus(200, ChronoUnit.DAYS)
  );

  public static final User janeDoe = new User(
        "2",
        "Jane",
        "Doe",
        "jane.doe@example.com",
        "student",
        swqa.name(),
        now().minus(100, ChronoUnit.DAYS)
  );

  public static final User mariahHairam = new User(
        "3",
        "Mariah",
        "Harris",
        "mariah.hairam@example.com",
        "teacher",
        swqa.name(),
        now().minus(300, ChronoUnit.DAYS)
  );
  public static final User rohnNhor = new User(
        "4",
        "Rohn",
        "Nhor",
        "rohn.nhor@example.com",
        "student",
        aiMl.name(),
        now().minus(150, ChronoUnit.DAYS)
  );

  static {
    defaultInitialState = new CampusAppState(
          new UsersRepositoryState(
                Set.of(johnDoe, janeDoe, mariahHairam, rohnNhor),
                Set.of(swqa, aiMl)
          ),
          Set.of()
    );
  }


}
