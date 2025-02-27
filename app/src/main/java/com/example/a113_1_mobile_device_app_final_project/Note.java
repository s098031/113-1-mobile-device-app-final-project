package com.example.a113_1_mobile_device_app_final_project;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Note extends AppCompatActivity {
    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private SQLiteDatabase dbrw;
    private Button btn_back, btn_tutorial;
    private ListView listView;
    private EditText ed_month,ed_date,ed_thing;
    private OnInputDialogListener inputDialogListener;
    private String  oldMonth,oldDate,oldThing,id_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ed_month = findViewById(R.id.ed_month);
        ed_date = findViewById(R.id.ed_date);
        ed_thing = findViewById(R.id.ed_thing);
        btn_back = findViewById(R.id.btn_back);
        btn_tutorial = findViewById(R.id.btn_tutorial);
        listView=findViewById(R.id.listView);
        dbrw = new MyDBHelper(this).getWritableDatabase();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);
        performQuery();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //回主選單
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Note.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Note.this.startActivity(intent);
                finish();
            }
        });
        // 點擊 ListView 上的項目時執行
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items.get(position);
            String[] parts = selectedItem.split("\t\t\t\t");//遇到\t\t\t\t時跟分割字串
            // 將點擊的項目信息填充到對應的 EditText 中
            ed_month.setText(parts[0].replace("月", ""));
            ed_date.setText(parts[1].replace("日 :", ""));
            ed_thing.setText(parts[2]);
            oldMonth = ed_month.getText().toString();
            oldDate = ed_date.getText().toString();
            oldThing = ed_thing.getText().toString();
            Cursor c= dbrw.rawQuery("SELECT * FROM myTable WHERE month LIKE '" + oldMonth +
                    "' AND date LIKE '"+oldDate+ "' AND thing LIKE '"+oldThing+ "' ORDER BY date", null);
            c.moveToFirst();
            id_item = c.getString(0); //方便修改刪除使用
            c.close();
        });


        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int datecheck = 0;
                if (ed_month.length() < 1 || ed_date.length() < 1 || ed_thing.length() < 1) {
                    showToast("欄位請勿留空");
                } else {
                    switch (Integer.parseInt(ed_month.getText().toString())) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            if (Integer.parseInt(ed_date.getText().toString()) < 1 || Integer.parseInt(ed_date.getText().toString()) > 31) {
                                showToast("請輸入正確月份與日期");
                                datecheck = 0;
                            } else datecheck = 1;
                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            if (Integer.parseInt(ed_date.getText().toString()) < 1 || Integer.parseInt(ed_date.getText().toString()) > 30) {
                                showToast("請輸入正確月份與日期");
                                datecheck = 0;
                            } else datecheck = 1;
                            break;
                        case 2:
                            if (Integer.parseInt(ed_date.getText().toString()) < 1 || Integer.parseInt(ed_date.getText().toString()) > 29) {
                                showToast("請輸入正確月份與日期");
                                datecheck = 0;
                            } else datecheck = 1;
                            break;
                        default:
                            showToast("請輸入正確月份與日期");
                            break;
                    }
                    if (datecheck == 1) {
                        try {
                            dbrw.execSQL("INSERT INTO myTable(month, date,thing) VALUES(?,?,?)",
                                    new String[]{ed_month.getText().toString(), ed_date.getText().toString(), ed_thing.getText().toString()});
                            showToast("新增:" + ed_month.getText() + "月" + ed_date.getText() + "日   事項:" + ed_thing.getText());
                            cleanEditText();
                            performQuery();
                        } catch (Exception e) {
                            showToast("新增失敗:" + e);
                        }
                    }

                }
            }
        });


        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ed_month.length() < 1 || ed_date.length() < 1 || ed_thing.length() < 1) {
                    showToast("欄位請勿留空");
                } else {
                    try {
                        String sql = "UPDATE myTable SET month=? ,date=?,thing = ? WHERE _id LIKE ? AND month LIKE ? AND date LIKE ? AND thing LIKE ?";
                        String[] args = {ed_month.getText().toString(), ed_date.getText().toString(), ed_thing.getText().toString(),id_item, oldMonth,oldDate, oldThing };
                        dbrw.execSQL(sql, args);
                        showToast("已修改成:" + ed_month.getText().toString() + "月" + ed_date.getText().toString() + "日 :" + ed_thing.getText());
                        cleanEditText();
                        performQuery();
                    } catch (Exception e) {
                        showToast("更新失敗:" + e);
                    }
                }

            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_month.length() > 0 && ed_date.length() > 0 && ed_thing.length() > 0) {                              //刪除全部的某種事項
                    dbrw.execSQL("DELETE FROM myTable WHERE _id LIKE '"+id_item+"'");
                    showToast("刪除"+ed_month.getText().toString()+"月"+ed_date.getText().toString()+"日 :"+ed_thing.getText().toString());
                    cleanEditText();
                    performQuery();
                } else if (ed_month.length() < 1 && ed_date.length() < 1 && ed_thing.length() >= 1) {                              //刪除全部的某種事項
                    dbrw.execSQL("DELETE FROM myTable WHERE thing LIKE '%" + ed_thing.getText() + "%'");
                    showToast("刪除事項內容含有「" + ed_thing.getText() + "」的項目");
                    cleanEditText();
                    performQuery();
                } else if (ed_month.length() > 0 && ed_date.length() < 1 && ed_thing.length() >= 1) {                        //刪除某個月的某種類型事項
                    dbrw.execSQL("DELETE FROM myTable WHERE month LIKE '" + ed_month.getText() + "' AND thing LIKE '%" + ed_thing.getText() + "%'");
                    showToast("刪除事項內容含有「" + ed_thing.getText() + "」的項目");
                    cleanEditText();
                    performQuery();
                } else if (ed_month.length() > 0 && ed_date.length() < 1 && ed_thing.length() < 1) {                        //單純刪除某月事項
                    dbrw.execSQL("DELETE FROM myTable WHERE month LIKE '" + ed_month.getText() + "'");
                    showToast("刪除:" + ed_month.getText() + "月   事項內容含有「" + ed_thing.getText() + "」的項目");
                    cleanEditText();
                    performQuery();
                } else if (ed_month.length() < 1 || ed_date.length() < 1) {
                    showToast("月份或日期請勿留空");
                } else {                     //刪除某月某日某類型事項
                    dbrw.execSQL("DELETE FROM myTable WHERE month LIKE '" + ed_month.getText() + "' " +
                            "AND date LIKE '" + ed_date.getText() + "' AND thing LIKE '%" + ed_thing.getText() + "%'");
                    showToast("刪除:" + ed_month.getText() + "月" + ed_date.getText() + "日   事項內容含有「" + ed_thing.getText() + "」的項目");
                    cleanEditText();
                    performQuery();
                }

            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String queryString = (ed_month.length() < 1) ?
                        "SELECT * FROM myTable ORDER BY month ASC " :
                        "SELECT * FROM myTable WHERE month LIKE '" + ed_month.getText() +
                                "' ORDER BY month ASC, date ASC";

                Cursor c = dbrw.rawQuery(queryString, null, null);
                c.moveToFirst();
                items.clear();
                showToast("共有" + c.getCount() + "筆事項");
                for (int i = 0; i < c.getCount(); i++) {
                    items.add(c.getInt(1) + "月\t\t\t\t" + c.getInt(2) + "日 :\t\t\t\t" + c.getString(3));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();

            }
        });


        btn_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog2();
            }
        });
    }

    public interface OnInputDialogListener {
        void onInputReceived(String userInput);
    }

    private OnInputDialogListener onInputDialogListener;


    private void showInputDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("使用說明");
        builder.setMessage("修改項目可以直接點及ListView中的項目進行修改\n刪除功能接支援模糊搜索，不用輸入完整內容就可以刪除資料。\n(謹慎使用，可能刪除相同內容不同月份的資料。)\n查詢功能只有單月與全部月份查詢。\n");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void cleanEditText() {

        ((EditText) findViewById(R.id.ed_month)).setText("");
        ((EditText) findViewById(R.id.ed_date)).setText("");
        ((EditText) findViewById(R.id.ed_thing)).setText("");

    }


    private void performQuery() {
        String queryString;
        if(ed_month.length() > 1 && ed_date.length() >1){
            queryString = "SELECT * FROM myTable WHERE month LIKE '" + ed_month.getText() + "' AND date LIKE '"+ed_date.getText()+"' ORDER BY month ASC, date ASC";
        }else if (ed_month.length() > 1) {
            queryString = "SELECT * FROM myTable WHERE month LIKE '" + ed_month.getText() + "' ORDER BY month ASC, date ASC";
        }else if (ed_date.length() > 1) {
            queryString = "SELECT * FROM myTable WHERE date LIKE '" + ed_date.getText() + "' ORDER BY month ASC, date ASC";
        }else {
            queryString = "SELECT * FROM myTable ORDER BY month ASC ";
        }

        Cursor c = dbrw.rawQuery(queryString,null, null);
        c.moveToFirst();
        items.clear();
        showToast("共有" + c.getCount() + "筆事項");
        for (int i = 0; i < c.getCount(); i++) {
            items.add( c.getInt(1) + "月\t\t\t\t" + c.getInt(2)+"日 :\t\t\t\t"+c.getString(3));
            c.moveToNext();
        }
        adapter.notifyDataSetChanged();
        c.close();
    }

    @Override
    protected void onDestroy() {
        dbrw.close();
        super.onDestroy();
    }


}