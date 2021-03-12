package yummycherrypie.pl.activities.tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import yummycherrypie.business_logic.Email;
import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.business_logic.OpenFileDialog;
import yummycherrypie.business_logic.Tests;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.default_records.BookingMenDefaultRecords;
import yummycherrypie.dal.default_records.BookingTypesDefaultRecords;
import yummycherrypie.dal.default_records.BookingsDefaultRecords;
import yummycherrypie.dal.default_records.ComponentsDefaultRecords;
import yummycherrypie.dal.default_records.EventsDefaultRecords;
import yummycherrypie.dal.default_records.RecipesDefaultRecords;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.system.R;

/**
 * Created by piskarev on 15.09.2015.
 * Настройки приложения
 */
public class Tools extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, OpenFileDialog.OpenDialogListener {

    public static String errors = "";

    private LinearLayout buttonDeleteAllRecords;
    private LinearLayout buttonRunTests;
    private LinearLayout buttonViewRecords;
    private LinearLayout buttonExportDB;
    private LinearLayout buttonImportDB;

    private Switch switchIsDebug;
    private boolean isDebug = false;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private TextView tvWait;

    LoadDefaultRecords ldr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        buttonDeleteAllRecords = (LinearLayout) findViewById(R.id.buttonSetDBToDefault);
        buttonRunTests  = (LinearLayout) findViewById(R.id.buttonRunTests);
        buttonViewRecords  = (LinearLayout) findViewById(R.id.buttonViewRecords);
        buttonExportDB = (LinearLayout) findViewById(R.id.buttonExportDB);
        buttonImportDB = (LinearLayout) findViewById(R.id.buttonImportDB);
        switchIsDebug = (Switch) findViewById(R.id.switchIsDebug);

        buttonDeleteAllRecords.setOnClickListener(this);
        buttonRunTests.setOnClickListener(this);
        buttonViewRecords.setOnClickListener(this);
        buttonExportDB.setOnClickListener(this);
        buttonImportDB.setOnClickListener(this);

        switchIsDebug.setOnCheckedChangeListener(this);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarWait);
        tvWait = (TextView) findViewById(R.id.messageWait);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSmallWait);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonSetDBToDefault:{
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Удаление базы данных")
                        .setMessage("Вы уверены, что хотите удалить все записи из базы данных? База данных будет заполнена значениями по умолчанию.")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ldr = new LoadDefaultRecords();
                                ldr.execute();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Пуся - криворучка", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).show();

            }
            break;
            case R.id.buttonRunTests:{
                Tests tests = new Tests(this);
            }
            break;
            case R.id.buttonViewRecords:{
                intent = new Intent(this, TableRecords.class);
                startActivity(intent);
            }
            break;
            case R.id.buttonExportDB:{
                new Email(this).SendMessage();
            }
            break;
            case R.id.buttonImportDB:{
                OpenFileDialog ofd = new OpenFileDialog(this);
                ofd.setOpenDialogListener(this);
                ofd.show();
            }
            break;
        }

    }

    public void onOkPressed(){
        try {
            try {
                db.beginTransaction();
                if (DBHelper.dropDataBase(getApplicationContext())) {
                    db.setTransactionSuccessful();
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Не удалось удалить базу данных!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
                db.endTransaction();
                db.close();
            }

            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            new BookingMenDefaultRecords(dbHelper).createDefaultRecords(isDebug);
            new ComponentsDefaultRecords(dbHelper).createDefaultRecords(isDebug);
            new RecipesDefaultRecords(dbHelper).createDefaultRecords(isDebug);
            new BookingTypesDefaultRecords(dbHelper).createDefaultRecords(isDebug);
            new BookingsDefaultRecords(dbHelper).createDefaultRecords(isDebug);
            new EventsDefaultRecords(dbHelper).createDefaultRecords(isDebug);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "База данных заполнена значениями по умолчанию", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception e)
        {
            LogExtension.Error(e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            finish();
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isDebug = isChecked;
        if (isDebug) {
            Toast.makeText(this, "Debug regime is on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Debug regime is off", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnSelectedFile(String fileName) {
        Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
        try {
            dbHelper.importDatabase(fileName);
        } catch (Exception e){
            Toast.makeText(this, "Не удалось считать файл", Toast.LENGTH_SHORT).show();
        }

    }

    class LoadDefaultRecords extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarLayout.setVisibility(View.VISIBLE);
            tvWait.setText("Загрузка данных...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            onOkPressed();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBarLayout.setVisibility(View.GONE);
        }

    }
}
