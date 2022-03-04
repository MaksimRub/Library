package com.example.library;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    ListView bookList;

    Button add,del,search;

    EditText name,author,year;


    Dialog dialog;

    TextView textView;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    LinkedList<HashMap<String,Object>> listForAdopter=new LinkedList<>();

    OpenHelper openHelper;
    SQLiteDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList=findViewById(R.id.book_list);

        add=findViewById(R.id.add);
        del=findViewById(R.id.del);
        search=findViewById(R.id.search);

        name=findViewById(R.id.name);
        author=findViewById(R.id.author);
        year=findViewById(R.id.year);


        openHelper=new OpenHelper(this);
        database=openHelper.getWritableDatabase();

        preferences=getSharedPreferences("book",MODE_PRIVATE);
        editor=preferences.edit();
        Set<String> ret = preferences.getStringSet("book", new HashSet<>());
        String title1="";
        String author1="";
        String another="";
        String year1="";
        LinkedList<Book> bookLinkedList=new LinkedList<>();
        for (String s:ret) {
            for (int i = 0; i < s.length(); i++) {
                String str=Character.toString(s.charAt(i));
                if(str.equals("*")){
                    another=another+str;
                }
                if(another.equals("*")&&!(Character.toString(s.charAt(i+1)).equals("*"))){
                    title1=title1+Character.toString(s.charAt(i+1));
                }
                if(another.equals("**")&&!(Character.toString(s.charAt(i+1)).equals("*"))){
                    author1=author1+Character.toString(s.charAt(i+1));
                }
                if(another.equals("***")&&i<s.length()-1){
                    year1=year1+Character.toString(s.charAt(i+1));
                }

            }
            bookLinkedList.add(new Book(title1,author1,Integer.parseInt(year1),R.drawable.book));
            title1="";
            author1="";
            another="";
            year1="";
        }






        //TODO подготовка данных
        if(bookLinkedList.size()==0) {
            bookLinkedList.add(new Book("Основание", "АюАзимов", 2015, R.drawable.osnovanie));
            bookLinkedList.add(new Book("Основани", "АюАзимов", 2015, R.drawable.osnovanie));
            bookLinkedList.add(new Book("Преступление и наказание", "Достаевский", 1972, R.drawable.prestuplenie));
            bookLinkedList.add(new Book("Шинель", "Гоголь", 1998, R.drawable.shinel));
            bookLinkedList.add(new Book("Роковые яйца", "М.Булгаков", 2018, R.drawable.book));
            bookLinkedList.add(new Book("Колобок", "народ", 2001, R.drawable.book));
        }


        //TODO сщздать массив с ключами и идентификаторами
        String[]keyArray={"title","author","year","cover"};
        int [] idArray={R.id.book_title,R.id.author,R.id.year,R.id.image};

        //TODO сщздание списка map для адаптера
        for (int i = 0; i < bookLinkedList.size(); i++) {
            HashMap<String,Object>bookMap=new HashMap<>();
            bookMap.put(keyArray[0],bookLinkedList.get(i).title);
            bookMap.put(keyArray[1],bookLinkedList.get(i).author);
            bookMap.put(keyArray[2],bookLinkedList.get(i).year);
            bookMap.put(keyArray[3],bookLinkedList.get(i).coverId);
            listForAdopter.add(bookMap);


            //TODO проверить на дубликаты (сделать дома)
            ContentValues values=new ContentValues();
            values.put(OpenHelper.COLUMN_AUTHOR,bookLinkedList.get(i).author);
            values.put(OpenHelper.COLUMN_TITLE,bookLinkedList.get(i).title);
            values.put(OpenHelper.COLUMN_YEAR,bookLinkedList.get(i).year);
            database.insert(OpenHelper.TABLE_NAME,null,values);


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
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TreeSet<Book> treeSet = new TreeSet<>();
                for (int i = 0; i < bookLinkedList.size(); i++) {
                    treeSet.add(bookLinkedList.get(i));
                }
                String author1=author.getText().toString();
                String all="";
                for (Book s : treeSet) {
                    if(s.author.equals(author1))
                        all=all+"\n"+s.toString();

                }
                dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_simple);
                textView=dialog.findViewById(R.id.search);
                textView.setText(all);
                dialog.show();


            }
        });

        simpleAdapter.notifyDataSetChanged();//обновление экрана

        //TODO сделать аннотацию к книге
    }

    void saveData() {
        Set<String> book = new HashSet<>();
        for (int i = 0; i < listForAdopter.size(); i++) {
            String s = "*"+listForAdopter.get(i).get("title").toString() + " *" +
                    listForAdopter.get(i).get("author").toString()+" *"
                    +listForAdopter.get(i).get("year").toString();
            book.add(s);

        }
        editor.putStringSet("book",book);
        editor.apply();
        Toast.makeText(this, "Saved",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();

        database.close();

    }
}