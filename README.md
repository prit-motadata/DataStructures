## DataStructures Project

This project is a Maven-based Java project containing data structure implementations and related exercises.

### Directory structure

- **`pom.xml`**: Maven project configuration (Java 21, JUnit Jupiter for tests).
- **`src/main/java`**: Application and library source code.
  - **`org/motadata/Main.java`**: Main entry point (if used).
  - **`org/motadata/datastructures/array/ArrayFixedSize.java`**: Array-based data structure implementation.
  - **`org/motadata/exercises/array/SeatBookingService.java`**: Example/exercise service using arrays.
- **`src/test/java`**: Test sources.
  - **`org/motadata/exercises/array/SeatBookingServiceTest.java`**: JUnit tests for `SeatBookingService`.
- **`target/`**: Maven build output (generated; can be deleted and recreated).
- **`.idea/`**: IntelliJ IDEA project settings.
- **`.gitignore`**: VCS ignore rules.

### Running tests

From the project root (`DataStructures`), run:

```bash
mvn test
```

This will compile the code (if needed) and execute all tests under `src/test/java`.
