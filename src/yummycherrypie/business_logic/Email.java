package yummycherrypie.business_logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.dal.DBHelper;

/**
 * Created by CoreAttack on 18.12.2015.
 */
public class Email {

    private Context context;

    public Email(Context context){
        this.context = context;
    }

    private String email;

    //todo научиться прикреплять файлы в сообщении
    public void SendMessage(){

        String text = "";
        try{
            text = String.format("yummycherrypie %s", context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        }
        catch (Exception e){
            text = "This message was automatic generated. Do not reply this e-mail.";
        }

        File root = Environment.getExternalStorageDirectory();
        File dbFile = DBHelper.pathDataBaseFile(context);
        String copyFileName = Environment.getExternalStorageDirectory() + File.separator + dbFile.getName() + "-copy";

        if (root.canWrite()) {
            File copiedFile = FileHelper.createTempFile(copyFileName);
            try {
                FileHelper.copyFile(dbFile, copiedFile);
            }catch(Exception e){
                LogExtension.Error(e.getMessage());
            }
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_SUBJECT, "Data base backup");
            i.putExtra(Intent.EXTRA_TEXT, text);
            i.setType("application/octet-stream");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(copyFileName)));
            try {
                context.startActivity(Intent.createChooser(i, "Чем отправить?"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "Не существует приложения для отправки сообщения по почте!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Нет прав на копирование базы данных!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getEmail(Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("E-mail");
        alertDialog.setMessage("Введите e-mail");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        email = input.getText().toString();
                    }
                });

        alertDialog.setNegativeButton("отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        email = "";
                        dialog.cancel();
                    }
                });
        return email;
    }
}
