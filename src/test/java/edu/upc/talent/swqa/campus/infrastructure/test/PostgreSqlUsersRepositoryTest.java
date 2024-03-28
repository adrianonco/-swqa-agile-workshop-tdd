package edu.upc.talent.swqa.campus.infrastructure.test;

import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import edu.upc.talent.swqa.jdbc.test.utils.DatabaseBackedTest;
import org.jetbrains.annotations.NotNull;

public final class PostgreSqlUsersRepositoryTest extends DatabaseBackedTest implements UsersRepositoryTest {

  @Override public UsersRepository getRepository(final UsersRepositoryState initialState) {
    PostgreSqlUsersRepositoryTestHelper.setInitialState(db, initialState);
    return new PostgreSqlUsersRepository(db);
  }

  @Override
  @NotNull
  public UsersRepositoryState getFinalState() {
    return PostgreSqlUsersRepositoryTestHelper.getFinalState(db);
  }

}