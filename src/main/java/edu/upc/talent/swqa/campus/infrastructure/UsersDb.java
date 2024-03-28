package edu.upc.talent.swqa.campus.infrastructure;

public final class UsersDb {

  private UsersDb() {}
  public static final String usersTableDml =
        """
        create table if not exists users (
            id text primary key,
            name text not null,
            surname text not null,
            email text not null,
            role text not null,
            group_id text not null,
            created_at timestamptz not null default now(),
            active boolean not null default true,
            foreign key (group_id) references groups(id)
        )
        """;

  public static final String groupsTableDml =
        """
        create table if not exists groups (
            id text primary key,
            name text not null
        )
        """;
}
