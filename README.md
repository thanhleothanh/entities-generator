
# Entity-generator

This maven plugin helps generating @Entity java files to:
- Your chosen source folder (if you want to add code/customize to the java files afterward)
- Or generate java files + java class files on the fly when compile and build the project (if you don't want to manually manage the java files)


## Installations

Install the plugin locally with

```bash
  mvn clean install
```

Then in the project that you want to use this plugin to generate code, add the plugin actifact to the build step
```bash
   <build>
    <plugins>
      <plugin>
        <groupId>org.example</groupId>
        <artifactId>maven-entities-generator</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
          <jdbcUrl>${jdbcUrl}</jdbcUrl>
          <jdbcUser>${jdbcUser}</jdbcUser>
          <jdbcPassword>${jdbcPassword}</jdbcPassword>
        </configuration>
        <executions>
          <execution>
            <phase>generate-sources</phase>
            <goals>
              <goal>generate-entities</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

Or, if you don't want to manage the dependencies from m2 repository, you could put the built `.jar` of the plugin directly in the project (let's say in the `./libs` folder), then configure the build step as following
```bash
   <build>
    <plugins>
      <plugin>
        <groupId>org.example</groupId>
        <artifactId>maven-entities-generator</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
          <jdbcUrl>${jdbcUrl}</jdbcUrl>
          <jdbcUser>${jdbcUser}</jdbcUser>
          <jdbcPassword>${jdbcPassword}</jdbcPassword>
        </configuration>
        <executions>
          <execution>
            <phase>generate-sources</phase>
            <goals>
              <goal>generate-entities</goal>
            </goals>
          </execution>
        </executions>
        <dependencies>
          <dependency>
            <groupId>org.example</groupId>
            <artifactId>maven-entities-generator</artifactId>
            <version>1.0-SNAPSHOT</version>
            <systemPath>${basedir}/libs/maven-entities-generator-1.0-SNAPSHOT.jar</systemPath>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>
```
Now your project has this plugin run at compilation phase
## Environment Variables

To run the plugin in the project, you will need to add the following environment variables

| Parameter | Type                    | Description                |
| :-------- |:------------------------| :------------------------- |
| `jdbcUrl` | ***required***`string`  | credentials of the database it reads from  |
| `jdbcUser` | ***required***`string`  | credentials of the database it reads from  |
| `jdbcPassword` | ***required***`string`  | credentials of the database it reads from  |
| `basePackageName` | ***required***`string`  | package name of the generated java files  |
| `outputDirectory` | ***optional***`string`  | where the plugin generate entity java files  |
| `cleanOutputDirectory` | ***optional***`boolean` | whether the plugin clean all content in the `outputDirectory` before generating new files  |
| `overwriteExistingFiles` | ***optional***`boolean` | whether the plugin overwrite entity files that already exist  |
| `skip` | ***optional***`boolean` | skip running the plugin  |


## API Reference

The default behaviour of the plugin, when not configuring any optional parameter is:

- First the plugin generate java class files to the `target/generated-sources/src` folder during the `generate-sources` phase.
- Then after that, during the `compile` phase, the Entity java files there get picked up and compiled to class files available in the `target/classes`
- As a result, all the entity files are available in the compile and runtime and project gets compiled and built successfully
