package com.kmtech.bitix_clone.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        H1 logo = new H1("Greemdom");
        logo.addClassNames("text-l", "m-m");
        addToNavbar(new DrawerToggle(), logo);

        Tabs tabs = new Tabs(
                createTab("Домой", HomeView.class),
                createTab("Заявки", RequestsView.class),
                createTab("Клиенты", ClientsView.class),
                createTab("Оборудование", EquipmentView.class),
                createTab("Добавить оборудование", AddEquipmentView.class),
                createTab("Добавить клиента", AddClientView.class),
                createTab("Карточка клиента", ClientCardView.class),
                createTab("Создать наряд", CreateRequestView.class),
                createTab("Отправить Email", SendEmailView.class),
                createLogoutTab()
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(new VerticalLayout(tabs));
    }

    private Tab createTab(String title, Class<? extends com.vaadin.flow.component.Component> view) {
        RouterLink link = new RouterLink(title, view);
        return new Tab(link);
    }

    private Tab createLogoutTab() {
        Button logoutButton = new Button("Выйти", event -> {
            System.out.println("Logout button clicked");
            getUI().ifPresent(ui -> {
                ui.getPage().executeJs("localStorage.clear(); sessionStorage.clear();");
                ui.getPage().executeJs(
                        "var form = document.createElement('form');" +
                                "form.method = 'POST';" +
                                "form.action = '/logout';" +
                                "document.body.appendChild(form);" +
                                "form.submit();"
                );
            });
        });
        return new Tab(logoutButton);
    }
}