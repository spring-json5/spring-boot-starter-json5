# Spring Boot Starter JSON5

![License](https://img.shields.io/badge/license-MIT-greeb.svg) ![Version](https://img.shields.io/maven-central/v/io.github.spring-json5/spring-boot-starter-json5)  

A **Spring Boot Starter** for adding [JSON5](https://json5.org/) support to your Spring Boot project.

✨ No additional dependencies are used in the project. Only spring boot is used as a dependency.

---

## ✨ Features

- **Support JSON5 for REST controllers**: Enables out-of-the-box support of `application/json5` media type.
- **Json5RestTemplate**: Class extended from the original RestTemplate with settings allowed to use JSON5 features.
- **Json5Mapper**: Class extended from the original JsonMapper with settings allowed to use JSON5 features.

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+**
- **Spring Boot 3+**

---

### Installation

Add the starter as a dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.spring-json5</groupId>
    <artifactId>spring-boot-starter-json5</artifactId>
    <version>1.0.0</version>
</dependency>
```

The starter will be activated automatically in your Spring Boot project using Spring Boot's auto-configuration mechanism.

---

### Usage

#### 1. REST Controllers

Since you added the starter to your pom.xml, no need for additional setup.
Provide request header `Accept: appication/json5` to receive response body in JSON5 format.
And in the same manner provide request header `Content-Type: appication/json5` with body in JSON5 format if you want your controller be able to deserialize JSON5 request body to your object in controller.

#### 2. RestTemplate

Use class `Json5RestTemplate` on order to make requests and get responses in JSON5 format.
The class 100% compatible with original `RestTemplate` because it is just extend it.
By default `Json5RestTemplate` able to serialize and deserialize with JSON5, but you should provide `Content-Type: application/json5` header and/or `Accept: application/json5` header if you want to force this behavior.
If you want ot avoid every time manually provide headers, you can use contractor with parameters `new Json5RestTemplate(USE_ONLY_JSON5_CONTENT_TYPE, USE_ONLY_JSON5_ACCEPT);`

#### 3. JsonMapper

Use class `Json5Mapper` on order to serialize/deserialize objects from/to JSON5 format.
The class 100% compatible with original Jackson `JsonMapper` because it is just extending it and construct with a bunch of settings that allow JSON5 format support.
Moreover, you can use static method `Json5Mapper.build()` if you want to get vanilla JsonMapper object that able work with JSON5.

---

## 🧩 Supported JSON5 features

Only embed Jackson features are used for implementing JSON5 format. Not all JSON5 features are supported.
List of supported features below:

### For DeSerialization (JSON5 string to Java objects)

- ALLOW_UNQUOTED_FIELD_NAMES
- ALLOW_TRAILING_COMMA
- ALLOW_SINGLE_QUOTES
- ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER
- ALLOW_NON_NUMERIC_NUMBERS
- ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS
- ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS
- ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS
- ALLOW_JAVA_COMMENT

### For Serialization (Java objects to JSON5 string)

- QUOTE_FIELD_NAMES
- WRITE_NAN_AS_STRING