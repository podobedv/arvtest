	Feature: REST service tests
	Check https://reqres.in/ REST service for GET, CREATE, UPDATE, DELETE

	Scenario Template: create
	Given I get user by id <id> I get back user data

	Scenarios:
      |  id |
      |  1  |
      |  5  |
      |  25 |

	Scenario Template: create
	Given I create user with "<name>" and "<job>" I get back user data I created

	Scenarios:
      |  name  |  job       |
      |  Jack  |  developer |
      |  Peter |  tester    |

	Scenario Template: update
	Given I update user by <id> with "<job>" I get back user data I updated

	Scenarios:
      |  id |  job       |
      |  1  |  developer |
      |  5  |  tester    |
	  |  22 |  tester    |

	Scenario Template: delete
	Given I delete user by <id> I get HTTP 204

	 Scenarios:
      |  id |
      |  2  |
      |  3  |
      |  19 |