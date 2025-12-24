request-response-model/
├── pom.xml                           (parent)
├── request-response-model-core/
│   ├── pom.xml
│   └── src/main/java/
│       └── com/company/rrm/core/
│           ├── annotation/
│           │   ├── RequestModel.java
│           │   ├── ResponseModel.java
│           │   └── ExtraField.java
│           └── security/
│               └── DataMasker.java
│
├── request-response-model-autoconfigure/
│   ├── pom.xml
│   ├── src/main/java/
│   │   └── com/company/rrm/autoconfigure/
│   │       ├── aspect/
│   │       │   └── ModelAspect.java
│   │       ├── config/
│   │       │   └── EncryptionProperties.java
│   │       ├── security/
│   │       │   └── DataEncryptor.java
│   │       └── RequestResponseModelAutoConfiguration.java
│   └── src/main/resources/
│       └── META-INF/spring/
│           └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│
└── request-response-model-spring-boot-starter/
└── pom.xml