package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;

@Route(value = "second", layout = MiddleLayout.class)
@RouteScope
@RouteScopeOwner(MainLayout.class)
@SpringComponent
public class SecondView extends VerticalLayout {
    public SecondView() {
        System.out.println(this + ": constructor");
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> System.out.println(e.getWindowName()));
        add(new RouterLink("Navigate to main view - this works correctly", MainView.class));
    }
}
