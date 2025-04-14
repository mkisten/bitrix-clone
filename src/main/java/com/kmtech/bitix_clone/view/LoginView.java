package com.kmtech.bitix_clone.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Вход")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    public LoginView() {
        System.out.println("LoginView constructor called");
        add(new H1("Greemdom"));
        add(new H2("Вход в систему"));
        Anchor loginLink = new Anchor("/oauth2/authorization/keycloak", "Войти через OAuth2");
        loginLink.getElement().setAttribute("router-ignore", true);
        add(loginLink);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        System.out.println("LoginView beforeEnter called");
        if (event.getLocation().getQueryParameters().getParameters().containsKey("logout")) {
            add(new Paragraph("Вы успешно вышли из системы."));
        }
    }
}