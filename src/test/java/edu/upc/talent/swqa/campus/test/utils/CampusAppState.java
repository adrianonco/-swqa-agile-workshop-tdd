package edu.upc.talent.swqa.campus.test.utils;

import java.util.HashSet;
import java.util.Set;

public record CampusAppState(UsersRepositoryState usersRepositoryState, Set<SentEmail> sentEmails) {

  public CampusAppState copy() {return new CampusAppState(usersRepositoryState.copy(), new HashSet<>(sentEmails));}
}


