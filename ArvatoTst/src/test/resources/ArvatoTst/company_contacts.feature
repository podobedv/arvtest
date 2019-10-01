Feature: View company contacts
  Candidate would like to see company contact information

   Scenario Template: contact information
	  Given I am on the front page
      When page width <browser_width>
      And I click on Contact Us link
      Then I should see company location
	  And should be able to open the map link
	  And should be able to navigate to company social media

    Scenarios:
      |  browser_width |
      |  950           |
      |  980           |