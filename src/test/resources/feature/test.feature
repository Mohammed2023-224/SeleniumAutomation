

Feature: feature to test login functionality
@smokeTest
  Scenario Outline: Check login is invalid

    Given user is on login page
    When user enters "<username>" and "<password>"
    And  user clicks on loging button
    Then Assert "<assertion>" login

    Examples:
    |username|password|assertion|
    |user1   |pass1  | fail     |
    |user2   | pass2  |fail     |