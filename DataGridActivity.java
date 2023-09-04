package com.mod5.projecttwo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataGridActivity extends AppCompatActivity implements DataAdapter.OnEditClickListener{
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private Button addButton;
    private Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);

        dbHelper = new Database(this);

        recyclerView = findViewById(R.id.data_grid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        dataAdapter = new DataAdapter(dbHelper, this);
        recyclerView.setAdapter(dataAdapter);

        addButton = findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataGridActivity.this, AddDataActivity.class);
                startActivity(intent);
            }
        });

        loadData();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
    }

    // loadData is a method that loads the data from the database to display on the UI
    private void loadData(){
        List<DataItem> dataItems = dbHelper.getAllDataItems();
        dataAdapter.setDataItems(dataItems);

        // checking if the inventroy is low and sending an SMS alert if inventory is at quantity of 1
        for(DataItem dataItem : dataItems){
            if(dataItem.getInventory() == 1){
                String message = "Low inventory alert: " + dataItem.getLabel1();
                sendSMSAlert(message);
            }
        }
    }

    // creating a method to check SMS permissions and send the SMS alert in DataGridActivity.java
    private void sendSMSAlert(String message){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            // sending the SMS message
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("phoneNumber", null, message, null, null);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    // adding the onEditClick method.
    @Override
    public void onEditClick(int position){
        DataItem dataItem = dataAdapter.getDataItems().get(position);
        Intent intent = new Intent(DataGridActivity.this, AddDataActivity.class);
        intent.putExtra("update", true);
        intent.putExtra("dataItem", dataItem);
        startActivity(intent);
    }

}
