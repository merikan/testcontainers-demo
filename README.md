#Intro
This is a small Spring Boot application to be used as an example 
when migrating fromJunit 4 to Junit 5

You can find more information in Junit documentation chapter [Migrating from JUnit 4](https://junit.org/junit5/docs/current/user-guide/#migrating-from-junit4)

#Migrate

To migrate from Junit 4 to Junit 5 it takes a lot of repetitive work. So to let the computer 
work instead we use the `sed` command. I have used this approach in several large projects and 
it has helped me a lot.   

The best thing is to take it in small steps and to see that you get the desired 
result. I also have a one-liner at the bottom that you can use if you are in a hurry.

I have run this on macos and where the custom command differs compared to linux. That's 
why I use the -i switch, for example.

### Step by step

1. Update dependencies in `pom.xml`, Exclude `junit-vintage` and include `junit-jupiter`   
    Before: 
    ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
      <exclusions>
        <exclusion>
          <groupId>org.junit.jupiter</groupId>
          <artifactId>junit-jupiter</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.mockito</groupId>
          <artifactId>mockito-junit-jupiter</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    ``` 
   After: 
    ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
      <exclusions>
        <exclusion>
          <groupId>org.junit.vintage</groupId>
          <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    ``` 
1. Make sure we only have Jupiter dependencies and the correct version
    ```
    $ mvn dependency:tree -Dincludes=junit
    $ mvn dependency:tree -Dincludes=org.junit.jupiter
    ```
1. Fix `@Test` annotations   
    Replace `import org.junit.Test` with `import org.junit.jupiter.api.Test`
    ```bash
    $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.Test;/import org.junit.jupiter.api.Test;/' {} \;
    $ git diff --unified=0
    $ git add .
    ```
1. Fix Before and After annotaions   
    `@Before` annotation is renamed to `@BeforeEach`   
    `@After` annotation is renamed to `@AfterEach`   
    `@BeforeClass` annotation is renamed to `@BeforeAll`   
    `@AfterClass` annotation is renamed to `@AfterAll`   
    
    Replace `import org.junit.Before` with `import org.junit.jupiter.api.BeforeEach`   
    Replace `@Before` with `@BeforeEach`   
    Replace `import org.junit.After` with `import org.junit.jupiter.api.AfterEach`   
    Replace `@After` with `@AfterEach`   
    Replace `import org.junit.BeforeClass` with `import org.junit.jupiter.api.BeforeAll`   
    Replace `@BeforeClass` with `@BeforeAll`   
    Replace `import org.junit.After`with `import org.junit.jupiter.api.AfterEach`   
    Replace `@AfterClass` with `@AfterAll`   
     
   ```bash
    $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.Before;/import org.junit.jupiter.api.BeforeEach;/; s/@Before[[:blank:]]*$/@BeforeEach /' {} \;
    $ git diff --unified=0
    $ git add .
    $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.After;/import org.junit.jupiter.api.AfterEach;/; s/@After[[:blank:]]*$/@AfterEach/' {} \;
    $ git diff --unified=0
    $ git add .
    $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.BeforeClass;/import org.junit.jupiter.api.BeforeAll;/; s/@BeforeClass[[:blank:]]*$/@BeforeAll/' {} \;
    $ git diff --unified=0
    $ git add .
    $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.AfterClass;/import org.junit.jupiter.api.AfterAll;/; s/@AfterClass[[:blank:]]*$/@AfterAll/' {} \;
    $ git diff --unified=0
    $ git add .
    ```
1. Fix `@Ignore` annotation   
   `@Ignore` annotation is renamed to `@Disabled`
   
   Replace `import org.junit.Ignore` with `import org.junit.jupiter.api.Disabled`   
   Replace `@Ignore` with `@Disabled`  
   
   ```
   $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.Ignore;/import org.junit.jupiter.api.Disabled;/; s/@Ignore(\(.*\))[[:blank:]]*$/@Disabled\1/' {} \;
   $ git diff --unified=0
   $ git add .
   ```   
1. Fix Spring Extension   
   `@RunWith` is replaced with `@ExtendWith`. We will replace it but in later versions of Spring Boot
   `@SpringBootTest` extends `@ExtendWith(SpringExtension.class)` so it can be removed later. 
   
   Replace `@RunWith(SpringRunner.class)` with `@ExtendWith(SpringExtension.class)`  
   `org.junit.runner.RunWith` with `org.junit.jupiter.api.extension.ExtendWith`   
   `org.springframework.test.context.junit4.SpringRunner` with `import org.springframework.test.context.junit.jupiter.SpringExtension`  
   
   ```
   $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.runner.RunWith;/import org.junit.jupiter.api.extension.ExtendWith;/; s/import org.springframework.test.context.junit4.SpringRunner;/import org.springframework.test.context.junit.jupiter.SpringExtension;/; s/@RunWith\(SpringRunner.class\)[[:blank:]]*$/@ExtendWith(SpringExtension.class)/' {} \;
   $ git diff --unified=0
   $ git add .
   ```

1. Use Junit 5 assertions   
   Replace `import static org.junit.Assert` with `import static org.assertj.core.api.Assertions`  
   
   ```
   $ find . -type f -name '*.java' -exec sed -E -i '' 's/import static org.junit.Assert/import static org.assertj.core.api.Assertions/' {} \;
   $ git diff --unified=0
   $ git add .

   ```
1. Fix wildcard imports   
   Replace `import org.junit.*;` with `import org.junit.jupiter.api.*;`  
   ```
   $ find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit\.\*;/import org.junit.jupiter.api\.\*;/' {} \;
   $ git diff --unified=0
   $ git add .
   ```
1. Fix the rest manually
Our our computer have done the work and it's time to do some manual work. Compile and run all tests and see if you have any errors.
For example, `@Test (expected = …)` no longer exists and you need to use `assertThrows (…)`` 

## A one-liner, "not for the faint-hearted"
If you want to run all steps with one command.
```
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.Test;/import org.junit.jupiter.api.Test;/' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.Before;/import org.junit.jupiter.api.BeforeEach;/; s/@Before[[:blank:]]*$/@BeforeEach /' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.After;/import org.junit.jupiter.api.AfterEach;/; s/@After[[:blank:]]*$/@AfterEach/' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.BeforeClass;/import org.junit.jupiter.api.BeforeAll;/; s/@BeforeClass[[:blank:]]*$/@BeforeAll/' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.AfterClass;/import org.junit.jupiter.api.AfterAll;/; s/@AfterClass[[:blank:]]*$/@AfterAll/' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.Ignore;/import org.junit.jupiter.api.Disabled;/; s/@Ignore(\(.*\))[[:blank:]]*$/@Disabled\1/' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit.runner.RunWith;/import org.junit.jupiter.api.extension.ExtendWith;/; s/import org.springframework.test.context.junit4.SpringRunner;/import org.springframework.test.context.junit.jupiter.SpringExtension;/; s/@RunWith\(SpringRunner.class\)[[:blank:]]*$/@ExtendWith(SpringExtension.class)/' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import static org.junit.Assert/import static org.assertj.core.api.Assertions/' {} \; && \
find . -type f -name '*.java' -exec sed -E -i '' 's/import org.junit\.\*;/import org.junit.jupiter.api\.\*;/' {} \;

``` 






