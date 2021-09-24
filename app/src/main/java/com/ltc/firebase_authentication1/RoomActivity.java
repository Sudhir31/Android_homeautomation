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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {

    private String selected_room;
    private Bundle bundle;
    private EditText name_module;
    private Button next_module;
    private ArrayList<String> modulenames = new ArrayList<String>();
    private ListView modulelist;
    private ArrayAdapter<String> arr;
    private TextView display_room;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        name_module = findViewById(R.id.modulename);
        next_module = findViewById(R.id.nextmodule);
        modulelist = findViewById(R.id.modulelist);
        display_room = findViewById(R.id.display_room);

        bundle =getIntent().getExtras();
        selected_room=bundle.getString("module");
        display_room.setText(selected_room);


        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                modulenames);
        modulelist.setAdapter(arr);



        Toast.makeText(RoomActivity.this, selected_room, Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(selected_room);

        // Read from the database
        myRef2.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "Hello" ;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                modulenames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    modulenames.add(snapshot.getKey());
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



        next_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rooms = name_module.getText().toString();

                myRef2.child(rooms).setValue("0");

                name_module.setText("");

                modulelist.setAdapter(arr);




                Toast.makeText(RoomActivity.this, "Module="+modulenames, Toast.LENGTH_SHORT).show();



            }
        });

        modulelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                Toast.makeText(RoomActivity.this, selected, Toast.LENGTH_SHORT).show();



                Intent intent = new Intent(RoomActivity.this, ModuleActivity.class);
                intent.putExtra("channel", selected);
                intent.putExtra("module", selected_room);
                startActivity(intent);

            }
        });

    }

}