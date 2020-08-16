package com.example.owen2.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Adapters.Classify_Adapter;
import com.example.owen2.app.Adapters.Product2_Adapter;
import com.example.owen2.app.Adapters.Same_Cate_Product_Adapter;
import com.example.owen2.app.Models.Classify;
import com.example.owen2.app.Models.Product;
import com.example.owen2.app.Ultil.SessionManager;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_Detail extends Fragment {

    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    int Customer_ID=0;
    int Product_ID,ProCat_ID,Price,Available;
    ImageView img_detail_product;
    TextView txt_name,txt_description,txt_price;
    Button btn_detail_addtocard;

    GridView gridView_same_cate_product;
    ArrayList<Product> arrayList_same_cate_product;
    Same_Cate_Product_Adapter same_cate_product_adapter;
    ScrollView scrollView;

    Spinner spinner;
    ArrayList<Classify> arrayList_Size;
    Classify_Adapter classify_adapter;
    String selectSize="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_detail,container,false);

        img_detail_product=view.findViewById(R.id.img_detail_product);
        txt_name=view.findViewById(R.id.txt_detail_name);
        txt_price=view.findViewById(R.id.txt_detail_price);
        btn_detail_addtocard=view.findViewById(R.id.btn_detail_addtocard);
        txt_description=view.findViewById(R.id.txt_detail_description);
        gridView_same_cate_product=view.findViewById(R.id.gridview_same_cate_product);
        scrollView=view.findViewById(R.id.scroll_detail);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.move_bottom_to_top);
        scrollView.startAnimation(animation);

        gridView_same_cate_product = view.findViewById(R.id.gridview_same_cate_product);
        gridView_same_cate_product.setAdapter(same_cate_product_adapter);

        spinner =view.findViewById(R.id.spinner_classify);
        spinner.setAdapter(classify_adapter);

        Get_Infor();
        get_Classify(Product_ID);
        spinner_click();
        Get_Detail(Product_ID);
        GetData_Same_Cate_Product();
        click_Add_To_Card();
        click_Item_Processing();
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList_same_cate_product = new ArrayList<>();
        same_cate_product_adapter = new Same_Cate_Product_Adapter(getContext(),arrayList_same_cate_product);
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("LOGIN",0);
        Customer_ID = sharedPreferences.getInt("CUSTOMER_ID",0);
        arrayList_Size = new ArrayList<>();
        classify_adapter = new Classify_Adapter(getContext(),arrayList_Size);

    }
    private void Get_Infor(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ProCat_ID = bundle.getInt("ProCat_ID", 0);
            Product_ID=bundle.getInt("Product_ID",0);
            Price = bundle.getInt("Price", 0);
            Available=bundle.getInt("Available",0);
            //Toast.makeText(getContext(),"Price: "+Price+"\nAvailable: "+Available,Toast.LENGTH_SHORT).show();
        }

    }
    private void spinner_click(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Classify item;
                item = (Classify) parent.getItemAtPosition(position);
                selectSize=item.getName();
                Toast.makeText(getContext(),selectSize+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Get_Detail(final int intID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        String duongdan = server.Link_get_Detail;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Product_ID=0;
                String Name ="";
                String Image="";
                String Description="";
                int Price=0;
                int ProCat_ID=0;
                int New=0;
                int Top=0;
                int Freeship=0;
                int Available=0;
                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product_ID = jsonObject.getInt("Product_ID");
                            Name=jsonObject.getString("Name");
                            Price=jsonObject.getInt("Price");
                            Image=jsonObject.getString("Image");
                            Description=jsonObject.getString("Description");
                            ProCat_ID=jsonObject.getInt("ProCat_ID");
                            New = jsonObject.getInt("New");
                            Top =jsonObject.getInt("Top");
                            Freeship =jsonObject.getInt("Freeship");
                            Available = jsonObject.getInt("Available");

                            //Toast.makeText(getContext(),Name+"",Toast.LENGTH_LONG).show();

                            txt_name.setText(Name+"");
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            txt_price.setText(decimalFormat.format(Price)+" VND");
                            txt_description.setText(Description);
                            Picasso.with(getContext())
                                    .load(Image)
                                    .placeholder(R.drawable.loader)
                                    .error(R.drawable.photo)
                                    .into(img_detail_product);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    checkConnection.showToast_short(getContext(),"END LIST");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("Product_ID",String.valueOf(intID));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void get_Classify(final int intProductID) {
        arrayList_Size.clear();
        classify_adapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String duongdan = server.Link_get_Classify;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Classify_ID=0;
                String Name="";
                int Display=0;

                if(response!=null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length()==0){
                            arrayList_Size.add(new Classify(1,"One size",1));
                            classify_adapter.notifyDataSetChanged();
                        }
                        else {
                            for (int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Classify_ID= jsonObject.getInt("Classify_ID");
                                Name=jsonObject.getString("Name");
                                Display = jsonObject.getInt("Display");
                                arrayList_Size.add(new Classify(Classify_ID,Name,Display));
                                classify_adapter.notifyDataSetChanged();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("Product_ID",String.valueOf(intProductID));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetData_Same_Cate_Product() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        String duongdan = server.Link_get_Same_Cate_Product;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Product_ID=0;
                String Name ="";
                String Image="";
                String Description="";
                int Price=0;
                int ProCat_ID=0;
                int New=0;
                int Top=0;
                int Freeship=0;
                int Available=0;
                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product_ID = jsonObject.getInt("Product_ID");
                            Name=jsonObject.getString("Name");
                            Price=jsonObject.getInt("Price");
                            Image=jsonObject.getString("Image");
                            Description=jsonObject.getString("Description");
                            ProCat_ID=jsonObject.getInt("ProCat_ID");
                            New = jsonObject.getInt("New");
                            Top =jsonObject.getInt("Top");
                            Freeship =jsonObject.getInt("Freeship");
                            Available = jsonObject.getInt("Available");

                            arrayList_same_cate_product.add(new Product(Product_ID,Name,Image,Description,Price,ProCat_ID,New, Top,Freeship,Available));
                            same_cate_product_adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    checkConnection.showToast_short(getContext(),"END LIST");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("ProCat_ID",String.valueOf(ProCat_ID));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void click_Item_Processing(){
        gridView_same_cate_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                fragment_Detail fragmen_detail = new fragment_Detail();
                Bundle bundle = new Bundle();
                bundle.putInt("Product_ID", arrayList_same_cate_product.get(i).getProduct_ID());
                bundle.putInt("ProCat_ID", arrayList_same_cate_product.get(i).getProCat_ID());
                fragmen_detail.setArguments(bundle);
                pushFragment(fragmen_detail,getContext());
            }
        });
    }

    private void click_Add_To_Card(){
    btn_detail_addtocard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(sessionManager.islogin()){
                click_Add_Processing(Customer_ID);
            }else {
                fragment_Pre_Login fragment_pre_login = new fragment_Pre_Login();
                pushFragment(fragment_pre_login,getContext());
            }

        }
    });


    }

    private void click_Add_Processing(final int intCustomer_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Check_Shopping_Card, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    make_Card(Customer_ID);
                }else {
                    int Order_ID = 0;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Order_ID=jsonObject.getInt("Order_ID");
                        Toast.makeText(getContext(),"Order_ID"+Order_ID,Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
                    }
                    check_Is_Exist(Customer_ID,Product_ID,selectSize,Order_ID);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error"+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();
                param.put("Customer_ID", String.valueOf(intCustomer_ID));
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void make_Card(final int intCustomer_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Make_Shoppingcard, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Toast.makeText(getContext(),"Make card fail",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Make card success",Toast.LENGTH_SHORT).show();
                    click_Add_Processing(Customer_ID);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"register error",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Customer_ID",String.valueOf(intCustomer_ID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void check_Is_Exist(final int intCustomerID, final int intProductID, final String strclassify, final int intOrderID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Check_Is_Exist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Toast.makeText(getContext(),"product is not exist",Toast.LENGTH_SHORT).show();
                    add_To_Card(intOrderID,Product_ID,Price,selectSize);
                }else {
                    int OrderDetail_ID=0;
                    int Quantity = 0;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        OrderDetail_ID=jsonObject.getInt("OrderDetail_ID");
                        Quantity=jsonObject.getInt("Quantity");
                        //Toast.makeText(getContext(),"OrderDetail_ID"+OrderDetail_ID+"\nQuantity: "+Quantity,Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
                    }
                    int n_Quantity=Quantity+1;
                    int n_Price=Price*n_Quantity;

                    update_Card(n_Quantity,n_Price,OrderDetail_ID);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error"+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();
                param.put("Customer_ID", String.valueOf(intCustomerID));
                param.put("Product_ID", String.valueOf(intProductID));
                param.put("Classify", String.valueOf(strclassify));
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void add_To_Card(final int intOrderID, final int intProductID, final int intPrice, final String strClassify){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Add_To_Card, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Toast.makeText(getContext(),"add to card was fail",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"add to card was success",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"register error",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Order_ID",String.valueOf(intOrderID));
                params.put("Product_ID",String.valueOf(intProductID));
                params.put("Price",String.valueOf(intPrice));
                params.put("Classify",strClassify);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void update_Card(final int intQuantity, final int intPrice, final int intOrderDetail_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Update_Shoppingcard, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")){
                    Toast.makeText(getContext(),"Update success",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Update fail",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error"+error,Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("OrderDetail_ID",String.valueOf(intOrderDetail_ID));
                params.put("Quantity",String.valueOf(intQuantity));
                params.put("Price",String.valueOf(intPrice));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.full_screen, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
