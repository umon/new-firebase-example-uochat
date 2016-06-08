package com.umutonur.uochat2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by umut on 31.05.2016.
 */

public class Profil extends AppCompatActivity {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        final EditText ad = (EditText) findViewById(R.id.ad);
        final EditText soyad = (EditText) findViewById(R.id.soyad);
        final EditText rumuz = (EditText) findViewById(R.id.rumuz);
        final Button sohbeteGir = (Button) findViewById(R.id.sohbeteGir);
        final Button logout = (Button) findViewById(R.id.logout);

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(
                firebaseUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            String kullaniciAdi, kullaniciSoyadi, kullaniciRumuzu;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kullaniciAdi = dataSnapshot.child("ad").getValue().toString();
                kullaniciSoyadi = dataSnapshot.child("soyad").getValue().toString();
                kullaniciRumuzu = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                ad.setText(kullaniciAdi);
                soyad.setText(kullaniciSoyadi);
                rumuz.setText(kullaniciRumuzu);

                ad.setEnabled(true);
                soyad.setEnabled(true);
                rumuz.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sohbeteGir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("ad").setValue(ad.getText().toString());
                databaseReference.child("soyad").setValue(soyad.getText().toString());
                firebaseUser.updateProfile(
                        new UserProfileChangeRequest.Builder()
                                .setDisplayName(rumuz.getText().toString()).build());
                startActivity(new Intent(Profil.this, Chat.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
            }
        });
    }
}
