package com.ltc.firebase_authentication1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private EditText name_room;
    private Button next_room;
    private ArrayList<String> roomnames = new ArrayList<String>();
    private ListView roomlist;
    private ArrayAdapter<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name_room = findViewById(R.id.roomname);
        next_room = findViewById(R.id.nextroom);
        roomlist = findViewById(R.id.roomlist);


        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                roomnames);
        roomlist.setAdapter(arr);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "Hello" ;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                roomnames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    roomnames.add(snapshot.getKey());
                }
                arr.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //take info from database and show it in roomnames array



        next_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rooms = name_room.getText().toString();

                myRef.child(rooms).setValue("0");

                name_room.setText("");

                roomlist.setAdapter(arr);




                Toast.makeText(AddActivity.this, "Rooms="+roomnames, Toast.LENGTH_SHORT).show();



            }
        });

        roomlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                //Toast.makeText(AddActivity.this, selected, Toast.LENGTH_SHORT).show();



                Intent intent = new Intent(AddActivity.this, RoomActivity.class);
                intent.putExtra("module", selected);
                startActivity(intent);

            }
        });

    }

}