package org.vaadin.example;

import java.util.UUID;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLayout;

@ParentLayout(MainLayout.class)
public class MiddleLayout extends Div implements RouterLayout {

    private String id = UUID.randomUUID().toString();

    public MiddleLayout() {
        add(new Div("Middle level layout"));
        System.out.println(this + ": constructor (middle)");
        System.out.println("ID: " + id);
    }
}
