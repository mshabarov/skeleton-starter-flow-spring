package org.vaadin.example;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.theme.Theme;
import com.vaadin.signals.SignalEnvironment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@PWA(name = "Project Base for Vaadin with Spring", shortName = "Project Base")
@Theme("my-theme")
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public VaadinServiceInitListener signalEnvironmentInit(ObjectMapper objectMapper) {
        return event -> {
            // Should get from the application or e.g. a TaskExecutor bean from Spring
            ExecutorService executor = Executors.newFixedThreadPool(2);
            event.getSource().addServiceDestroyListener(e -> executor.shutdown());

            // Setup for Vaadin in general
            SignalEnvironment.tryInitialize(objectMapper, executor);

            // Setup specific for Flow
            Executor dispatcher = SignalEnvironment.defaultDispatcher();
            SignalEnvironment.addDispatcherOverride(() -> {
                UI owner = UI.getCurrent();
                if (owner == null) {
                    return null;
                }

                return task -> {
                    if (UI.getCurrent() == owner) {
                        task.run();
                    } else {
                        dispatcher.execute(() -> owner.access(task::run));
                    }
                };
            });
        };
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
