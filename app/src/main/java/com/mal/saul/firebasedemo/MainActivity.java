package com.mal.saul.firebasedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView helloworld,dialog_language;
    int lang_selected;
    RelativeLayout show_lan_dialog;
    Context context;
    Resources resources;
    Button btnCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getToken();
        registerToFirebaseTopic();
        btnCall = findViewById(R.id.btnCall);



        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowMeet();
            }
        });




        dialog_language = (TextView)findViewById(R.id.dialog_language);
        helloworld =(TextView)findViewById(R.id.helloworld);
        show_lan_dialog = (RelativeLayout)findViewById(R.id.showlangdialog);
        if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("es"))
        {
            context = LocaleHelper.setLocale(MainActivity.this,"es");
            resources =context.getResources();
            dialog_language.setText("ESPAÑOL");
            helloworld.setText("cambiar idioma");
            setTitle(resources.getString(R.string.app_name));
            lang_selected = 0;
        }else if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en")){
            context = LocaleHelper.setLocale(MainActivity.this,"en");
            resources =context.getResources();
            dialog_language.setText("ENGLISH");
            helloworld.setText(resources.getString(R.string.hello_word));
            setTitle(resources.getString(R.string.app_name));
            lang_selected =1;
        }
        else if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("fr")){
            context = LocaleHelper.setLocale(MainActivity.this,"fr");
            resources =context.getResources();
            dialog_language.setText("Fracais");
            helloworld.setText(resources.getString(R.string.hello_word));
            setTitle(resources.getString(R.string.app_name));
            lang_selected =2;
        }
        show_lan_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] Language = {"ESPAÑOL","ENGLISH","FRANCES"};
                final int checkItem;
                Log.d("Clicked","Clicked");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Select a Language")
                        .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog_language.setText(Language[i]);
                                if(Language[i].equals("ESPAÑOL")){
                                    context = LocaleHelper.setLocale(MainActivity.this,"es");
                                    resources =context.getResources();
                                    lang_selected = 0;
                                    helloworld.setText(resources.getString(R.string.hello_word));
                                    setTitle(resources.getString(R.string.app_name));
                                }
                                if(Language[i].equals("ENGLISH"))
                                {
                                    context = LocaleHelper.setLocale(MainActivity.this,"en");
                                    resources =context.getResources();
                                    lang_selected = 1;
                                    helloworld.setText(resources.getString(R.string.hello_word));
                                    setTitle(resources.getString(R.string.app_name));
                                }
                                if(Language[i].equals("FRANCES"))
                                {
                                    context = LocaleHelper.setLocale(MainActivity.this,"fr");
                                    resources =context.getResources();
                                    lang_selected = 2;
                                    helloworld.setText(resources.getString(R.string.hello_word));
                                    setTitle(resources.getString(R.string.app_name));
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                dialogBuilder.create().show();
            }
        });
    }



    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("MainActivity", "error al obtener el token", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
                Log.d("MainActivity", token);
            }
        });
    }
    private void onShowMeet(){
        Intent i = new Intent(getApplicationContext(), MainActivity2.class);
        startActivity(i);
    }
    private void registerToFirebaseTopic() {
        Log.d("MainActivity", "registering: ");
        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("MainActivity", "Subscribed");
                } else
                    Log.d("MainActivity", "Not subscribed" + task.getException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }












}
