# Contributing

## Installation and Setup
This project runs on Spring Boot with Maven, a MySQL backing database. Thymeleaf is also incorporated for HTML files.

At the current moment, you will need to set up your own database for testing your own code. Do this by setting up the appropriate variables in the [dotenv](/src/main/resources/config/dotenv) file, then rename that file to "`.env`".

## Making Changes
Don't make any changes to the main branch. Branch naming conventions aren't enforced, although naming branches with a `username`/`task-name` convention is typical for user edits.

Make sure to view the [style guide](/docs/STYLE_GUIDE.md) for coding standards.

While it is not enforced, follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/#summary) for commits.

## Submitting Pull Requests
Create pull requests against main or larger feature branches, if applicable. Once you're done pushing commits and have added any needed comments, mark the PR as "Ready to Review", otherwise leave it as a draft. 

Allow repository moderators to approve pull requests to the main branch and feature branches.