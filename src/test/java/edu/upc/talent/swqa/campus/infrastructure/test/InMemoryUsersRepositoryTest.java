package edu.upc.talent.swqa.campus.infrastructure.test;

import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.test.utils.InMemoryUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;

public final class InMemoryUsersRepositoryTest implements UsersRepositoryTest {

  private UsersRepositoryState state;

  @Override public UsersRepository getRepository(final UsersRepositoryState initialState) {
    this.state = initialState.copy();
    return new InMemoryUsersRepository(state);
  }

  @Override
  public UsersRepositoryState getFinalState() {
    return state;
  }
}
