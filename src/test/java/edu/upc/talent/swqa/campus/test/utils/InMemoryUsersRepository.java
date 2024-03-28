package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.domain.BirthdayEmailData;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import static edu.upc.talent.swqa.util.Utils.now;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public record InMemoryUsersRepository(UsersRepositoryState state) implements UsersRepository {

  @Override
  public void createUser(
        final String id,
        final String name,
        final String surname,
        final String email,
        final String role,
        final String groupName
  ) {
    final var user = new User(id, name, surname, email, role, groupName, now());
    state.users().add(user);
  }

  @Override
  public void createGroup(final String id, final String name) {
    state.groups().add(new Group(id, name));
  }


  @Override
  public List<User> getUsersByGroup(final String groupName) {
    return state.users().stream()
                .filter(user -> user.groupName().equals(groupName))
                .toList();
  }

  @Override public List<BirthdayEmailData> getUsersInBirthday() {
    final var today = LocalDate.now();
    return state.users().stream()
                .filter(user -> {
                  final var createdDate = user.createdDate();
                  return createdDate.getDayOfMonth() == today.getDayOfMonth() &&
                         createdDate.getMonth() == today.getMonth();
                })
                .map(user -> new BirthdayEmailData(
                      user.email(),
                      user.name(),
                      user.surname(),
                      user.createdAt().toString()
                ))
                .toList();
  }

  @Override
  public User getUserById(String id) {
    // InMemoryUsersRepository.java: Retrieves a user by their ID from an in-memory state.
    // Initiates a stream from the collection of users in the in-memory state.
    return state.users().stream()
            // Filters the stream to only include the user with the matching ID.
            .filter(user -> user.id().equals(id))
            // Attempts to find the first (and should be only) user in the stream after filtering.
            .findFirst()
            // Throws an exception if no user matches the provided ID.
            .orElseThrow(() -> new NoSuchElementException("User " + id + " does not exist"));
  }

}
