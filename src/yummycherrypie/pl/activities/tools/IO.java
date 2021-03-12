package yummycherrypie.pl.activities.tools;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by CoreAttack on 18.12.2015.
 */
public class IO {
    private static String DIR_SD = "yummycherrypie";

    public static void saveDBToExternalStorage(Context context, String fileName){
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_SHORT).show();
            return;
        }

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            bw.close();
            Toast.makeText(context, "Файл создан по адресу: " + sdPath.getAbsolutePath() + "/" + DIR_SD + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Не удалось записать файл", Toast.LENGTH_SHORT).show();
        }

    }

    public static void saveDBToLocalStorage(Context context, String fileName){

        File sdPath = context.getFilesDir();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            bw.close();
            Toast.makeText(context, "Файл создан по адресу: " + sdPath.getAbsolutePath() + "/" + DIR_SD + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Не удалось записать файл", Toast.LENGTH_SHORT).show();
        }

    }
}
