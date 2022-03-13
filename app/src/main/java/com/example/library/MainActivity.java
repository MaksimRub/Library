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
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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

    Button add,del,search,information;

    EditText name,author,year,genre;


    Dialog dialog;

    TextView textView;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    LinkedList<HashMap<String,Object>> listForAdopter=new LinkedList<>();

    OpenHelper openHelper;
    SQLiteDatabase database;
    int l=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList=findViewById(R.id.book_list);

        add=findViewById(R.id.add);
        del=findViewById(R.id.del);
        search=findViewById(R.id.search);
        information=findViewById(R.id.information);

        name=findViewById(R.id.name);
        author=findViewById(R.id.author);
        year=findViewById(R.id.year);
        genre=findViewById(R.id.genre);



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
        /*for (String s:ret) {
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
        }*/

        Cursor cursor=database.query(OpenHelper.TABLE_NAME,
                new String[]{OpenHelper.COLUMN_AUTHOR,OpenHelper.COLUMN_TITLE,OpenHelper.COLUMN_GENRE,OpenHelper.COLUMN_YEAR},
                null,null,null,null,null);
        cursor.moveToFirst();
        l=0;
        while (cursor.moveToNext()) {
            l++;
            if(l==1){
                cursor.moveToFirst();
            }
            String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
            String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
            String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
            int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));

            bookLinkedList.add(new Book(title, author,genre, year, R.drawable.book));
        }





        //TODO подготовка данных
        if(bookLinkedList.size()==0) {
            bookLinkedList.add(new Book("Основание", "АюАзимов", "классика",2015, R.drawable.osnovanie));
            bookLinkedList.add(new Book("Основани", "АюАзимов", "классика",2015, R.drawable.osnovanie));
            bookLinkedList.add(new Book("Преступление и наказание", "Достаевский", "классика",1972, R.drawable.prestuplenie));
            bookLinkedList.add(new Book("Шинель", "Гоголь", "классика",1998, R.drawable.shinel));
            bookLinkedList.add(new Book("Роковые яйца", "М.Булгаков", "классика",2018, R.drawable.book));
            bookLinkedList.add(new Book("Колобок", "народ", "сказка",2001, R.drawable.book));
        }



        //TODO сщздать массив с ключами и идентификаторами
        String[]keyArray={"title","author","year","genre","cover"};
        int [] idArray={R.id.book_title,R.id.author,R.id.year,R.id.genre,R.id.image};

        //TODO сщздание списка map для адаптера
        for (int i = 0; i < bookLinkedList.size(); i++) {
            HashMap<String,Object>bookMap=new HashMap<>();
            bookMap.put(keyArray[0],bookLinkedList.get(i).title);
            bookMap.put(keyArray[1],bookLinkedList.get(i).author);
            bookMap.put(keyArray[2],bookLinkedList.get(i).year);
            bookMap.put(keyArray[3],bookLinkedList.get(i).genre);
            bookMap.put(keyArray[4],bookLinkedList.get(i).coverId);
            listForAdopter.add(bookMap);


            //TODO проверить на дубликаты (сделать дома)
            if(!preferences.contains("controller")) {
                ContentValues values = new ContentValues();
                values.put(OpenHelper.COLUMN_AUTHOR, bookLinkedList.get(i).author);
                values.put(OpenHelper.COLUMN_TITLE, bookLinkedList.get(i).title);
                values.put(OpenHelper.COLUMN_YEAR, bookLinkedList.get(i).year);
                values.put(OpenHelper.COLUMN_GENRE, bookLinkedList.get(i).genre);
                database.insert(OpenHelper.TABLE_NAME, null, values);
                Toast.makeText(getApplicationContext(), "книги добавленны в базу данных", Toast.LENGTH_SHORT).show();
            }


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


                    //TODO показать результаты на интерфейсе
                }
        });


        //TODO создать интерфейс(можно кнопками) добавления/удаления кнниги

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_information);
                TextView textView_authors=dialog.findViewById(R.id.authors);
                TextView textView_books=dialog.findViewById(R.id.books);
                TextView textView_genres=dialog.findViewById(R.id.genres);
                String all_authors="авторы:";
                String all_genres="жанры:";
                String all_books="книги:";
                Cursor cursor1=database.query(OpenHelper.TABLE_NAME,
                        new String[]{OpenHelper.COLUMN_AUTHOR},
                        null,null,null,null,OpenHelper.COLUMN_AUTHOR);
                cursor1.moveToFirst();
                l=0;
                while (cursor1.moveToNext()) {
                    l++;
                    if(l==1){
                        cursor1.moveToFirst();
                    }
                    String author = cursor1.getString(cursor1.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                    all_authors=all_authors+"\n"+author;

                }
                Cursor cursor2=database.query(OpenHelper.TABLE_NAME,
                        new String[]{OpenHelper.COLUMN_AUTHOR,OpenHelper.COLUMN_TITLE,OpenHelper.COLUMN_GENRE},
                        null,null,null,null,OpenHelper.COLUMN_TITLE);
                cursor2.moveToFirst();
                l=0;
                while (cursor2.moveToNext()) {
                    l++;
                    if(l==1){
                        cursor2.moveToFirst();
                    }
                    String author = cursor2.getString(cursor2.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                    String title = cursor2.getString(cursor2.getColumnIndex(OpenHelper.COLUMN_TITLE));
                    String genre = cursor2.getString(cursor2.getColumnIndex(OpenHelper.COLUMN_GENRE));
                    all_genres=all_genres+"\n"+genre;
                    all_books=all_books+"\n"+author+" "+title;

                }
                textView_authors.setText(all_authors);
                textView_books.setText(all_books);
                textView_genres.setText(all_genres);
                dialog.show();



            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1=name.getText().toString();
                String author1=author.getText().toString();
                String genre1=genre.getText().toString();
                int year1=0;
                try {
                    year1 = Integer.parseInt(year.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "введите год числом", Toast.LENGTH_SHORT).show();

                }
                if (year1!=0) {
                    bookLinkedList.add(new Book(name1,author1,genre1,year1,R.drawable.book));
                    HashMap<String, Object> bookMap = new HashMap<>();
                    bookMap.put(keyArray[0], name1);
                    bookMap.put(keyArray[1], author1);
                    bookMap.put(keyArray[2], year1);
                    bookMap.put(keyArray[3], genre1);
                    bookMap.put(keyArray[4], R.drawable.book);
                    listForAdopter.add(bookMap);
                    simpleAdapter.notifyDataSetChanged();
                }


                ContentValues values=new ContentValues();
                values.put(OpenHelper.COLUMN_AUTHOR,author1);
                values.put(OpenHelper.COLUMN_TITLE,name1);
                values.put(OpenHelper.COLUMN_YEAR,year1);
                values.put(OpenHelper.COLUMN_GENRE,genre1);
                database.insert(OpenHelper.TABLE_NAME,null,values);
                Toast.makeText(getApplicationContext(), "добавлено в базу данных", Toast.LENGTH_SHORT).show();




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
               /* TreeSet<Book> treeSet = new TreeSet<>();
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
                dialog.show();*/
                String author1 = author.getText().toString();
                String title1 = name.getText().toString();
                String year1 = year.getText().toString();
                String genre1 = genre.getText().toString();
                String all;
                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_simple);
                textView = dialog.findViewById(R.id.search);

                if (!author1.equals("")) {
                    Cursor cursor = database.query(OpenHelper.TABLE_NAME,
                            new String[]{OpenHelper.COLUMN_AUTHOR, OpenHelper.COLUMN_TITLE, OpenHelper.COLUMN_YEAR, OpenHelper.COLUMN_GENRE},
                            "author=?", new String[]{author1}, null, null, null);
                    cursor.moveToFirst();
                    all = "";
                    if (!cursor.moveToNext()) {
                        cursor.moveToFirst();
                        try {
                            String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                            String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                            String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                            int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                            String year_need = Integer.toString(year);
                            all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                        } catch (CursorIndexOutOfBoundsException e) {
                            Toast.makeText(getApplicationContext(), "нет таких книг", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.moveToFirst();
                    l = 0;
                    while (cursor.moveToNext()) {
                        l++;
                        if (l == 1) {
                            cursor.moveToFirst();
                        }
                        String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                        String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                        String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                        int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                        String year_need = Integer.toString(year);
                        all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                    }

                    textView.setText(all);
                    dialog.show();

                }
                if (!title1.equals("")) {
                    Cursor cursor = database.query(OpenHelper.TABLE_NAME,
                            new String[]{OpenHelper.COLUMN_AUTHOR, OpenHelper.COLUMN_TITLE, OpenHelper.COLUMN_YEAR, OpenHelper.COLUMN_GENRE},
                            "title=?", new String[]{title1}, null, null, null);
                    cursor.moveToFirst();
                    all = "";
                    if (!cursor.moveToNext()) {
                        cursor.moveToFirst();
                        try {
                            String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                            String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                            String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                            int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                            String year_need = Integer.toString(year);
                            all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                        } catch (CursorIndexOutOfBoundsException e) {
                            Toast.makeText(getApplicationContext(), "нет таких книг", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.moveToFirst();

                    l = 0;
                    while (cursor.moveToNext()) {
                        l++;
                        if (l == 1) {
                            cursor.moveToFirst();
                        }
                        String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                        String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                        String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                        int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                        String year_need = Integer.toString(year);
                        all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                    }

                    textView.setText(all);
                    dialog.show();
                }


                if (!year1.equals("")) {
                    Cursor cursor = database.query(OpenHelper.TABLE_NAME,
                            new String[]{OpenHelper.COLUMN_AUTHOR, OpenHelper.COLUMN_TITLE, OpenHelper.COLUMN_YEAR, OpenHelper.COLUMN_GENRE},
                            "year=?", new String[]{year1}, null, null, null);
                    cursor.moveToFirst();
                    all = "";
                    if (!cursor.moveToNext()) {
                        cursor.moveToFirst();
                        try {
                            String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                            String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                            String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                            int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                            String year_need = Integer.toString(year);
                            all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                        } catch (CursorIndexOutOfBoundsException e) {
                            Toast.makeText(getApplicationContext(), "нет таких книг", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.moveToFirst();

                    l = 0;
                    while (cursor.moveToNext()) {
                        l++;
                        if (l == 1) {
                            cursor.moveToFirst();
                        }
                        String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                        String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                        String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                        int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                        String year_need = Integer.toString(year);
                        all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                    }

                    textView.setText(all);
                    dialog.show();
                }
                if (!genre1.equals("")) {
                    Cursor cursor = database.query(OpenHelper.TABLE_NAME,
                            new String[]{OpenHelper.COLUMN_AUTHOR, OpenHelper.COLUMN_TITLE, OpenHelper.COLUMN_YEAR, OpenHelper.COLUMN_GENRE},
                            "genre=?", new String[]{genre1}, null, null, null);
                    cursor.moveToFirst();
                    all = "";
                    if (!cursor.moveToNext()) {
                        cursor.moveToFirst();
                        try {
                            String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                            String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                            String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                            int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                            String year_need = Integer.toString(year);
                            all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                        } catch (CursorIndexOutOfBoundsException e) {
                            Toast.makeText(getApplicationContext(), "нет таких книг", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.moveToFirst();

                    l = 0;
                    while (cursor.moveToNext()) {
                        l++;
                        if (l == 1) {
                            cursor.moveToFirst();
                        }
                        String author = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTHOR));
                        String title = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_TITLE));
                        String genre = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_GENRE));
                        int year = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_YEAR));
                        String year_need = Integer.toString(year);
                        all = all + author + " " + title + " " + year_need + " " + genre + "\n";
                    }

                    textView.setText(all);
                    dialog.show();

                }


            }

        });



        //TODO сделать аннотацию к книге
        simpleAdapter.notifyDataSetChanged();//обновление экрана
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
        editor.putInt("controller",1);
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