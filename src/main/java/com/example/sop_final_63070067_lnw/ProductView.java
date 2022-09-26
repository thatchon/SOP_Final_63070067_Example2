package com.example.sop_final_63070067_lnw;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Route("")
public class ProductView extends VerticalLayout {
    private ComboBox<Product> p_list;
    private TextField p_name;
    private NumberField p_cost, p_profit, p_price;
    private HorizontalLayout buttonLayout;
    private Button b_add, b_update, b_delete, b_clear;

    public ProductView() {
        p_list = new ComboBox<Product>("Product List");
        p_name = new TextField("Product Name:");
        p_cost = new NumberField("Product Cost:");
        p_profit = new NumberField("Product Profit:");
        p_price = new NumberField("Product Price:");
        buttonLayout = new HorizontalLayout();
        b_add = new Button("Add Product");
        b_update = new Button("Update Product");
        b_delete = new Button("Delete Product");
        b_clear = new Button("Clear Product");

        p_list.setWidth("600px");
        p_name.setWidth("600px");
        p_cost.setWidth("600px");
        p_profit.setWidth("600px");
        p_price.setWidth("600px");

        p_price.setEnabled(false);

        this.clearProduct();

        p_list.addFocusListener(e -> {
            this.fetchProductLists();
        });

        p_list.addValueChangeListener(e -> {
            if (this.p_list.getValue() != null) {
                this.p_name.setValue(this.p_list.getValue().getProductName());
                this.p_cost.setValue(this.p_list.getValue().getProductCost());
                this.p_profit.setValue(this.p_list.getValue().getProductProfit());
                this.p_price.setValue(this.p_list.getValue().getProductPrice());
            } else {
                this.clearProduct();
            }
        });

        p_cost.addKeyPressListener(Key.ENTER, e ->{
            this.calculatePrice();
        });

        p_profit.addKeyPressListener(Key.ENTER, e ->{
            this.calculatePrice();
        });

        b_add.addClickListener(e -> {
            this.calculatePrice();
            String name = this.p_name.getValue();
            double cost = this.p_cost.getValue();
            double profit = this.p_profit.getValue();
            double price = this.p_price.getValue();

            WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/addProduct")
                    .body(Mono.just(new Product(null, name, cost, profit, price)), Product.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            this.fetchProductLists();
            new Notification("Add Succesfully!", 500).open();
        });

        b_update.addClickListener(e -> {
            this.calculatePrice();
            String id = this.p_list.getValue().get_id();
            String name = this.p_name.getValue();
            double cost = this.p_cost.getValue();
            double profit = this.p_profit.getValue();
            double price = this.p_price.getValue();

            WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/updateProduct")
                    .body(Mono.just(new Product(id, name, cost, profit, price)), Product.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            this.fetchProductLists();
            new Notification("Update Successfully!", 500).open();
        });

        b_delete.addClickListener(e -> {
            String id = this.p_list.getValue().get_id();
            String name = this.p_name.getValue();
            double cost = this.p_cost.getValue();
            double profit = this.p_profit.getValue();
            double price = this.p_price.getValue();

            WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/deleteProduct")
                    .body(Mono.just(new Product(id, name, cost, profit, price)), Product.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            this.fetchProductLists();
            this.clearProduct();
        });

        b_clear.addClickListener(e -> {
            this.clearProduct();
            new Notification("Cleared", 500).open();
        });

        buttonLayout.add(b_add, b_update, b_delete, b_clear);
        this.add(p_list, p_name, p_cost, p_profit, p_price, buttonLayout);
    }

    private void fetchProductLists() {
        List<Product> productALl = WebClient
                .create()
                .get()
                .uri("http://localhost:8080/getProducts")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Product>>() {})
                .block();

        this.p_list.setItems(productALl);
        System.out.println(productALl);
    }

    private ComponentEventListener clearProduct() {
        p_name.setValue("");
        p_cost.setValue(0.0);
        p_profit.setValue(0.0);
        p_price.setValue(0.0);
        this.fetchProductLists();
        return null;
    }

    private void calculatePrice() {
        double cost = this.p_cost.getValue();
        double profit = this.p_profit.getValue();

        Double price = WebClient
                .create()
                .get()
                .uri("http://localhost:8080/getPrice/" + cost + "/" + profit)
                .retrieve()
                .bodyToMono(Double.class)
                .block();
        this.p_price.setValue(price);
    }
}