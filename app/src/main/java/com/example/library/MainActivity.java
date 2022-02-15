package com.example.library;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    ListView bookList;

    Button add,del;

    EditText name,author,year;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList=findViewById(R.id.book_list);

        add=findViewById(R.id.add);
        del=findViewById(R.id.del);

        name=findViewById(R.id.name);
        author=findViewById(R.id.author);
        year=findViewById(R.id.year);




        //TODO подготовка данных
        LinkedList<Book> bookLinkedList=new LinkedList<>();
        bookLinkedList.add(new Book("Основание","АюАзимов",2015,R.drawable.osnovanie));
        bookLinkedList.add(new Book("Преступление и наказание","Достаевский",1972,R.drawable.prestuplenie));
        bookLinkedList.add(new Book("Шинель","Гоголь",1998,R.drawable.shinel));
        bookLinkedList.add(new Book("Роковые яйца","М.Булгаков",2018,R.drawable.book));
        bookLinkedList.add(new Book("Колобок","народ",2001,R.drawable.book));


        //TODO сщздать массив с ключами и идентификаторами
        String[]keyArray={"title","author","year","cover"};
        int [] idArray={R.id.book_title,R.id.author,R.id.year,R.id.image};

        //TODO сщздание списка map для адаптера
        LinkedList<HashMap<String,Object>> listForAdopter=new LinkedList<>();
        for (int i = 0; i < bookLinkedList.size(); i++) {
            HashMap<String,Object>bookMap=new HashMap<>();
            bookMap.put(keyArray[0],bookLinkedList.get(i).title);
            bookMap.put(keyArray[1],bookLinkedList.get(i).author);
            bookMap.put(keyArray[2],bookLinkedList.get(i).year);
            bookMap.put(keyArray[3],bookLinkedList.get(i).coverId);
            listForAdopter.add(bookMap);

        }
        //TODO создпние адаптера

        //ArrayAdapter<Book>adapter=new ArrayAdapter<>(this,R.layout.list_item,bookLinkedList);
        //SimpleAdapter adapter1;
        //SimpleCursorAdapter adapter2;
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listForAdopter,R.layout.list_item,keyArray,idArray);

        bookList.setAdapter(simpleAdapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), bookLinkedList.get(i).toString(), Toast.LENGTH_SHORT).show();
                FragmentManager manager = getSupportFragmentManager();
                MyAlertDialog myDialogFragment = new MyAlertDialog();
                //myDialogFragment.show(manager, "myDialog");
                Bundle args = new Bundle();
                String a=listForAdopter.get(i).get("title").toString()+listForAdopter.get(i).get("author").toString();
                args.putString("name",a );
                myDialogFragment.setArguments(args);
                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");
            }
        });


        //TODO создать интерфейс(можно кнопками) добавления/удаления кнниги

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1=name.getText().toString();
                String author1=author.getText().toString();
                int year1=0;
                try {
                    year1 = Integer.parseInt(year.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "введите год числом", Toast.LENGTH_SHORT).show();

                }
                if (year1!=0) {
                    bookLinkedList.add(new Book(name1,author1,year1,R.drawable.book));
                    HashMap<String, Object> bookMap = new HashMap<>();
                    bookMap.put(keyArray[0], name1);
                    bookMap.put(keyArray[1], author1);
                    bookMap.put(keyArray[2], year1);
                    bookMap.put(keyArray[3], R.drawable.book);
                    listForAdopter.add(bookMap);
                    simpleAdapter.notifyDataSetChanged();
                }




            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1=name.getText().toString();
                String author1=author.getText().toString();
                for (int i = 0; i < listForAdopter.size(); i++) {
                    if((name1+" "+author1).equals(bookLinkedList.get(i).toString())){
                        listForAdopter.remove(i);
                        break;
                    }
                }
                simpleAdapter.notifyDataSetChanged();

            }
        });

        simpleAdapter.notifyDataSetChanged();//обновление экрана

        //TODO сделать аннотацию к книге
    }
}