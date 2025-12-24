# Installation Instructions

## Proper Way to Install the Library

**DO NOT manually copy JAR files!** Use Maven's install command instead.

### Step 1: Build and Install the Library

From the library project root directory:
```bash
cd "C:\Users\kiran\Downloads\request-response-model (1)\request-response-model"
mvn clean install -DskipTests
```

This will:
- Build all modules (core, autoconfigure, spring-boot-starter)
- Install JARs and POMs to: `C:\Users\kiran\.m2\repository\com\kp\`
- Create the correct Maven repository structure

### Step 2: Rebuild Your Consuming Project

From your consuming project:
```bash
cd E:\RequestResponseModelKP
mvn clean install -DskipTests
```

### Why Manual Copy Doesn't Work

When you manually copy JAR files, you're missing:
1. **POM files** - Maven needs these to resolve dependencies
2. **Correct directory structure** - Must be: `groupId/artifactId/version/`
3. **Metadata files** - Maven generates these automatically
4. **Dependency information** - POMs contain transitive dependencies

### Correct Maven Repository Structure

```
C:\Users\kiran\.m2\repository\
└── com\
    └── kp\
        ├── request-response-model-core\
        │   └── 1.0.0\
        │       ├── request-response-model-core-1.0.0.jar
        │       └── request-response-model-core-1.0.0.pom
        ├── request-response-model-autoconfigure\
        │   └── 1.0.0\
        │       ├── request-response-model-autoconfigure-1.0.0.jar
        │       └── request-response-model-autoconfigure-1.0.0.pom
        └── request-response-model-spring-boot-starter\
            └── 1.0.0\
                ├── request-response-model-spring-boot-starter-1.0.0.jar
                └── request-response-model-spring-boot-starter-1.0.0.pom
```

### Quick Build Script

You can also use the provided `build.bat` file:
```bash
cd "C:\Users\kiran\Downloads\request-response-model (1)\request-response-model"
build.bat
```

