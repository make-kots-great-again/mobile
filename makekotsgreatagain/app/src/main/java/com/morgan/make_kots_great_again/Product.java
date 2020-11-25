package com.morgan.make_kots_great_again;

public class Product {

    String product_name;
    String product_brand;
    String product_owner;
    String product_quantity;
    String product_uid;

    public Product(String name, String brand, String owner, String quantity, String uid){
        this.product_name = name;
        this.product_brand = brand;
        this.product_owner = owner;
        this.product_quantity = quantity;
        this.product_uid = uid;
    }

    public String getProduct_owner() {
        return product_owner;
    }
}
