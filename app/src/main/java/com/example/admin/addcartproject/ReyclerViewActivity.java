package com.example.admin.addcartproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReyclerViewActivity extends AppCompatActivity {

    DatabaseReference mDatabase, mDatabaseCart;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reycler_view);

        mDatabase = FirebaseDatabase.getInstance().getReference("products");
        mDatabaseCart = FirebaseDatabase.getInstance().getReference().child("Cart");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FireProduct, ViewDataHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FireProduct, ViewDataHolder>(
                FireProduct.class,
                R.layout.recyclerview_item,
                ViewDataHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final ViewDataHolder viewHolder, FireProduct model, int position) {

                final String key = getRef(position).getKey();

                viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());

//                if (viewHolder.quantity <= 100) {

                    mDatabaseCart.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            viewHolder.button_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    viewHolder.quantity = viewHolder.quantity + 1;
                                    dataSnapshot.child(key).child("items").getRef().setValue(viewHolder.quantity);
                                    dataSnapshot.child(key).child("uid").getRef().setValue(key);
                                }
                            });


                            viewHolder.button_minus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    long quant = Long.parseLong(String.valueOf(viewHolder.quantityTextView.getText()));
                                    System.out.println("Zack's Quantity: " + quant);
                                    if (quant >= 1) {
                                        viewHolder.quantity = viewHolder.quantity - 1;
                                        dataSnapshot.child(key).child("items").getRef().setValue(viewHolder.quantity);
                                        dataSnapshot.child(key).child("uid").getRef().setValue(key);
                                    } else {
                            Toast.makeText(getApplicationContext(),"You cannot exceed more than 0 orders.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                            viewHolder.displayQuantity(Long.parseLong(String.valueOf(dataSnapshot.child(key).child("items").getValue())));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                }

//                viewHolder.button_add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if(viewHolder.quantity == 100){
//                            Toast.makeText(getApplicationContext(),"You cannot exceed more than 100 orders.", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        viewHolder.quantity = viewHolder.quantity + 1;
//                        viewHolder.displayQuantity(viewHolder.quantity);
//                    }
//                });
//
//                viewHolder.button_minus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(viewHolder.quantity == 0){
//                            Toast.makeText(getApplicationContext(),"You cannot exceed more than 0 orders.", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        viewHolder.quantity = viewHolder.quantity - 1;
//                        viewHolder.displayQuantity(viewHolder.quantity);
//                    }
//                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ViewDataHolder extends RecyclerView.ViewHolder{

        View mView;

        TextView item_name;
        TextView item_price;
        Button button_add;
        Button button_minus;
        long quantity;
        TextView quantityTextView;
        long value;

        public ViewDataHolder(View itemView) {
            super(itemView);
            mView = itemView;

            quantity = 1;

            item_name = mView.findViewById(R.id.list_item_name);
            button_add = mView.findViewById(R.id.button_add);
            button_minus = mView.findViewById(R.id.button_minus);
            quantityTextView = (TextView) mView.findViewById(R.id.tv_quantity_view);

        }

        private void displayQuantity(long quantity) {
            quantityTextView.setText("" + quantity);
        }

        public void setName(String name){
            item_name.setText(name);
        }

        public void setAdd(String name){
            button_add.setText(name);
        }

        public void setMinus(String name){
            button_minus.setText(name);
        }

        public void setPrice(double price){
            item_price = mView.findViewById(R.id.list_item_price);
            item_price.setText(String.valueOf(price));
        }
    }
}
