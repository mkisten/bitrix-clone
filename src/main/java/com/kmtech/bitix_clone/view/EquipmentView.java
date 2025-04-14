package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.Brand;
import com.kmtech.bitix_clone.model.Equipment;
import com.kmtech.bitix_clone.model.Model;
import com.kmtech.bitix_clone.service.BrandService;
import com.kmtech.bitix_clone.service.EquipmentService;
import com.kmtech.bitix_clone.service.ModelService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "equipment", layout = MainLayout.class)
@PageTitle("Оборудование")
public class EquipmentView extends VerticalLayout {

    private final EquipmentService equipmentService;
    private final BrandService brandService;
    private final ModelService modelService;
    private Grid<Equipment> grid = new Grid<>(Equipment.class);
    private TextField searchField = new TextField("Поиск оборудования...");
    private Button searchButton = new Button("Найти", e -> searchEquipment());
    private ComboBox<Brand> brandComboBox = new ComboBox<>("Марка");
    private ComboBox<Model> modelComboBox = new ComboBox<>("Модель");
    private Button addButton = new Button("Добавить", e -> createEquipment());

    @Autowired
    public EquipmentView(EquipmentService equipmentService, BrandService brandService, ModelService modelService) {
        this.equipmentService = equipmentService;
        this.brandService = brandService;
        this.modelService = modelService;

        // Настройка грида
        grid.setColumns("id", "brand", "model");
        grid.addComponentColumn(equipment -> new Button("Удалить", e -> deleteEquipment(equipment)))
                .setHeader("Действия");
        grid.addComponentColumn(equipment -> new Button("История обслуживания", e -> navigateToMaintenance(equipment.getId())))
                .setHeader("История");

        // Форма для поиска
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, searchButton);
        searchLayout.setAlignItems(Alignment.BASELINE);

        // Форма для добавления
        List<Brand> brands = brandService.getAllBrands();
        brandComboBox.setItems(brands);
        brandComboBox.setItemLabelGenerator(Brand::getName);
        brandComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                List<Model> models = modelService.getModelsByBrand(event.getValue().getId());
                modelComboBox.setItems(models);
                modelComboBox.setItemLabelGenerator(Model::getName);
            } else {
                modelComboBox.clear();
            }
        });

        HorizontalLayout addLayout = new HorizontalLayout(brandComboBox, modelComboBox, addButton);
        addLayout.setAlignItems(Alignment.BASELINE);

        add(new H2("Справочник оборудования"), searchLayout, addLayout, grid);
        fetchAllEquipment();
    }

    private void fetchAllEquipment() {
        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        grid.setItems(equipmentList);
    }

    private void searchEquipment() {
        String query = searchField.getValue();
        List<Equipment> equipmentList = equipmentService.searchEquipment(query);
        grid.setItems(equipmentList);
    }

    private void createEquipment() {
        Equipment equipment = new Equipment();
        equipment.setBrand(brandComboBox.getValue().getName());
        equipment.setModel(modelComboBox.getValue().getName());
        equipmentService.createEquipment(equipment);
        fetchAllEquipment();
        brandComboBox.clear();
        modelComboBox.clear();
    }

    private void deleteEquipment(Equipment equipment) {
        equipmentService.deleteEquipment(equipment.getId());
        fetchAllEquipment();
    }

    private void navigateToMaintenance(Long equipmentId) {
        getUI().ifPresent(ui -> ui.navigate("equipment/maintenance?equipmentId=" + equipmentId));
    }
}