package com.morgan.make_kots_great_again;

import java.util.Objects;

public class Product implements Comparable<Product> {

    private int product_code;
    private String product_name;
    private String product_brand;
    private String product_owner;
    private int product_quantity;
    private String product_uid; // Universal Unique IDentifier
    private String product_note;

    public Product(int code, String name, String brand, String owner, int quantity, String uid, String note){
        this.product_code = code;
        this.product_name = name;
        this.product_brand = brand;

        if (owner.contains("group")) {
            this.product_owner = "GROUPE";
        }
        else {
            this.product_owner = owner;
        }

        this.product_quantity = quantity;
        this.product_uid = uid;
        this.product_note = note;
    }

    // Used with API request search for product pattern (when adding a new product)
    public Product(int code, String name) {
        this.product_code = code;
        this.product_name = name;
    }

    public int getProduct_code() {
        return product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public String getProduct_owner() {
        return product_owner;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public String getProduct_uid() {
        return product_uid;
    }

    public String getProduct_note() {
        return product_note;
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

    public boolean can_modify_product_quantity (){
        if (this.product_owner.equals("GROUPE") || this.product_owner.equals("Moi")){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Product o) {
        return product_owner.compareTo(o.getProduct_owner());
    }
}
