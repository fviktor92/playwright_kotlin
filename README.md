# Kotlin Test Framework with Playwright and JUnit 5

This repository contains a Kotlin test framework that utilizes Playwright and JUnit 5 for automated testing of web applications. This framework allows you to write and execute UI automated test cases using different web browsers (Chromium, Firefox, or WebKit). To get started, follow the instructions below.

## Prerequisites

Before you can execute the tests, make sure you have the following prerequisites installed:

- Java Development Kit (JDK) 8 or higher
- Maven

## Setup

1. Clone this repository to your local machine:
2. Navigate to the project directory:

   ```bash
   cd playwright_kotlin
   ```

3. Download the project dependencies using Maven:

   ```bash
   mvn clean package
   ```

## Running UI Tests

To execute the automated UI test cases, you need to provide three system properties as mentioned below:

- `browserName`: Choose from "chromium", "firefox", or "webkit".
- `email`: Use this for the UI automated test cases.
- `password`: Use this for the UI automated test cases.

You can pass these properties when running Maven commands or configure them in your IDE's run configuration.

### Command Line

Execute the tests with the following Maven command:

```bash
mvn test -DbrowserName=<browser> -Demail=<your-email> -Dpassword=<your-password>
```

Replace `<browser>` with the browser you want to use (e.g., "chromium", "firefox", or "webkit"). Also, replace `<your-email>` and `<your-password>` with appropriate values for your test environment.

### IDE Configuration

1. Open your IDE (e.g., IntelliJ IDEA or Eclipse).

2. Locate the test class that you want to run.

3. Configure the run configuration by setting the required system properties:
    - `browserName`
    - `email`
    - `password`

4. Run the test class.

## Test Structure

The test cases are organized using JUnit 5 annotations. You can find the test classes in the `src/test/kotlin` directory. Feel free to create new test classes and methods according to your application's requirements.

## Test Reporting

Allure results will appear in allure-results folder. To generate html report and automatically open it in a web browser, run the following command:

```bash
allure serve allure-results
```