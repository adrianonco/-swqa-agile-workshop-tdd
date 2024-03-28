package edu.upc.talent.swqa.campus.e2e;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.exception.UserNotFoundException;
import edu.upc.talent.swqa.campus.domain.exception.UserNotTeacherException;
import edu.upc.talent.swqa.campus.domain.test.InMemoryEmailService;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.test.PostgreSqlUsersRepositoryTestHelper;
import edu.upc.talent.swqa.campus.test.utils.CampusAppState;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.SentEmail;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.defaultInitialState;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.janeDoe;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.johnDoe;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.mariahHairam;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.swqa;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import edu.upc.talent.swqa.jdbc.test.utils.DatabaseBackedTest;
import static edu.upc.talent.swqa.test.utils.Asserts.assertEquals;
import static edu.upc.talent.swqa.util.Utils.plus;
import static edu.upc.talent.swqa.util.Utils.union;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public final class CampusAppEndToEndTest extends DatabaseBackedTest {

  private Set<SentEmail> emailServiceState = new HashSet<>();

  public CampusApp getApp(final CampusAppState initialState) {
    PostgreSqlUsersRepositoryTestHelper.setInitialState(db, initialState.usersRepositoryState());
    final var repo = new PostgreSqlUsersRepository(db);
    emailServiceState = new HashSet<>(Set.copyOf(initialState.sentEmails()));
    return new CampusApp(repo, new InMemoryEmailService(emailServiceState));
  }


  private CampusAppState getFinalState() {
    return new CampusAppState(PostgreSqlUsersRepositoryTestHelper.getFinalState(db), emailServiceState);
  }

  @Test
  public void testCreateGroup() {
    final var app = getApp(defaultInitialState);
    final var group = new Group("3", "bigdata");
    app.createGroup(group.id(), group.name());
    final var expectedState = new CampusAppState(
          new UsersRepositoryState(
                defaultInitialState.usersRepositoryState().users(),
                plus(defaultInitialState.usersRepositoryState().groups(), group)
          ),
          defaultInitialState.sentEmails()
    );
    assertEquals(expectedState, getFinalState());
  }

  @Test
  public void testSendEmailToGroup() {
    final var app = getApp(defaultInitialState);
    final var subject = "New campus!";
    final var body = "Hello everyone! We just created a new virtual campus!";
    app.sendMailToGroup(swqa.name(), subject, body);
    final var expectedNewEmails = Set.of(
          new SentEmail(johnDoe.email(), subject, body),
          new SentEmail(janeDoe.email(), subject, body),
          new SentEmail(mariahHairam.email(), subject, body)
    );
    final var expectedState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          union(defaultInitialState.sentEmails(), expectedNewEmails)
    );
    assertEquals(expectedState, getFinalState());
  }

  @Test
  public void testSendEmailToGroupRole() {
    final var app = getApp(defaultInitialState);
    final var roleName = "teacher";
    final var subject = "Hey! Teacher!";
    final var body = "Let them students alone!!";
    app.sendMailToGroupRole(swqa.name(), roleName, subject, body);
    final var expectedNewEmails = Set.of(new SentEmail(mariahHairam.email(), subject, body));
    final var expectedState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          union(defaultInitialState.sentEmails(), expectedNewEmails)
    );
    assertEquals(expectedState, getFinalState());
  }

  @Test
  public void testSendEmailToTeacherById() {
    // Arrange: Set up the application state and identify a teacher by ID
    var app = getApp(defaultInitialState);
    var teacherId = "3"; // Identified Mariah Hairam as the teacher
    var subject = "Meeting Reminder";
    var body = "Don't forget about the meeting tomorrow at 10 AM.";

    // Act: Attempt to send an email to the identified teacher
    app.sendEmailToTeacherById(teacherId, subject, body);

    // Assert: Verify that the expected email was sent
    var expectedEmail = new SentEmail(mariahHairam.email(), subject, body);
    var sentEmails = emailServiceState;
    assertEquals(Set.of(expectedEmail), sentEmails);
  }

  @Test
  public void testSendEmailToNonExistentTeacherByIdThrowsException() {
    // CampusAppEndToEndTest.java
    // Arrange: Set up with an ID known not to exist in the system
    var app = getApp(defaultInitialState);
    var nonExistentTeacherId = "9999";
    var subject = "Meeting Reminder";
    var body = "Don't forget about the meeting tomorrow at 10 AM.";

    // Act & Assert: Attempt to send an email and expect a UserNotFoundException
    // CampusAppEndToEndTest.java: Change RuntimeException
    // to UserNotFoundException(id)
    Exception exception = assertThrows(UserNotFoundException.class, () -> {
      app.sendEmailToTeacherById(nonExistentTeacherId, subject, body);
    });

    // Verify the correct exception message is thrown
    assertEquals("User 9999 does not exist", exception.getMessage());
  }

  @Test
  public void testSendEmailToUserWhoIsNotTeacherThrowsException() {
    // Arrange: Setup with a known non-teacher user ID
    var app = getApp(defaultInitialState);
    var nonTeacherUserId = "1"; // Assuming John Doe is not a teacher

    // Act & Assert: Verify that trying to send an email to a non-teacher
    // throws an exception
    assertThrows(UserNotTeacherException.class, () -> {
      app.sendEmailToTeacherById(nonTeacherUserId, "Meeting Reminder", "Please prepare the monthly report.");
    });
  }

}
