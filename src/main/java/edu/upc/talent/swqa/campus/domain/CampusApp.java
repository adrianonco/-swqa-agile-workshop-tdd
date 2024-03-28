package edu.upc.talent.swqa.campus.domain;

import edu.upc.talent.swqa.campus.domain.exception.UserNotFoundException;
import edu.upc.talent.swqa.campus.domain.exception.UserNotTeacherException;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.SmtpEmailService;
import edu.upc.talent.swqa.jdbc.Database;
import static edu.upc.talent.swqa.jdbc.HikariCP.getDataSource;

public final class CampusApp {

  private final UsersRepository usersRepository;
  private final EmailService emailService;

  public CampusApp(final UsersRepository usersRepository, final EmailService emailService) {
    this.usersRepository = usersRepository;
    this.emailService = emailService;
  }

  public void sendMailToGroup(final String groupName, final String subject, final String body) {
    final var users = usersRepository.getUsersByGroup(groupName);
    users.forEach(u -> emailService.sendEmail(u, subject, body));
  }


  public void sendBirthdayEmails() {
    usersRepository.getUsersInBirthday().forEach(u ->
                                                       System.out.println("--------\n" +
                                                                          "to: " + u.email() + "\n" +
                                                                          "subject: Happy Campus Birthday!\n" +
                                                                          "body:\n" + "Happy campus birthday " +
                                                                          u.name() + " " + u.surname() + "!\n" +
                                                                          "You have been with us since " +
                                                                          u.createdAt() + "\n" +
                                                                          "--------\n")
    );
  }

  public void createUser(
        final String id,
        final String name,
        final String surname,
        final String email,
        final String role,
        final String groupName
  ) {
    usersRepository.createUser(id, name, surname, email, role, groupName);
  }

  public void createGroup(final String id, final String name) {
    usersRepository.createGroup(id, name);
  }

  public static CampusApp buildProductionApp() {
    final var dbHost = System.getenv("DATABASE_HOST");
    final var dbUser = System.getenv("DATABASE_USER");
    final var dbPassword = System.getenv("DATABASE_PASSWORD");
    final var db = new Database(getDataSource("jdbc:postgresql://" + dbHost + "/", dbUser, dbPassword));
    final var emailService = new SmtpEmailService();
    final var usersRepository = new PostgreSqlUsersRepository(db);
    return new CampusApp(usersRepository, emailService);
  }

  public void sendMailToGroupRole(final String groupName, final String roleName, final String subject, final String body) {
    final var users = usersRepository.getUsersByGroup(groupName);
    users.stream().filter(u -> u.role().equals(roleName))
          .forEach(u -> emailService.sendEmail(u, subject, body));
  }
/*
  // CampusApp.java: Implement the method to send an email to a teacher by ID
  public void sendEmailToTeacherById(String id, String subject, String body) {
    var user = usersRepository.getUserById(id); // Retrieve the user by ID
    emailService.sendEmail(user, subject, body); // Send the email
  }
 */

  /*
  // CampusApp.java: Update including custom exception classes
  public void sendEmailToTeacherById(String id, String subject, String body) {
    try {
      User user = usersRepository.getUserById(id);
      // Here, you would add a check for the user's role being "teacher"
      // and potentially throw UserNotTeacherException if not
      emailService.sendEmail(user, subject, body);
    } catch (UserNotFoundException | UserNotTeacherException e) {
      // Handle or rethrow the exception as appropriate for your application
      throw e;
    }
  }
   */

  /*public void sendEmailToTeacherById(String id, String subject, String body) {
    try {
      User user = usersRepository.getUserById(id);
      // Assuming the role of a teacher is identified by the string "teacher"
      // CampusApp.java: Applied specific role for users
      if (!user.hasRole("teacher")) {
        // Ensuring user is a teacher
        throw new UserNotTeacherException(id);
      }
      // Proceed to send the email
      emailService.sendEmail(user, subject, body);
    } catch (UserNotFoundException e) {
      throw e;
    }
  }*/

  // CampusApp.java: Break down method and separate user validation from email sending
  public void sendEmailToTeacherById(String id, String subject, String body) {
    User user = validateTeacherById(id);
    emailService.sendEmail(user, subject, body);
  }

  private User validateTeacherById(String id) {
    User user = usersRepository.getUserById(id);
    if (!user.hasRole("teacher")) {
      throw new UserNotTeacherException(id);
    }
    return user;
  }

}
