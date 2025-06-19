package org.vaadin.example;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Objects;

import org.springframework.security.provisioning.UserDetailsManager;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.signals.Signal;
import com.vaadin.signals.SignalFactory;
import com.vaadin.signals.ValueSignal;

@Route("value")
@RouteAlias("")
@Menu(title = "ValueSignal", icon = "vaadin:plus")
public class ValueView extends VerticalLayout {

    private static final ValueSignal<Product> product =
            SignalFactory.IN_MEMORY_SHARED.value("product", Product.class);

    static {
        product.value(new Product("Product #1", 100.0));
    }

    public ValueView() {
//        ValueSignal<Product> productValueSignal = new ValueSignal<>(Product.class);
//        Signal<Product> defaultProduct = productValueSignal.map(product1 -> {
//            if (product1 == null) {
//                return new Product("Default Product", 0.0);
//            }
//            return product1;
//        });
//        ValueSignal<Integer> age = SignalFactory.IN_MEMORY_SHARED.value("age", Integer.class);
//        Signal<String> ageCategory = age.map(a ->
//                a < 18 ? "Child" : (a < 65 ? "Adult" : "Senior"));
//        ageCategory.peekConfirmed()
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        TextField name = new TextField();
        NumberField price = new NumberField();

        var form = new FormLayout(name, price);
        form.setWidthFull();

        Span span = new Span();

        Signal.effect(() -> {
            Product value = product.value();
            name.setValue(value.getName());
            price.setValue(value.getPrice());
            span.setText("Product to submit: " + value);
        });

        name.addValueChangeListener(event -> product.update(
                productValue -> new Product(
                        event.getValue(), productValue.getPrice())));

        price.addValueChangeListener(event -> product.update(
                productValue -> new Product(
                        productValue.getName(), event.getValue())));

        Checkbox discount = new Checkbox("With discount", event ->
                product.update(productValue -> {
                    if (event.getValue()) {
                        return new Product(productValue.getName(),
                                productValue.getPrice() * 0.95);
                    } else {
                        return new Product(productValue.getName(),
                                productValue.getPrice() / 0.95);
                    }
        }));

        add(form, span, discount);
    }

    private static class Product implements Serializable {
        private String name;
        private double price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Product() {
        }

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return Double.compare(price, product.price) == 0 && Objects.equals(name, product.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, price);
        }

        @Override
        public String toString() {
            DecimalFormat decimalFormat = new DecimalFormat("###.##");
            String formatted = decimalFormat.format(price);
            return name + " with price " + formatted + " EUR";
        }
    }
}
