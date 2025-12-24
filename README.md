# Request Response Model

A Spring Boot library for handling request and response models with encryption and masking capabilities. This library provides annotations and automatic configuration to simplify data transformation, field exclusion, encryption, and masking in your Spring Boot applications.

## Features

- 🎯 **Request Model Transformation**: Automatically transform incoming requests to your model classes
- 📤 **Response Model Transformation**: Transform responses with field exclusion, masking, and encryption
- 🔒 **Data Encryption**: Encrypt sensitive fields in responses
- 🎭 **Data Masking**: Mask sensitive data (e.g., credit cards, SSN) in responses
- ⚙️ **Auto-Configuration**: Zero-configuration setup with Spring Boot auto-configuration
- 🔧 **Flexible Field Control**: Exclude, add, or modify fields dynamically
- 📦 **Spring Boot Starter**: Easy integration with Spring Boot applications

## Requirements

- Java 17 or higher
- Spring Boot 3.2.5 or higher

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.kirandpolawar</groupId>
    <artifactId>request-response-model-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```gradle
dependencies {
    implementation 'io.github.kirandpolawar:request-response-model-spring-boot-starter:1.0.0'
}
```

### Gradle (Kotlin DSL)

Add the following to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.kirandpolawar:request-response-model-spring-boot-starter:1.0.0")
}
```

## Configuration

### Encryption Key Setup

To enable encryption features, configure the encryption key in your `application.yml` or `application.properties`:

**application.yml:**
```yaml
app:
  security:
    encryption:
      key: your-encryption-key-here-min-16-chars
```

**application.properties:**
```properties
app.security.encryption.key=your-encryption-key-here-min-16-chars
```

> **Note**: The encryption key should be at least 16 characters long for AES encryption.

## Usage

### Request Model

Use the `@RequestModel` annotation to automatically transform incoming request data to your model class:

```java
import com.kp.rrm.core.annotation.RequestModel;
import com.kp.rrm.core.annotation.ExtraField;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<User> createUser(
        @RequestModel(model = User.class, 
                     exclude = {"id", "createdAt"},
                     add = {
                         @ExtraField(name = "source", type = String.class)
                     }) 
        User user) {
        // user object is automatically transformed from request
        // excluded fields are removed
        // extra fields are added
        return ResponseEntity.ok(userService.create(user));
    }
}
```

**@RequestModel Parameters:**
- `model`: The target model class
- `exclude`: Array of field names to exclude from the request
- `add`: Array of `@ExtraField` annotations to add additional fields

### Response Model

Use the `@ResponseModel` annotation to transform responses with field exclusion, masking, and encryption:

```java
import com.kp.rrm.core.annotation.ResponseModel;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    @ResponseModel(
        model = User.class,
        exclude = {"password", "internalNotes"},
        mask = {"email", "phone"},
        encrypt = {"ssn", "creditCard"}
    )
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        // Response will automatically:
        // - Exclude password and internalNotes
        // - Mask email and phone (e.g., u***@example.com)
        // - Encrypt ssn and creditCard
        return ResponseEntity.ok(user);
    }
}
```

**@ResponseModel Parameters:**
- `model`: The target model class
- `responseType`: Optional response wrapper type (default: Object.class)
- `exclude`: Array of field names to exclude from the response
- `mask`: Array of field names to mask in the response
- `encrypt`: Array of field names to encrypt in the response

### Example Model Class

```java
public class User {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String ssn;
    private String creditCard;
    private String internalNotes;
    
    // Getters and setters
    // ...
}
```

### Complete Example

```java
package com.example.controller;

import com.kp.rrm.core.annotation.RequestModel;
import com.kp.rrm.core.annotation.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    @ResponseModel(
        model = User.class,
        exclude = {"password"},
        mask = {"email"}
    )
    public ResponseEntity<User> createUser(
        @RequestModel(model = User.class, exclude = {"id"}) 
        User user) {
        // Process user creation
        User created = userService.save(user);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    @ResponseModel(
        model = User.class,
        exclude = {"password", "internalNotes"},
        mask = {"email", "phone"},
        encrypt = {"ssn"}
    )
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @ResponseModel(
        model = User.class,
        exclude = {"password", "ssn", "creditCard", "internalNotes"},
        mask = {"email"}
    )
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
```

## How It Works

1. **Request Processing**: The `@RequestModel` annotation uses AOP to intercept method parameters and transform the incoming request data to your model class, applying exclusions and adding extra fields.

2. **Response Processing**: The `@ResponseModel` annotation uses both AOP and `ResponseBodyAdvice` to intercept method responses, transform the data, apply field exclusions, masking, and encryption before sending to the client.

3. **Auto-Configuration**: The library automatically configures all necessary beans when included as a dependency. No manual configuration required!

## Module Structure

- **request-response-model-core**: Core annotations (`@RequestModel`, `@ResponseModel`, `@ExtraField`)
- **request-response-model-autoconfigure**: Spring Boot auto-configuration and implementation
- **request-response-model-spring-boot-starter**: Starter dependency (includes both core and autoconfigure)

## Security Considerations

- **Encryption Key**: Store your encryption key securely. Consider using environment variables or a secrets management system in production.
- **Key Length**: The encryption key must be at least 16 characters for AES encryption.
- **Sensitive Data**: Always encrypt or mask sensitive data like SSN, credit cards, passwords, etc.

## Troubleshooting

### Encryption Not Working

Ensure you have configured the encryption key:
```yaml
app:
  security:
    encryption:
      key: your-encryption-key-here
```

### Fields Not Being Excluded/Masked

- Verify the field names in the annotation match your model class field names (case-sensitive)
- Ensure your model class has proper getters/setters
- Check that the library is properly auto-configured (check application logs)

### Auto-Configuration Not Loading

- Verify the dependency is correctly added to your `pom.xml` or `build.gradle`
- Ensure you're using Spring Boot 3.2.5 or higher
- Check that `@SpringBootApplication` is present on your main class

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Author

**Kiran Polawar**

- Email: kirand.d.polawar@gmail.com
- GitHub: [@kirandpolawar](https://github.com/kirandpolawar)

## Links

- **GitHub Repository**: [https://github.com/kirandpolawar/request-response-model](https://github.com/kirandpolawar/request-response-model)
- **Issues**: [https://github.com/kirandpolawar/request-response-model/issues](https://github.com/kirandpolawar/request-response-model/issues)

## Version History

- **1.0.0** - Initial release
  - Request model transformation
  - Response model transformation with exclusion, masking, and encryption
  - Spring Boot auto-configuration
  - Spring Boot starter

---

**Note**: This library is published to Maven Central. Once published, you can use it directly without any additional repository configuration.

