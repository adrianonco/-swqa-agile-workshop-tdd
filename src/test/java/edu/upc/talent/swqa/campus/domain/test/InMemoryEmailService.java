package edu.upc.talent.swqa.campus.domain.test;

import edu.upc.talent.swqa.campus.domain.EmailService;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.test.utils.SentEmail;

import java.util.Set;

public class InMemoryEmailService implements EmailService {

  private final Set<SentEmail> state;

  public InMemoryEmailService(final Set<SentEmail> initialState) {
    state = initialState;
  }

  @Override public void sendEmail(final User to, final String subject, final String body) {
    state.add(new SentEmail(to.email(), subject, body));
  }

  public Set<SentEmail> getState() {
    return Set.copyOf(state);
  }

}
