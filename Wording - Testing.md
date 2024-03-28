# SWQA: Agile testing workshop

## Questions

1. For each one of the following classes, pick an example of a test where the pattern "Arrange-Act-Assert" or "AAA with state" are applied and identify clearly the three parts of the pattern.
    - `CampusAppTest`
    - `CampusAppMockedTest`
    - `CampusAppEndToEndTest`
    - `UsersRepositoryTest`
2. Briefly describe the main issue(s) with `CampusAppEndToEndTest`. How would you fix them?
3. Briefly describe the Pros and Cons of the strategy applied in `CampusAppEndToEndTest`, assuming that the issue or issues described in the previous question are fixed.
4. Briefly describe the Pros and Cons of the strategy applied in `CampusAppMockedTest`.
5. Briefly describe the Pros and Cons of the strategy applied in `CampusAppTest`.
6. For each one of the following patterns, enumerate the test classes where it is applied.
    - AAA with state
    - Isolated production-like external systems
    - Production-ready in-memory test doubles
7. What is an example of a production-ready in-memory test fake in this project? Why do we say it is production-ready?
8. Could a buggy test fake make `CampusAppTest` pass even if CampusApp had bugs? If so, what can be done?
9. Remove the `@Disabled` annotation from `UsersRepositoryTest::testCreateUserFailsIfGroupDoesNotExist` and run the tests. What happens? Why? How is this related to the previous question? Note: Add the `@Disabled` annotation again before moving on to the next section.
10. Remove the `@Disabled` annotation from `UsersRepositoryTest::testCreateUser` and run the tests. What happens? Why? What is causing the problem? How could it be fixed? Note: Add the `@Disabled` annotation again before moving on to the next section. 
11. Which methods in UsersRepository are not tested?

## Tasks (optional)

- [ ] Write a test for `CampusApp::createUser` in `CampusAppTest`
- [ ] Write a test for `CampusApp::createUser` in `CampusAppMockedTest`
- [ ] Write a test for `CampusApp::createUser` in `CampusAppEndToEndTest` in a way that solves the issue described in question 1. Pay attention to the strategy and patterns you use.
- [ ] Fix the bug causing `UsersRepositoryTest::testCreateUserFailsIfGroupDoesNotExist` to fail
- [ ] Write tests for the **one of** the untested methods in UsersRepository (those you identified in question 11)