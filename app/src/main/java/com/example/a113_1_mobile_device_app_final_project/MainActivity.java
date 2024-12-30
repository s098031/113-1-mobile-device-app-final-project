package com.example.a113_1_mobile_device_app_final_project;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {
    private TextView tv_func;
    private Button btn_start;
    private int cont=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_func = findViewById(R.id.tv_func);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).asGif().load(R.drawable.giphy).into(imageView);
        //設定ListView的內容
        String[] messageArray = new String[]{
                "記事本", "電子記帳",
                "債務表", "敬請期待",
                "敬請期待", "敬請期待",
                "敬請期待","","","","","","","沒功能了~不要往下了~~",
                "","","","","","","","","","","","",
                "","你很奇怪耶!"
        };

        ArrayAdapter<String> messageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageArray);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(messageAdapter);


        //設定被選擇的功能在主畫面上
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 使用者點擊了 ListView 中的項目
                String selectedMessage = messageArray[position];
                if(selectedMessage == ""){
                    tv_func.setText("選擇功能:無");
                }else if(selectedMessage =="沒功能了~不要往下了~~"){
                    tv_func.setText("沒功能了~不要往下了~~");
                }else if(selectedMessage =="你很奇怪耶!"){
                    tv_func.setText("你很奇怪耶!");

                    if(cont<=1)Toast.makeText(MainActivity.this, "不要點我了!", Toast.LENGTH_SHORT).show();
                    if(cont >=4 && cont<=5)Toast.makeText(MainActivity.this, "真的不要點了，這裡沒有功能", Toast.LENGTH_SHORT).show();
                    if(cont>=8){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("ㄟ不是，你當這樣可以開啟開發著模式嗎?")
                                .setMessage("由於作者技術不佳，所以沒有發發者模式可以用:P");

                        builder.setPositiveButton("我知道了!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MainActivity.this.startActivity(intent);
                                finish();
                                dialogInterface.dismiss();
                            }
                        });


                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                    cont++;
                }else{
                    tv_func.setText("選擇功能:"+selectedMessage);
                }

            }
        });


        //功能!啟動!!
        btn_start=findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                String str1=tv_func.getText().toString();
                switch (str1){
                    case "選擇功能:記事本":
                         intent = new Intent(MainActivity.this, Note.class);
                        Toast.makeText(MainActivity.this, "Service ! 啟動 !!", Toast.LENGTH_SHORT).show();
                        break;

                    case "選擇功能:電子記帳":
                         intent = new Intent(MainActivity.this, DigitalWallet.class);
                        Toast.makeText(MainActivity.this, "Service ! 啟動 !!", Toast.LENGTH_SHORT).show();
                        break;

                    case "選擇功能:債務表":
                         intent = new Intent(MainActivity.this, ExpensesTracker.class);
                        Toast.makeText(MainActivity.this, "Service ! 啟動 !!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        Toast.makeText(MainActivity.this, "請選擇一項功能", Toast.LENGTH_SHORT).show();
                        break;
                }

                // 啟動 Note Activity 的 Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);

                // 結束 MainActivity
                finish();
            }
        });
    }
}



