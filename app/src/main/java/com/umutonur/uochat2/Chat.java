package com.umutonur.uochat2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by umut on 31.05.2016.
 */

public class Chat extends AppCompatActivity {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String rumuz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        final ListView mesajlar = (ListView) findViewById(R.id.mesajlar);
        final EditText mesaj = (EditText) findViewById(R.id.mesaj);
        final ImageView gonder = (ImageView) findViewById(R.id.mesajGonder);

        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>());

        final DatabaseReference reference = firebaseDatabase.getReference().child("Mesajlar");

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        rumuz  = firebaseUser.getDisplayName();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrayAdapter.add(
                        dataSnapshot.child("Rumuz").getValue().toString() + " : " +
                                dataSnapshot.child("Mesaj").getValue().toString()
                );
                mesajlar.setAdapter(arrayAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Rumuz", rumuz);
                map.put("Mesaj", mesaj.getText().toString());
                reference.push().setValue(map);
                mesaj.setText("");
            }
        });
    }
}
