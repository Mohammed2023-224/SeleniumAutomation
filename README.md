# Selenium Automation Framework

[![Maven Central](https://img.shields.io/maven-central/v/io.github.mohammed2023-224/selenium-automation-framework.svg)](https://search.maven.org/search?q=g:io.github.mohammed2023-224)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A comprehensive Selenium-based test automation framework with property management, email testing, API testing, and reporting capabilities.

## 📦 Installation

### Maven
Add to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.mohammed2023-224</groupId>
    <artifactId>selenium-automation-framework</artifactId>
    <version>0.1.0-beta.1</version>
</dependency>
```

## 🚀 Quick Start

### 1. Add the dependency to your `pom.xml`

### 2. Create configuration file
Create `src/main/resources/properties/config.properties`:

```properties
app.url=https://example.com
browser=chrome
timeout.seconds=30
local=true
```

### 3. Write your test
```java
import io.github.mohammed2023_224.framework.PropertyReader;
import io.github.mohammed2023_224.framework.SetupDriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class ExampleTest {
    
    @Test
    public void test() {
        // Read configuration
        String url = PropertyReader.get("app.url", String.class);
        String browser = PropertyReader.get("browser", String.class);
        boolean local = PropertyReader.get("local", Boolean.class);
        
        // Get WebDriver
        WebDriver driver = new SetupDriver().startDriver(browser, local);
        driver.get(url);
        
        // Your test logic here
        
        driver.quit();
    }
}
```

## ✨ Features

- ✅ **Smart Property Reader** - Lazy-initialized configuration with environment support
- ✅ **WebDriver Management** - Automatic driver setup for local and remote execution
- ✅ **Thread-Safe** - Built for parallel execution
- ✅ **Email Testing** - Mailosaur integration
- ✅ **API Testing** - RestAssured support
- ✅ **Excel Operations** - Apache POI for reading/writing Excel files
- ✅ **Logging** - Log4j2 integration
- ✅ **Test Data Generation** - JavaFaker for realistic test data
- ✅ **Allure Reporting** - Comprehensive test reports and screenshots

## ⚙️ Configuration

### Property Files Structure
```
src/main/resources/properties/
├── config.properties          # Default configuration
├── development/              # Use with: -Denv=development
├── staging/                  # Use with: -Denv=staging
└── production/               # Use with: -Denv=production
```

### Environment Selection
```bash
# Run tests with specific environment
mvn test -Denv=staging

# Run in headless mode
mvn test -Dheadless=true

# Specify browser
mvn test -Dbrowser=firefox
```

## 📚 Usage Examples

### Reading Configuration
```java
// Basic usage
String url = PropertyReader.get("app.url", String.class);
int timeout = PropertyReader.get("timeout.seconds", Integer.class);
boolean headless = PropertyReader.get("headless", Boolean.class);

// System property override (takes priority)
System.setProperty("app.url", "https://override.example.com");
```

## 🧪 Running Tests

```bash
# Basic test execution
mvn test

# With specific environment
mvn test -Denv=production

# Generate Allure report
mvn test allure:report

# Open report in browser
mvn allure:serve
```

## 📄 License

Apache License 2.0 - See the [LICENSE](LICENSE) file for details.

## 🤝 Support

- **GitHub Repository**: https://github.com/Mohammed2023-224/SeleniumAutomation
- **Report Issues**: https://github.com/Mohammed2023-224/SeleniumAutomation/issues

---

*Built with Java 24
