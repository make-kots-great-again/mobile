package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequest {

    String token = new String();
    String user = new String();
    String shopping_list_url = "https://kotsapp.herokuapp.com/server/api/shoppingList/";
    String loggin_url = "https://kotsapp.herokuapp.com/server/api/login";
    Activity activity;

    /**
     //--------------
     // CONSTRUCTOR
     //--------------
     * @param activity
     */
    public ApiRequest(Activity activity){
        this.activity = activity;
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        user = pref.getString("username", null);
        token = pref.getString("token", null);
    }

    /**
     //---------------------------------------------------------
     // Function that takes care of sending POST request to api
     //---------------------------------------------------------
     * @param username
     * @param password
     */
    protected void login_post_request(TextView username, TextView password) {

        JSONObject object = new JSONObject();
        try {
            object.put("pseudo",username.getText());
            object.put("password",password.getText());
        } catch (JSONException ignored) {}

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, loggin_url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = response.getJSONObject("user").getString("username");
                            token = response.getJSONObject("user").getString("token");

                            SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("username", user);
                            editor.putString("token", token);
                            editor.commit();

                        } catch (JSONException ignored) { }
                        Login login = new Login();
                        login.launch_page2(activity);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "⚠️ Incorrect username or password.", Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(jsonObjectRequest);
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
    protected void Get_Shopping_Lists_items(final ArrayList<String> items, final ArrayList<String> owner, final ArrayList<String> quantity, final ArrayList<String> uid, final String selected_list, final Activity activity) {

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

                    items.clear();
                    owner.clear();
                    quantity.clear();
                    uid.clear();

                    for(int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        String product_name = object.getString("product_name");
                        String product_owner = object.getString("username");
                        if (product_owner.equals(user)){ product_owner = "Me"; } // US M12
                        String product_quantity = object.getString("quantity");
                        String product_uid = object.getString("shoppingListId");
                        String group_id = object.getString("groupId");

                        items.add(product_name);
                        if (product_owner.equals("group")){
                            owner.add(product_owner.toUpperCase());
                        }
                        else {
                            owner.add(product_owner);
                        }
                        quantity.add(product_quantity);
                        uid.add(product_uid);

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
            listview.setAdapter(new MyCustomAdapter(items, owner, quantity, uid, activity));
        } catch (InterruptedException ignored) { }
    }

    /**
     * ---------------------------------------------------------------------------------------
     *  Method that gets all items of the selected shopping list via an api request
     *  and launch Page3.java by building the listview with special class "MyCustomAdapter2"
     * ---------------------------------------------------------------------------------------
     * @param products
     * @param selected_list
     * @param activity
     */
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
                        if (product_owner.equals(user)){ product_owner = "Me"; }
                        if (product_owner.equals("group")){ product_owner.toUpperCase(); }
                        String product_quantity = object.getString("quantity");
                        String product_uid = object.getString("shoppingListId");
                        String group_id = object.getString("groupId");

                        if (product_owner.equals("group")){
                            products.add(new Product(product_name, product_brand, product_owner.toUpperCase(), product_quantity, product_uid));
                        }
                        else {
                            products.add(new Product(product_name, product_brand, product_owner, product_quantity, product_uid));
                        }
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

}