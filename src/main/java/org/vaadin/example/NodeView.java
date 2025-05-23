package org.vaadin.example;

import java.time.LocalDate;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.FieldSet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.signals.NodeSignal;
import com.vaadin.signals.Signal;
import com.vaadin.signals.SignalEnvironment;
import com.vaadin.signals.SignalFactory;
import com.vaadin.signals.ValueSignal;
import com.vaadin.signals.operations.InsertOperation;

@Route("node")
@Menu(title = "NodeSignal", icon = "vaadin:plus")
public class NodeView extends VerticalLayout {

    private static final NodeSignal category = SignalFactory.IN_MEMORY_SHARED.node("category");

    private final IntegerField id = new IntegerField("ID");
    private final TextField name = new TextField("Name");
    private final RadioButtonGroup<Type> type = new RadioButtonGroup<>("Type");
    private final DatePicker date = new DatePicker("Created by date");

    static {
        SignalEnvironment.tryInitialize(new ObjectMapper(), Executors.newSingleThreadExecutor());
        category.putChildWithValue("id", 123);
        category.putChildWithValue("name", "Category 1");
        category.putChildWithValue("type", Type.TYPE1);
        category.putChildWithValue("date", LocalDate.now());
        // just examples of how to use the API
        ValueSignal<LocalDate> dateValue = category.asMap(LocalDate.class).value().get("date");
        InsertOperation<NodeSignal> date1 = category.putChildIfAbsent("date");
    }

    public NodeView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        type.setItems(Type.values());

        Signal.effect(() -> {
            id.setValue(category.value().mapChildren().get("id").asValue(Integer.class).value());
            name.setValue(category.value().mapChildren().get("name").asValue(String.class).value());
            type.setValue(category.value().mapChildren().get("type").asValue(Type.class).value());
            date.setValue(category.value().mapChildren().get("date").asValue(LocalDate.class).value());
        });

        id.addValueChangeListener(event -> {
            if (event.isFromClient()) {
                ValueSignal<Integer> idSignal = category.value().mapChildren()
                        .get("id").asValue(Integer.class);
                idSignal.value(event.getValue());
            }
        });

        name.addValueChangeListener(event -> {
            if (event.isFromClient()) {
                ValueSignal<String> nameSignal = category.value().mapChildren()
                        .get("name").asValue(String.class);
                nameSignal.value(event.getValue());
            }
        });

        type.addValueChangeListener(event -> {
            if (event.isFromClient()) {
                ValueSignal<Type> typeSignal = category.value().mapChildren()
                        .get("type").asValue(Type.class);
                typeSignal.value(event.getValue());
            }
        });

        date.addValueChangeListener(event -> {
            if (event.isFromClient()) {
                ValueSignal<LocalDate> dateSignal = category.value().mapChildren().get("date").asValue(LocalDate.class);
                dateSignal.value(event.getValue());
            }
        });

        var form = new FormLayout(id, name, type, date);
        form.setWidthFull();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("500px", 2));

        FieldSet categoryForm = new FieldSet("Category", form);

        add(categoryForm);
    }

    public enum Type {
        TYPE1, TYPE2, TYPE3
    }
}
