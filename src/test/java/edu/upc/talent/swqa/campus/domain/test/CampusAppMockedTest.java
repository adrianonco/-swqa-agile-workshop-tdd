package edu.upc.talent.swqa.campus.domain.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.EmailService;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.defaultInitialState;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.janeDoe;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.johnDoe;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.mariahHairam;
import static edu.upc.talent.swqa.campus.test.utils.TestFixtures.swqa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

public final class CampusAppMockedTest {

  private CampusApp app;
  private EmailService emailService;
  private UsersRepository usersRepository;

  @BeforeEach
  public void setUp() {
    usersRepository = mock(UsersRepository.class);
    emailService = mock(EmailService.class);
    app = new CampusApp(usersRepository, emailService);
    when(usersRepository.getUsersByGroup(swqa.name())).thenReturn(List.of(johnDoe, janeDoe, mariahHairam));
  }

  @Test
  public void testCreateGroup() {
    app.createGroup("1", "bigdata");
    verify(usersRepository).createGroup(any(), eq("bigdata"));
    verifyNoMoreInteractions(usersRepository);
    verifyNoMoreInteractions(emailService);
  }


  @Test
  public void testSendEmailToGroup() {
    final var subject = "New campus!";
    final var body = "Hello everyone! We just created a new virtual campus!";
    app.sendMailToGroup("swqa", subject, body);
    verify(emailService).sendEmail(johnDoe, subject, body);
    verify(emailService).sendEmail(janeDoe, subject, body);
    verify(emailService).sendEmail(mariahHairam, subject, body);
    verifyNoMoreInteractions(emailService);
  }

  @Test
  public void testSendEmailToGroupRole() {
    final var subject = "Hey! Teacher!";
    final var body = "Let them students alone!!";
    app.sendMailToGroupRole("swqa", "teacher", subject, body);
    verify(emailService).sendEmail(mariahHairam, subject, body);
    verifyNoMoreInteractions(emailService);
  }
}
