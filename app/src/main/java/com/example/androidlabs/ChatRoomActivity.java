package com.example.androidlabs;

import  androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    ListView listV;
    EditText editT;
    Button sendBtn;
    Button rcvBtn;
    List<Message> messageList = new ArrayList<>();
    Context context;
    MyListAdapter myAdapter;

    MyOpener myOpener;
    SQLiteDatabase db;
    ContentValues cv;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listV = (ListView)findViewById(R.id.listview);
        editT = (EditText)findViewById(R.id.chatEditText);
        sendBtn = (Button)findViewById(R.id.sendButton);
        rcvBtn = (Button)findViewById(R.id.receiveButton);


        myOpener = new MyOpener(this);
        db = myOpener.getWritableDatabase();
        cv = new ContentValues();



        loadDataFromDatabase();





        sendBtn.setOnClickListener( v -> {
            String message = editT.getText().toString();
            ContentValues cv = new ContentValues();
            cv.put(myOpener.COL_MESSAGE,message);
            cv.put(myOpener.COL_ISSENDORRECEIVE,1);
            long newId = db.insert(myOpener.TABLE_NAME,null,cv);
            Message msg = new Message(message, true,newId);
            messageList.add(msg);
            editT.setText("");
                MyListAdapter mladptr = new MyListAdapter(messageList, getApplicationContext());
                listV.setAdapter(mladptr);




        });

        rcvBtn.setOnClickListener( v -> {
            String message = editT.getText().toString();
            ContentValues cv = new ContentValues();
            cv.put(myOpener.COL_MESSAGE,message);
            cv.put(myOpener.COL_ISSENDORRECEIVE,0);
            long newId = db.insert(myOpener.TABLE_NAME,null,cv);
            Message msg = new Message(message, false,newId);//, newId);
            messageList.add(msg);
            editT.setText("");
                MyListAdapter mladptr = new MyListAdapter(messageList, getApplicationContext());
                listV.setAdapter(mladptr);


        });

        listV.setAdapter( myAdapter = new MyListAdapter(messageList,getApplicationContext()));
        listV.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            adb.setTitle("Do you want to delete this?");
            adb.setMessage("The selected row is: " + position+"\n The database id is: "+id);
            adb.setIcon(android.R.drawable.ic_dialog_alert);

            adb.setPositiveButton("OK",(click,arg)->  {


                    Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                    Message m = messageList.remove(position);
                    deleteMessage(m) ;
            myAdapter.notifyDataSetChanged();
                 });

            adb.setNegativeButton("Cancel", (click,arg)->   {

                    Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                    //finish();
                 });
            adb.show().create();

            return true;
            });

    }

    private void deleteMessage(Message m) {
        db.execSQL("delete from "+MyOpener.TABLE_NAME+" where "+MyOpener.COL_ID+"="+m.messageID);

    }

    private void loadDataFromDatabase(){

        String [] columns = {myOpener.COL_ID,myOpener.COL_ISSENDORRECEIVE,myOpener.COL_MESSAGE};
        Cursor results = db.query(false,myOpener.TABLE_NAME,columns, null,null,null,null,null,null);

        int idColumnIndex = results.getColumnIndex(myOpener.COL_ID);
        int sendOrReceiveColumnIndex = results.getColumnIndex(myOpener.COL_ISSENDORRECEIVE);
        int messageColumnIndex = results.getColumnIndex(myOpener.COL_MESSAGE);

        while(results.moveToNext()){
            long id = results.getLong(idColumnIndex);
            boolean isSendOrReceive = results.getString(sendOrReceiveColumnIndex).equals("1");
            String message = results.getString(messageColumnIndex);

            messageList.add(new Message(message,isSendOrReceive,id));

        }

        results.moveToFirst();
        //results.move
        myOpener.printCursor(results, db.getVersion());

    }



    public class MyListAdapter extends BaseAdapter{

        private List<Message> msg;
        private LayoutInflater inflater;
        private Context context;



    public MyListAdapter(List<Message> msg, Context context) {
                this.msg = msg;
                this.context = context;
                this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return msg.size();
            }

            @Override
            public Message getItem(int position) {
                return msg.get(position);
            }

            @Override
            public long getItemId(int position) {
                return (long) getItem(position).getMessageID();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;

                if (v== null){
                    if (msg.get(position).isSendOrReceieve()){
                        v = inflater.inflate(R.layout.send, null);

                    }else {
                        v = inflater.inflate(R.layout.receive, null);
                    }
                    TextView  messageText = (TextView)v.findViewById(R.id.textViewMessage);
                    messageText.setText(msg.get(position).message);
                }
                return v;
            }
        }


}