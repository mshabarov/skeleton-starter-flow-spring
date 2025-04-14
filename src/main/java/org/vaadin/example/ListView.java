package org.vaadin.example;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.signals.ListSignal;
import com.vaadin.signals.Signal;
import com.vaadin.signals.SignalFactory;
import com.vaadin.signals.ValueSignal;

@Route("list")
@Menu(title = "ListSignal", icon = "vaadin:plus")
public class ListView extends VerticalLayout {

    public static final ListSignal<Person> persons = SignalFactory.IN_MEMORY_SHARED.list(
            "persons", Person.class);

    static {
        persons.insertLast(new Person("John Doe", 30));
        persons.insertLast(new Person("Jane Doe", 25));
        persons.insertLast(new Person("Sam Smith", 20));
    }

    public ListView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        GridPro<Person> grid = new GridPro<>();
        grid.setWidthFull();
        grid.setEditOnClick(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        Button remove = new Button("Remove selected", click -> {
            for (Person selectedItem : grid.getSelectedItems()) {
                for (ValueSignal<Person> personValueSignal : persons.peek()) {
                    if (selectedItem.equals(personValueSignal.peek())) {
                        persons.remove(personValueSignal);
                    }
                }
            }
        });

        grid.addEditColumn(Person::getName).text(
                ListView::handleNameEdit).setHeader("Name");

        grid.addEditColumn(Person::getAge).text(
                ListView::handleAgeEdit).setHeader("Age");


        Signal.effect(() -> {
            List<Person> list = persons.value().stream().map(Signal::value).toList();
            grid.setItems(list);
        });

        add(grid, remove);
    }

    private static void handleAgeEdit(Person person, String age) {
        ValueSignal<Person> personValueSignal = persons.peek().stream()
                .filter(signal -> signal.peek().equals(person)).findFirst().get();
        personValueSignal.update(p -> new Person(p.getName(), Integer.parseInt(age)));
    }

    private static void handleNameEdit(Person person, String name) {
        ValueSignal<Person> personValueSignal = persons.peek().stream()
                .filter(signal -> signal.peek().equals(person)).findFirst().get();
        personValueSignal.update(p -> new Person(name, p.getAge()));
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

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

}
