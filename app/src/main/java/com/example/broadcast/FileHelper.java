package com.example.broadcast;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {
    public static void make_dir(Context context, String name) {
        File dir = new File(context.getFilesDir(), name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void write(Context context, String name, String data, int mode) {//0 is cover, 1 is append
        File file = new File(context.getFilesDir(), name);
        FileOutputStream fos = null;
        try {
            fos = mode == 0 ? new FileOutputStream(file) : new FileOutputStream(file, true);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String read(Context context, String name) {
        File file = new File(context.getFilesDir(), name);
        if (!file.exists()) return "not exist";
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(name); InputStreamReader isr = new InputStreamReader(fis); BufferedReader br = new BufferedReader(isr);) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void delete_file(Context context, String name) {
        File file = new File(context.getFilesDir(), name);
        file.delete();
    }
}