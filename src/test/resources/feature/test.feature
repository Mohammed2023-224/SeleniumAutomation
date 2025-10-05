

Feature: feature to test login functionality
@smokeTest
  Scenario Outline: Check login is invalid

    Given user is on login page
    When user enters "<username>" and "<password>"
    And  user clicks on loging page
    Then Error appears that credentials is wrong

    Examples:
    |username|password|
    |user1   |pass1  |
    |user2   | pass2  |