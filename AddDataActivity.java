package com.mod5.projecttwo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDataActivity extends AppCompatActivity {
    private EditText inputLabel1, inputLabel2;
    private Button submitButton;
    private boolean isUpdate = false;
    private DataItem dataItem;
    private Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data_form);

        // intializing the database helper
        dbHelper = new Database(this);

        // find views by ID
        inputLabel1 = findViewById(R.id.input_label1);
        inputLabel2 = findViewById(R.id.input_label2);
        submitButton = findViewById(R.id.submit_button);

        // checking if the activity is called for updating and existing items
        isUpdate = getIntent().getBooleanExtra("update", false);
        if(isUpdate){
            dataItem = (DataItem) getIntent().getSerializableExtra("dataItem");
            if(dataItem != null){
                // populating input fields
                inputLabel1.setText(dataItem.getLabel1());
                inputLabel2.setText(dataItem.getLabel2());
            }
        }

        // created an onClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String label1 = inputLabel1.getText().toString().trim();
                String label2 = inputLabel2.getText().toString().trim();

                // checking if the input fields are empty
                if(label1.isEmpty() || label2.isEmpty()){
                    Toast.makeText(AddDataActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }else{
                    if(isUpdate){
                        // updating the data item in the database if its updated
                        if(dataItem != null){
                            dataItem.setLabel1(label1);
                            dataItem.setLabel2(label2);
                            dbHelper.updateDataItem(dataItem);
                        }
                    }else{
                        // adding new item to the database if its not an update
                        dbHelper.addDataItem(label1, label2);
                    }
                    // finish the activity and lets us return to the previous screen
                    finish();
                }
            }
        });
    }
}
