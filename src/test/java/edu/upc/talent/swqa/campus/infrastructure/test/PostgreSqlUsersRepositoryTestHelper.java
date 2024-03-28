package edu.upc.talent.swqa.campus.infrastructure.test;

import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import edu.upc.talent.swqa.jdbc.Database;
import static edu.upc.talent.swqa.jdbc.Param.p;

public final class PostgreSqlUsersRepositoryTestHelper {

  private PostgreSqlUsersRepositoryTestHelper() {
  }

  public static void setInitialState(final Database db, final UsersRepositoryState state) {
    db.update(UsersDb.groupsTableDml);
    db.update(UsersDb.usersTableDml);
    db.update("delete from users");
    db.update("delete from groups");
    state.groups().forEach(g -> db.update("insert into groups (id, name) values (?, ?)", p(g.id()), p(g.name())));
    state.users().forEach(u -> db.update(
          "insert into users (id, name, surname, email, role, group_id, created_at) " +
          "values (?, ?, ?, ?, ?, (select id from groups where name = ?), ?)",
          p(u.id()),
          p(u.name()),
          p(u.surname()),
          p(u.email()),
          p(u.role()),
          p(u.groupName()),
          p(u.createdAt())
    ));
  }


  public static UsersRepositoryState getFinalState(final Database db) {
    return new UsersRepositoryState(
          db.selectToSet(
                """
                select u.id, u.name, u.surname, u.email, u.role, g.name, u.created_at
                from users u join groups g on u.group_id = g.id""",
                (rs) -> new User(
                      rs.getString(1),
                      rs.getString(2),
                      rs.getString(3),
                      rs.getString(4),
                      rs.getString(5),
                      rs.getString(6),
                      rs.getInstant(7)
                )
          ),
          db.selectToSet(
                "SELECT id, name FROM groups",
                (rs) -> new Group(rs.getString(1), rs.getString(2))
          )
    );
  }

}
