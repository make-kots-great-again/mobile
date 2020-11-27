package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiRequest {

    String token;
    String user;
    String current_group_id;
    String shopping_list_url = "https://kotsapp.herokuapp.com/server/api/shoppingList/";

    public ApiRequest(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        user = pref.getString("username", null);
        token = pref.getString("token", null);
    }

    /**
     * ----------------------------------------------------------------------------------------------------
     *  Method that gets all the diffrenent shopping list that the current user possess via an api request
     * ----------------------------------------------------------------------------------------------------
     * @param shopping_list_arraylist
     */
    public void Get_Shopping_Lists(final ArrayList<String> shopping_list_arraylist) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().header("Authorization", token).url(shopping_list_url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String responseBody = response.body().string();
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONObject Jobject2 = Jobject.getJSONObject("shoppingList");

                    Iterator<String> iter = Jobject2.keys();
                    while(iter.hasNext()){
                        String key = iter.next();
                        shopping_list_arraylist.add(key);
                    }
                } catch (JSONException ignored) { }
            }
        });
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------------------
     *  Method that gets all items of the selected shopping list via an api request and add the desired elements in some arraylists
     * ------------------------------------------------------------------------------------------------------------------------------
     * @param items
     * @param owner
     * @param quantity
     * @param uid
     * @param selected_list
     * @param context
     */
    protected void Get_Shopping_Lists_items(ArrayList<Product> products, ArrayList<Product> products_modified, final String selected_list, final Activity activity) {

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().header("Authorization", token).url(shopping_list_url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("GET", "Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String responseBody = response.body().string();
                Log.d("GET", "Ok");
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONObject Jobject2 = Jobject.getJSONObject("shoppingList");
                    JSONArray Jarray = Jobject2.getJSONArray(selected_list);

                    products.clear();
                    products_modified.clear();

                    for(int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);

                        String product_name = object.getString("product_name");
                        String product_brand = object.getString("product_brand");
                        String product_owner = object.getString("username");
                        if (product_owner.equals(user)){ product_owner = "Me"; } // US M12
                        int product_quantity = Integer.parseInt(object.getString("quantity"));
                        String product_uid = object.getString("shoppingListId");
                        String group_id = object.getString("groupId");


                        if (product_owner.equals("group")){
                            products.add(new Product(product_name, product_brand, product_owner.toUpperCase(), product_quantity, product_uid));
                            products_modified.add(new Product(product_name, product_brand, product_owner.toUpperCase(), product_quantity, product_uid));
                        }
                        else {
                            products.add(new Product(product_name, product_brand, product_owner, product_quantity, product_uid));
                            products_modified.add(new Product(product_name, product_brand, product_owner, product_quantity, product_uid));
                        }

                        // Permet de stocker l'ID du groupe dans une "shared preference"
                        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("group_id", group_id);
                        editor.commit();
                    }

                } catch (JSONException ignored) { }
            }
        });
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            ListView listview = (ListView) activity.findViewById(R.id.listview);
            listview.setAdapter(new MyCustomAdapter(products, products_modified, activity));
        } catch (InterruptedException ignored) { }
    }

    protected void Get_items_page3(final ArrayList<Product> products, final String selected_list, final Activity activity) {

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().header("Authorization", token).url(shopping_list_url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("GET2", "Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String responseBody = response.body().string();
                Log.d("GET2", "Ok");
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONObject Jobject2 = Jobject.getJSONObject("shoppingList");
                    JSONArray Jarray = Jobject2.getJSONArray(selected_list);

                    products.clear();

                    for(int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        String product_name = object.getString("product_name");
                        String product_owner = object.getString("username");
                        String product_brand = object.getString("product_brand");
                        if (product_owner.equals(user)){ product_owner = "Me"; } // US M12
                        if (product_owner.equals("group")){ product_owner.toUpperCase(); }
                        int product_quantity = Integer.parseInt(object.getString("quantity"));
                        String product_uid = object.getString("shoppingListId");
                        String group_id = object.getString("groupId");

                        if (product_owner.equals("group")){
                            products.add(new Product(product_name, product_brand, product_owner.toUpperCase(), product_quantity, product_uid));
                        }
                        else {
                            products.add(new Product(product_name, product_brand, product_owner, product_quantity, product_uid));
                        }

                        /*
                        // Permet de stocker l'ID du groupe dans une "shared preference"
                        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("group_id", group_id);
                        editor.commit();*/
                    }

                } catch (JSONException ignored) { }
            }
        });
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            ListView listview2 = activity.findViewById(R.id.listview2);
            listview2.setAdapter(new MyCustomAdapter2(products, activity));
        } catch (InterruptedException ignored) { }
    }

    /**
     * Make an API request to get a list of product that contains the pattern
     *
     * @param db_products
     * @param products_codes
     * @param pattern
     */
    public void getProductsFromPattern(final ArrayList<String> db_products, final Map<String, Integer> products_codes, String pattern)
    {
        String url = "https://kotsapp.herokuapp.com/server/api/products/" + pattern;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().header("Authorization", token).url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONArray Jarray = Jobject.getJSONArray("products");

                    db_products.clear();

                    for(int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        String product_name = object.getString("product_name");
                        String product_code = object.getString("code");

                        db_products.add(product_name);

                        products_codes.put(product_name, Integer.parseInt(product_code));
                    }

                } catch (JSONException ignored) { }
            }
        });
    }

    /**
     * Function called when the user press the "add" button
     * Display the response message when we get it
     *
     * @param activity
     * @param requestBody
     * @param current_group_id
     */
    public void addProductToList(final Activity activity, JSONObject requestBody, String current_group_id)
    {
        JSONObject json = requestBody;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", token)
                .url("https://kotsapp.herokuapp.com/server/api/shoppingList/addProduct/" + current_group_id)
                .post(RequestBody.create(MediaType.parse("application/json"), String.valueOf(json)))
                .build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {/*Nothing*/}

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try
                {
                    String responseBody = response.body().string();

                    final JSONObject Jobject = new JSONObject(responseBody);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                Toast.makeText(activity, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Build and send a API request in order to delete a product
     *
     * The Unique ID of the product allows to know in which shoppingList it's located
     *
     * @param activity : activity calling the popup
     * @param uidProduct : Unique ID of the product that we want to delete
     */
    public void deleteProductRequest(final Activity activity, String uidProduct)
    {
        String url = shopping_list_url + "removeProduct/" + uidProduct;

        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(url)
                .delete()
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {/*Nothing*/}

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String responseBody = response.body().string();

                final JSONObject Jobject;

                try
                {
                    Jobject = new JSONObject(responseBody);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                Toast.makeText(activity, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
