package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.Brand;
import com.kmtech.bitix_clone.model.Equipment;
import com.kmtech.bitix_clone.model.Model;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route(value = "add-equipment", layout = MainLayout.class)
@PageTitle("Добавить оборудование")
public class AddEquipmentView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ComboBox<Brand> brandComboBox;
    private final ComboBox<Model> modelComboBox;

    public AddEquipmentView() {
        add(new H2("Добавить оборудование"));

        // Поле для названия оборудования
//        TextField nameField = new TextField("Название оборудования");
//        nameField.setRequired(true);

        // Выпадающий список для брендов
        brandComboBox = new ComboBox<>("Бренд");
        brandComboBox.setItemLabelGenerator(Brand::getName);
        brandComboBox.setRequired(true);
        loadBrands();

        // Кнопка для добавления нового бренда
        Button addBrandButton = new Button("Добавить новый бренд", event -> openAddBrandDialog());

        // Выпадающий список для моделей
        modelComboBox = new ComboBox<>("Модель");
        modelComboBox.setItemLabelGenerator(Model::getName);
        modelComboBox.setRequired(true);
        modelComboBox.setEnabled(false); // Отключено, пока не выбран бренд

        // Кнопка для добавления новой модели
        Button addModelButton = new Button("Добавить новую модель", event -> openAddModelDialog());
        addModelButton.setEnabled(false); // Отключено, пока не выбран бренд

        // При выборе бренда загружаем модели
        brandComboBox.addValueChangeListener(event -> {
            Brand selectedBrand = event.getValue();
            if (selectedBrand != null) {
                loadModels(selectedBrand.getId());
                modelComboBox.setEnabled(true);
                addModelButton.setEnabled(true);
            } else {
                modelComboBox.clear();
                modelComboBox.setEnabled(false);
                addModelButton.setEnabled(false);
            }
        });

        // Кнопка для сохранения оборудования
        Button saveButton = new Button("Сохранить", event -> {
            if (brandComboBox.isEmpty() || modelComboBox.isEmpty()) {
                Notification.show("Заполните все поля!");
                return;
            }

            Equipment equipment = new Equipment();

            equipment.setBrand(String.valueOf(brandComboBox.getValue()));
            equipment.setModel(String.valueOf(modelComboBox.getValue()));

            try {
                Equipment savedEquipment = restTemplate.postForObject(
                        "http://localhost:8080/api/equipment", equipment, Equipment.class);
                Notification.show("Оборудование успешно добавлено: " + savedEquipment.getModel());
//                nameField.clear();
                brandComboBox.clear();
                modelComboBox.clear();
            } catch (Exception e) {
                Notification.show("Ошибка при сохранении оборудования: " + e.getMessage());
            }
        });

        add(brandComboBox, addBrandButton, modelComboBox, addModelButton, saveButton);
    }

    private void loadBrands() {
        try {
            Brand[] brands = restTemplate.getForObject("http://localhost:8080/api/brands", Brand[].class);
            if (brands != null) {
                brandComboBox.setItems(Arrays.asList(brands));
            }
        } catch (Exception e) {
            Notification.show("Ошибка при загрузке брендов: " + e.getMessage());
        }
    }

    private void loadModels(Long brandId) {
        try {
            Model[] models = restTemplate.getForObject(
                    "http://localhost:8080/api/models/by-brand/" + brandId, Model[].class);
            if (models != null) {
                modelComboBox.setItems(Arrays.asList(models));
            }
        } catch (Exception e) {
            Notification.show("Ошибка при загрузке моделей: " + e.getMessage());
        }
    }

    private void openAddBrandDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Добавить новый бренд");

        TextField brandNameField = new TextField("Название бренда");
        brandNameField.setRequired(true);

        Button saveButton = new Button("Сохранить", event -> {
            if (brandNameField.isEmpty()) {
                Notification.show("Введите название бренда!");
                return;
            }

            Brand brand = new Brand();
            brand.setName(brandNameField.getValue());

            try {
                Brand savedBrand = restTemplate.postForObject(
                        "http://localhost:8080/api/brands", brand, Brand.class);
                Notification.show("Бренд успешно добавлен: " + savedBrand.getName());
                loadBrands();
                dialog.close();
            } catch (Exception e) {
                Notification.show("Ошибка при добавлении бренда: " + e.getMessage());
            }
        });

        Button cancelButton = new Button("Отмена", event -> dialog.close());

        dialog.add(brandNameField);
        dialog.getFooter().add(cancelButton, saveButton);
        dialog.open();
    }

    private void openAddModelDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Добавить новую модель");

        TextField modelNameField = new TextField("Название модели");
        modelNameField.setRequired(true);

        Button saveButton = new Button("Сохранить", event -> {
            if (modelNameField.isEmpty()) {
                Notification.show("Введите название модели!");
                return;
            }

            Model model = new Model();
            model.setName(modelNameField.getValue());
            model.setBrand(brandComboBox.getValue());

            try {
                Model savedModel = restTemplate.postForObject(
                        "http://localhost:8080/api/models", model, Model.class);
                Notification.show("Модель успешно добавлена: " + savedModel.getName());
                loadModels(brandComboBox.getValue().getId());
                dialog.close();
            } catch (Exception e) {
                Notification.show("Ошибка при добавлении модели: " + e.getMessage());
            }
        });

        Button cancelButton = new Button("Отмена", event -> dialog.close());

        dialog.add(modelNameField);
        dialog.getFooter().add(cancelButton, saveButton);
        dialog.open();
    }
}