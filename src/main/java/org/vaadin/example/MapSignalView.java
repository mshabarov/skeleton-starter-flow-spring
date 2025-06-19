package org.vaadin.example;

import java.io.Serializable;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.signals.MapSignal;
import com.vaadin.signals.Signal;
import com.vaadin.signals.SignalFactory;

@Route("map")
@Menu(title = "MapSignal Demo")
public class MapSignalView extends VerticalLayout {
    private static final MapSignal<Person> mapSignal =
            SignalFactory.IN_MEMORY_SHARED.map("map", Person.class);
    private VerticalLayout persons = new VerticalLayout();
    static {
        mapSignal.put("Alice", new Person("Alice", 30));
        mapSignal.put("Bob", new Person("Bob", 25));
        mapSignal.put("Charlie", new Person("Charlie", 35));
    }

    public MapSignalView() {
        add(persons);

        Signal.effect(() -> {
            persons.removeAll();
            mapSignal.value().forEach( (key, value) -> {
                var name = new TextField("Name: ");
                name.setValue(value.value().getName());
                name.addValueChangeListener(event ->
                        value.update(person -> {
                            person.setName(event.getValue());
                            return person;
                        }));
                var age = new NumberField("Age: ");
                age.setValue((double) value.value().getAge());
                age.addValueChangeListener(event ->
                        value.update(person -> {
                            person.setAge(event.getValue().intValue());
                            return person;
                        }));
                var remove = new Button("Remove", click -> mapSignal.remove(key));
                persons.add(new HorizontalLayout(name, age, remove));
            });
        });

        var newPerson = new TextField("Add new person", event -> {
            String name = event.getValue();
            if (!name.isEmpty() && !mapSignal.value().containsKey(name)) {
                mapSignal.put(name, new Person(name, 0));
                event.getSource().setValue("");
            }
        });

        add(newPerson);
    }

    public static class Person implements Serializable {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
