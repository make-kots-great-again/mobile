package com.morgan.make_kots_great_again;

import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;

public class Page2Test extends TestCase {



    public void testQuantitiesHaveBeenChanged() {
        ArrayList<Product> prod = new ArrayList<Product>();
        ArrayList<Product> prod_mod = new ArrayList<Product>();

        Page2 page2 = new Page2();
        Product product1 = new Product(123, "product1", "brand1", "group", 1, "123-uid", "note");
        Product product2 = new Product(456, "product2", "brand2", "group", 1, "456-uid", "note");
        Product product3 = new Product(789, "product3", "brand3", "group", 1, "789-uid", "note");

        prod.add(product1);
        prod.add(product2);
        prod.add(product3);

        prod_mod.add(product1);
        prod_mod.add(product2);
        prod_mod.add(product3);

        assertEquals(false, page2.quantitiesHaveBeenChanged(prod, prod_mod));
    }
}