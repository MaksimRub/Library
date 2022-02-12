package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    ListView bookList;

    Button add,del;

    EditText name,author;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList=findViewById(R.id.book_list);

        add=findViewById(R.id.add);
        del=findViewById(R.id.del);

        name=findViewById(R.id.name);
        author=findViewById(R.id.author);

        //TODO подготовка данных
        LinkedList<Book> bookLinkedList=new LinkedList<>();
        bookLinkedList.add(new Book("Гарри поттер","Роулинг"));
        bookLinkedList.add(new Book("Идиот","Достаевский"));
        bookLinkedList.add(new Book("Гиперболоид инженнера Гарина","А.Толстой"));
        bookLinkedList.add(new Book("Роковые яйца","М.Булгаков"));
        bookLinkedList.add(new Book("Колобок","народ"));

        //TODO создпние адаптера

        ArrayAdapter<Book>adapter=new ArrayAdapter<>(this,R.layout.list_item,bookLinkedList);
        //SimpleAdapter adapter1;
        //SimpleCursorAdapter adapter2;

        bookList.setAdapter(adapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), bookLinkedList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //TODO создать интерфейс(можно кнопками) добавления/удаления кнниги

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1=name.getText().toString();
                String author1=author.getText().toString();
                bookLinkedList.add(new Book(name1,author1));
                adapter.notifyDataSetChanged();




            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1=name.getText().toString();
                String author1=author.getText().toString();
                for (int i = 0; i < bookLinkedList.size(); i++) {
                    if((name1+" "+author1).equals(bookLinkedList.get(i).toString())){
                        bookLinkedList.remove(i);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });

        adapter.notifyDataSetChanged();//обновление экрана
    }
}