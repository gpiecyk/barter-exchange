package com.gp.barter.exchange.configuration;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

@Plugin(name = "LoggerConfigurationFactory", category = ConfigurationFactory.CATEGORY)
public class LoggerConfigurationFactory extends ConfigurationFactory {

    private static final String PATTERN = "%-5level | %d{yyyy-MM-dd HH:mm:ss.SSS} | %logger{36} | %L | %msg%n";
    private static final String SYSTEM_OUT = "systemOut";
    private static final String SYSTEM_ERR = "systemErr";

    private static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.INFO);

        setConsoleErrAppender(builder);
        setConsoleOutAppender(builder);

        builder.add(builder.newRootLogger(Level.INFO)
                .add(builder.newAppenderRef(SYSTEM_ERR))
                .add(builder.newAppenderRef(SYSTEM_OUT)));
        return builder.build();
    }

    private static void setConsoleErrAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        AppenderComponentBuilder consoleErrAppender = builder.newAppender(SYSTEM_ERR, "CONSOLE").addAttribute("target", ConsoleAppender.Target.SYSTEM_ERR);
        consoleErrAppender.add(builder.newLayout("PatternLayout").addAttribute("pattern", PATTERN));
        consoleErrAppender.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY).addAttribute("level", Level.WARN));
        builder.add(consoleErrAppender);
    }

    private static void setConsoleOutAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        AppenderComponentBuilder consoleOutAppender = builder.newAppender(SYSTEM_OUT, "CONSOLE").addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        consoleOutAppender.add(builder.newLayout("PatternLayout").addAttribute("pattern", PATTERN));
        consoleOutAppender.add(builder.newFilter("ThresholdFilter", Filter.Result.DENY, Filter.Result.ACCEPT).addAttribute("level", Level.WARN));
        builder.add(consoleOutAppender);
    }

    @Override
    public Configuration getConfiguration(ConfigurationSource configurationSource) {
        return getConfiguration(configurationSource.toString(), null);
    }

    @Override
    public Configuration getConfiguration(String name, URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }
}
