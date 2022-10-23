package com.example.jonakipust;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonakipust.Database.FirebaseHelper;
import com.example.jonakipust.Database.MainDBHelper;
import com.example.jonakipust.Fragments.Nav_Fragment.HistoryFragment;
import com.example.jonakipust.Fragments.Nav_Fragment.UserFragment;
import com.example.jonakipust.Model.DonationHistory.DonationHistoryModel;
import com.example.jonakipust.Model.Post.PostModel;
import com.example.jonakipust.Model.UserModel;
import com.example.jonakipust.Model.UserSelf;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateHistoryActivity extends AppCompatActivity {
    EditText donerid,patentInfo,security;
    TextView helpMessage;
    Button createBTN;
    ProgressDialog progressDialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_history);

        donerid = findViewById(R.id.et_history_donar_id);
        patentInfo = findViewById(R.id.et_history_patent_info);
        security = findViewById(R.id.et_history_security_code);
        helpMessage = findViewById(R.id.tv_history_help_message);
        createBTN = findViewById(R.id.btn_history_create);
        FirebaseHelper.getSecurityCode("Donation");
        MainDBHelper dbHelper = new MainDBHelper(CreateHistoryActivity.this);

        createBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(FirebaseHelper.isConnected(view.getContext())) {
                    String Id = donerid.getText().toString();
                    String info = patentInfo.getText().toString();
                    String secq = "D"+security.getText().toString();

                    UserModel user = dbHelper.getUserByStudentId(Integer.parseInt(Id));
                    if(user == null){
                        helpMessage.setText("Student id not found.");
                    }else if (info.equals("")) {
                        helpMessage.setText("Write something about patent.");
                    }else if(FirebaseHelper.securityCode == null){
                        helpMessage.setText("Wait few moment for download code.");
                    }else if(!secq.equals(FirebaseHelper.securityCode)){
                        helpMessage.setText("put right security from jonaki admin.");
                    }else {
                        FirebaseHelper.securityCode = null;
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                        DonationHistoryModel model = new DonationHistoryModel(String.valueOf(Long.MAX_VALUE - new
                                Date().getTime()),
                                user.getUid(),dateTimeFormatter.format(LocalDateTime.now()),
                                info+"\n\n Added by : "+UserSelf.getUserSelf().getUserModel().getName());
                        boolean success = dbHelper.insertDonationHistory(model,false);
                        if (success) {
                            user.setLastDonationDate(model.getDonationDate().split(" ")[0]);
                            dbHelper.insertUser(user,false);

                            progressDialog = new ProgressDialog(CreateHistoryActivity.this);
                            progressDialog.setTitle("Donation Creating.");
                            progressDialog.setMessage("Wait few moment to add your Donation history");
                            progressDialog.show();
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    //your code here
                                    Toast.makeText(CreateHistoryActivity.this, "Donation History create success.",
                                            Toast.LENGTH_SHORT).show();
                                    CreateHistoryActivity.this.finish();
                                    progressDialog.dismiss();
                                }
                            }, 5000);


                        } else {
                            Toast.makeText(view.getContext(), "Donation History insert fail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    helpMessage.setText("First connect to the internet.");
                }
            }
        });
    }
}