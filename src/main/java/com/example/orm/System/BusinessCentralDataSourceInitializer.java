package com.example.orm.System;

import com.example.orm.Annotation.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Configuration
public class BusinessCentralDataSourceInitializer extends SqlDataSourceScriptDatabaseInitializer {

    @Autowired
    private ResourceLoader resourceLoader;

    public BusinessCentralDataSourceInitializer(DataSource dataSource, SqlInitializationProperties properties) {
        super(dataSource, properties);
    }


    @Override
    protected void customize(ResourceDatabasePopulator populator) {
        List<Resource> resources = new ArrayList<>();
        String resourcePath = "";

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BusinessCentralConfig.class);
        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Table.class).values();

        try {
            resourcePath = resourceLoader.getResource("classpath:/").getURI().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Object bean : beans) {
            String tableName = bean.getClass().getSimpleName();
            List<String> fields = new ArrayList<>();

            for (Field declaredField : bean.getClass().getDeclaredFields()) {
                fields.add(declaredField.getName() + " " + "VARCHAR(255)," + System.lineSeparator());
            }

            File file = new File(resourcePath + "sql/" + tableName + ".sql");
            try {
                boolean newFile = file.createNewFile();
                if (file.exists()) {
                    FileWriter fileWriter = new FileWriter(file);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder
                            .append("DROP TABLE IF EXISTS Customer;")
                            .append(System.lineSeparator())
                            .append("CREATE TABLE IF NOT EXISTS Customer (")
                            .append(System.lineSeparator());

                    for (String field : fields) {
                        stringBuilder.append(field);
                    }
                    System.out.println(stringBuilder);
                    int lastIndexOf = stringBuilder.lastIndexOf(",");
                    stringBuilder.deleteCharAt(lastIndexOf);
                    System.out.println(stringBuilder);
                    stringBuilder
                            .append(System.lineSeparator())
                            .append(");");
                    fileWriter.write(stringBuilder.toString());
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Resource resource = new FileSystemResource(file);
            populator.addScript(resource);

        }
    }
}
