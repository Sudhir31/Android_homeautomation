package com.ltc.firebase_authentication1;

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

public class ModuleActivity extends AppCompatActivity {

    private String selected_module;
    private String selected_room;
    private String location;
    private Bundle bundle;
    private EditText name_channel;
    private Button next_channel;
    private ArrayList<String> channelnames = new ArrayList<String>();
    private ListView channellist;
    private ArrayAdapter<String> arr;
    private TextView display_channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        name_channel = findViewById(R.id.channelname);
        next_channel = findViewById(R.id.nextchannel);
        channellist = findViewById(R.id.channellist);
        display_channel = findViewById(R.id.display_module);

        bundle =getIntent().getExtras();
        selected_room =bundle.getString("module");
        selected_module=bundle.getString("channel");
        display_channel.setText(selected_module);


        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                channelnames);
        channellist.setAdapter(arr);



        Toast.makeText(ModuleActivity.this, selected_room+"/"+selected_module, Toast.LENGTH_SHORT).show();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(selected_room).child(selected_module);

        // Read from the database
        myRef3.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "Hello" ;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                channelnames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    channelnames.add(snapshot.getKey());
                }
                arr.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //take info from database and show it in channelnames array



        next_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String channels = name_channel.getText().toString();

                myRef3.child(channels).setValue("0");

                name_channel.setText("");

                channellist.setAdapter(arr);




                Toast.makeText(ModuleActivity.this, "channel="+channelnames, Toast.LENGTH_SHORT).show();



            }
        });

        channellist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                Toast.makeText(ModuleActivity.this, selected, Toast.LENGTH_SHORT).show();



                //Intent intent = new Intent(RoomActivity.this, RoomActivity.class);
                //intent.putExtra("module", selected);
                //startActivity(intent);

            }
        });
    }
}