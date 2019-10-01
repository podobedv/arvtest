Feature: View open positions
  Candidate would like to see the list of open vacancies

  Background:
    Given I am on the front page

  Scenario Template: open positions
    When page width <browser_width>
    And I click on "<page>" link
    Then I should see open vacancies on "<page>" page

  Scenarios:
    |  browser_width |  page                    |
    |  950           |  Career                  |
    |  950           |  IT Development Center   |
    |  980           |  Career                  |
    |  980           |  IT Development Center   |
