package com.example.orm.System;

import com.example.orm.Annotation.Table;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@ComponentScan(
        value = "com.example.orm.Table",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Table.class)},
        useDefaultFilters = false
)
public class BusinessCentralConfig {
}
