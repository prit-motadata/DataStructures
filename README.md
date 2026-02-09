## 🚀 DataStructures – Java DSA Exercises

[![Build Status](https://github.com/prit-motadata/DataStructures/actions/workflows/maven.yml/badge.svg)](https://github.com/prit-motadata/DataStructures/actions/workflows/maven.yml) [![Qodana](https://github.com/prit-motadata/DataStructures/actions/workflows/qodana_code_quality.yml/badge.svg)](https://github.com/prit-motadata/DataStructures/actions/workflows/qodana_code_quality.yml) [![codecov](https://codecov.io/gh/prit-motadata/DataStructures/branch/main/graph/badge.svg)](https://codecov.io/gh/prit-motadata/DataStructures)


## 📖 About
This is a **Maven-based Java project** for practicing **Data Structures and related exercises**.  
It includes:

- ✅ Core data structure implementations (e.g. fixed-size arrays)
- ✅ Exercise-style services (e.g. seat booking with arrays)
- ✅ JUnit-based test cases to validate behavior

---

## 🛠️ Tech Stack
- **Java 21**
- **Maven** (build & dependency management)
- **JUnit Jupiter** (testing)

---

## 📦 Getting Started

From the project root (`DataStructures`):

### 1️⃣ Build & Run Tests
Run all tests:

```bash
mvn test
```

This will compile the project and execute all tests under `src/test/java`.

---

## 📂 Project Structure

```text
src/
 ├── main/java/org/motadata/...
 │    ├── Main.java
 │    ├── datastructures/           # Datastructure implementation
 │    └── exercises/                # Exercise implementation
 └── test/java/org/motadata/...
      └── exercises/                # Test implementation
```

Other files:

- `pom.xml` – Maven project configuration (Java 21, JUnit Jupiter).
- `target/` – Maven build output (generated).
- `.idea/` – IntelliJ IDEA configuration.
- `.gitignore` – Git ignore rules.

---

## 🧪 Example Workflow
- **Edit or add** a data structure / exercise in `src/main/java`.
- **Write or update** tests in `src/test/java`.
- **Run**:

```bash
mvn test
```

to verify everything still passes.

