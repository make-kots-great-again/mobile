package com.morgan.make_kots_great_again;

import java.util.Objects;

public class Product {

    String product_name;
    String product_brand;
    String product_owner;
    int product_quantity;
    String product_uid;
    String product_note;

    public Product(String name, String brand, String owner, int quantity, String uid, String note){
        this.product_name = name;
        this.product_brand = brand;
        this.product_owner = owner;
        this.product_quantity = quantity;
        this.product_uid = uid;
        this.product_note = note;
    }

    public String getProduct_owner() {
        return product_owner;
    }

    public void reduce_quantity() { this.product_quantity--; }

    public void add_quantity() { this.product_quantity++; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return product_quantity == product.product_quantity &&
                product_name.equals(product.product_name) &&
                product_brand.equals(product.product_brand) &&
                product_owner.equals(product.product_owner) &&
                product_uid.equals(product.product_uid) &&
                product_note.equals(product_note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_name, product_brand, product_owner, product_quantity, product_uid, product_note);
    }
}
