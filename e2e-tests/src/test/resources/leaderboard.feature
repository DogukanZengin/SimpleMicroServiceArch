Feature: Users are listed from highest score to lowest, and when they get points they can  move
  up on the ranking
Scenario: A user sends a higher number of right attempts and it's positioned at the first place
  at the first place in the ranking.
  When the user dogukan sends 2 right attempts
  And the user john sends 1 right attempts
  Then the user dogukan is number 1 on the leaderboard
  And the user john is number 2 on the leaderboard
Scenario: A user passes another one when gets higher score
  Given the user dogukan sends 3 right attempts
  And the user john sends 2 right attempts
  And the user dogukan is number 1 on the leaderboard
  When the user john sends 2 right attempts
  Then the user john is number 1 on the leaderboard
  And the user dogukan is number 2 on the leaderboard