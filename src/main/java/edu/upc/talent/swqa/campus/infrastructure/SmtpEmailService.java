package edu.upc.talent.swqa.campus.infrastructure;

import edu.upc.talent.swqa.campus.domain.EmailService;
import edu.upc.talent.swqa.campus.domain.User;

public class SmtpEmailService implements EmailService {
  @Override public void sendEmail(final User to, final String subject, final String body) {

      System.out.println("--------\n" +
                         "to: " + to.email() + "\n" +
                         "subject: " + subject + "\n" +
                         "body:\n" + body + "\n" +
                         "--------\n");

  }
}
