package org.vaadin.example;

import com.vaadin.flow.component.ComponentEffect;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.signals.NumberSignal;
import com.vaadin.signals.SignalFactory;

@Route("simple")
@Menu(title = "Simple Demo", icon = "vaadin:plus")
public class SignalDemo extends VerticalLayout {
    // creates a signal instance that can be shared across the application
    private final NumberSignal counter =
            SignalFactory.IN_MEMORY_SHARED.number("counter");

    public SignalDemo() {
        Button button = new Button();
        button.addClickListener(
                // updates the signal value on each button click
                click -> counter.incrementBy(1.0));
        add(button);

        // Effect that resolves the button's text whenever the counter changes
        ComponentEffect.format(button, Button::setText, "Clicked %.0f times", counter);
    }
}
