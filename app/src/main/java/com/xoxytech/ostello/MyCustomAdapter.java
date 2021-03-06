package com.xoxytech.ostello;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 5/10/17.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {

    final String url = "http://ostallo.com/ostello/fetchownerhostels.php";
    String data = " ", hostel_id, category, vacancy, rate, address, city, type, facilities;
    JSONArray jArray;
    JSONObject json_data;
    int pos, i;
    MyCustomAdapter adapter;
    ListView listView;
    View view;
    String urldelete = "http://ostallo.com/ostello/deleteSpecificHostel.php";
    private ArrayList<String> list;
    private Context context;
    private String number = null;

    public MyCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 1;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        pos = position;
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview, null);
        }



        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));
        listView = (ListView) view.findViewById(R.id.listview);

        //Handle buttons and add onClickListeners
        final ImageView deleteBtn = (ImageView) view.findViewById(R.id.delete_btn);
        final ImageView editBtn = (ImageView) view.findViewById(R.id.edit_btn);

        SharedPreferences sp = context.getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        number = sp.getString("USER_PHONE", null);



        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(context.getApplicationContext(),
                        R.anim.bounce);
                deleteBtn.startAnimation(animation1);


                if (CheckInternet.checkinternet(context.getApplicationContext())) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    alertDialog.setMessage("Are you want to delete hostel?");
                    RequestQueue queue = Volley.newRequestQueue(context);

                    StringRequest getrequest = new StringRequest(Request.Method.GET, url + "?phone=" + number, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                jArray = new JSONArray(response);

                                json_data = jArray.getJSONObject(position);

                                hostel_id = json_data.getString("hostel_id");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                            Intent intent = new Intent(context, ManageHostels.class);

                            context.startActivity(intent);

                        }
                    });
                    queue.add(getrequest);

                    alertDialog.setPositiveButton(
                            "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    RequestQueue queue1 = Volley.newRequestQueue(context);

                                    StringRequest postrequest = new StringRequest(Request.Method.POST, urldelete + "?hostel_id=" + hostel_id.trim(), new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            Log.d("*****", urldelete + "?hostel_id=" + hostel_id.trim());


                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("***error", urldelete + "?hostel_id=" + hostel_id.trim());


                                        }
                                    });
                                    queue1.add(postrequest);
                                    Activity a = (Activity) context;
                                    a.recreate();

                                    dialog.cancel();

                                }
                            });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alertDialog.show();
                } else
                    Toast.makeText(context, "Make sure you have Active Internet Connection", Toast.LENGTH_LONG).show();



            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(context.getApplicationContext(),
                        R.anim.bounce);
                editBtn.startAnimation(animation1);
                if (CheckInternet.checkinternet(context.getApplicationContext())) {


                    RequestQueue queue = Volley.newRequestQueue(context);

                    StringRequest getrequest = new StringRequest(Request.Method.GET, url + "?phone=" + number, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                jArray = new JSONArray(response);

                                json_data = jArray.getJSONObject(position);

                                data = json_data.getString("hostelname") + " | ";
                                data += json_data.getString("category") + " | ";
                                data += json_data.getString("vacancy") + " | ";
                                data += json_data.getString("rate") + " | ";
                                data += json_data.getString("address") + " |";
                                data += json_data.getString("city") + " |";
                                data += json_data.getString("type") + " |";
                                data += json_data.getString("facilities") + " |";
                                data += json_data.getString("hostel_id");
                                hostel_id = json_data.getString("hostel_id");


                                Intent intent = new Intent(context, ManageHostels2.class);
                                intent.putExtra("data", data);
                                context.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());

                        }
                    });
                    queue.add(getrequest);
                    notifyDataSetChanged();
                } else
                    Toast.makeText(context, "Make sure you have Active Internet Connection", Toast.LENGTH_LONG).show();

            }
        });


        return view;
    }

}
