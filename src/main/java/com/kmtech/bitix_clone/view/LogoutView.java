package com.kmtech.bitix_clone.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("logout")
public class LogoutView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        System.out.println("LogoutView beforeEnter called");
        // Перенаправляем на /login?logout
        UI.getCurrent().getPage().setLocation("/login?logout");
    }
}