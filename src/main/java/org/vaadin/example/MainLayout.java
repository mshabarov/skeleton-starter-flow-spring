package org.vaadin.example;

import java.util.UUID;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.RouterLayout;

@PreserveOnRefresh(partialMatch = true)
public class MainLayout extends Div implements RouterLayout {

    private String id = UUID.randomUUID().toString();

    public MainLayout() {
        add(new Div("Top level layout"));
        System.out.println(this + ": constructor (top)");
        System.out.println("ID: " + id);
    }

//    @Override
//    public void showRouterLayoutContent(HasElement content) {
//        content.getElement().removeFromTree();
//        RouterLayout.super.showRouterLayoutContent(content);
//    }
}
