package edu.upc.talent.swqa.campus.infrastructure;

import edu.upc.talent.swqa.campus.domain.BirthdayEmailData;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.domain.exception.UserNotFoundException;
import edu.upc.talent.swqa.jdbc.Database;
import static edu.upc.talent.swqa.jdbc.Param.p;

import java.sql.SQLException;
import java.util.List;

public class PostgreSqlUsersRepository implements UsersRepository {

  private final Database db;

  public PostgreSqlUsersRepository(final Database db) {
    this.db = db;
  }

  public List<User> getUsersByGroup(final String groupName) {
    return db.select(
          """
          select u.id, u.name, u.surname, u.email, u.role, u.created_at
          from users u join groups g on u.group_id = g.id
          where u.active and g.name = ?""",
          (rs) -> new User(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                groupName,
                rs.getInstant(6)
          ),
          p(groupName)
    );
  }


  @Override public List<BirthdayEmailData> getUsersInBirthday() {
    return db.select(
          "select email, name, surname, created_at from users where (now() - created_at) > '1 days' and  to_char(created_at, 'MM-dd') = to_char(now(), 'MM-dd')",
          (rs) -> new BirthdayEmailData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4))
    );
  }

  @Override public void createUser(
        final String id,
        final String name,
        final String surname,
        final String email,
        final String role,
        final String groupName
  ) {
    db.update(
          "insert into users (id, name, surname, email, role, group_id) " +
          "values (?, ?, ?, ?, ?, (select id from groups where name = ?))",
          p(id),
          p(name),
          p(surname),
          p(email),
          p(role),
          p(groupName)
    );
  }

  @Override public void createGroup(final String id, final String name) {
    db.update("insert into groups (id, name) values (?, ?)", p(id), p(name));
  }

  /*@Override
  public User getUserById(String id) {
    // PostgreSqlUsersRepository.java: Executes a SQL query to retrieve a user by their ID from the database.
    // The query joins the 'users' table with the 'groups' table to also fetch the group name associated with the user.
    return db.selectOne(
            """
            SELECT u.id, u.name, u.surname, u.email, u.role, g.name AS group_name, u.created_at
            FROM users u
            JOIN groups g ON u.group_id = g.id
            WHERE u.id = ?""", // The '?' is a placeholder for the user ID parameter in the query.
            rs -> new User(
                    rs.getString(1), // Extracts the user's ID from the query result.
                    rs.getString(2), // Extracts the user's name.
                    rs.getString(3), // Extracts the user's surname.
                    rs.getString(4), // Extracts the user's email.
                    rs.getString(5), // Extracts the user's role.
                    rs.getString(6), // Extracts the group name to which the user belongs.
                    rs.getInstant(7) // Extracts the timestamp when the user was created.
            ),
            p(id) // Binds the provided user ID to the placeholder '?' in the SQL query.
    );
  }*/

  @Override
  public User getUserById(String id) {
    // Update PostgreSqlUsersRepository.java
    try {
      return db.selectOne(
              // SQL query remains the same
              """
              SELECT u.id, u.name, u.surname, u.email, u.role, g.name AS group_name, u.created_at
              FROM users u
              JOIN groups g ON u.group_id = g.id
              WHERE u.id = ?""",
              // ResultSet mapping remains the same
              rs -> new User(
                      rs.getString(1), rs.getString(2), rs.getString(3),
                      rs.getString(4), rs.getString(5), rs.getString(6),
                      rs.getInstant(7)
              ),
              p(id)
      );
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause instanceof SQLException && cause.getMessage().contains("No rows returned")) {
        // PostgreSqlUsersRepository.java: Change RuntimeException
        // to UserNotFoundException(id)
        throw new UserNotFoundException(id);
      } else {
        throw e; // Re-throw the original exception if it's not the specific case we're checking for
      }
    }
  }

}
