package edu.upc.talent.swqa.campus.domain;

public interface EmailService {

  void sendEmail(final User to, final String subject, final String body);
}
