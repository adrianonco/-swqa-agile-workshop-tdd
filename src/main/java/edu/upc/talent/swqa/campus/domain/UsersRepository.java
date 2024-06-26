package edu.upc.talent.swqa.campus.domain;

import java.util.List;

public interface UsersRepository {
  List<User> getUsersByGroup(final String groupName);
  List<BirthdayEmailData> getUsersInBirthday();

  void createUser(final String id, final String name, final String surname, final String email, final String role, final String groupName);

  void createGroup(final String id, final String name);

  // UserRepository.java: Method declaration to retrieve a User object
  // by their unique identifier (ID).
  User getUserById(String id);

}
