package com.example.library;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MyAlertDialog extends AppCompatDialogFragment {

    Button add;
    EditText tekstAdd;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String file="";
        String name = getArguments().getString("name");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(getActivity().openFileInput(name)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                file = file + str;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    if(file.equals("")) {
        View view = inflater.inflate(R.layout.fragment_custom, null);
        builder.setView(view);

        add = view.findViewById(R.id.add);
        tekstAdd = view.findViewById(R.id.tekstadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(getActivity().openFileOutput(name, Context.MODE_PRIVATE)));
                    bw.write(tekstAdd.getText().toString());
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        });
    }else {
        builder.setMessage(file);
    }
        return builder.setPositiveButton("Закрыть",null)
                .create()
                ;



    }
}
