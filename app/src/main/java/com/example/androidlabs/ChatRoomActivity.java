package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private List<ChatMsg> msgList = new ArrayList<>();
    private Button sendBtn, receiveBtn;
    private EditText type;
    private ListView listView;
    private chatAdapter adapter;

    private static final String TAG = "ChatRoomActivity";
    private boolean send = true;
    private boolean receive = false;
    private MessageDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendBtn = (Button) findViewById(R.id.sendB);
        receiveBtn = (Button) findViewById(R.id.receiveB);
        type = (EditText) findViewById(R.id.typeHere);
        listView = (ListView) findViewById(R.id.listView);

        dataSource = new MessageDataSource(ChatRoomActivity.this);
        dataSource.open();
        msgList = dataSource.getAllMessages();
        adapter = new chatAdapter(ChatRoomActivity.this, R.layout.activity_chat_room, msgList);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMsg sendMsg = dataSource.createChatMessage(type.getText().toString(), send);
                msgList.add(sendMsg);
                adapter.notifyDataSetChanged();
                type.setText("");
            }
        });

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMsg receiveMsg = dataSource.createChatMessage(type.getText().toString(), receive);
                msgList.add(receiveMsg);
                adapter.notifyDataSetChanged();
                type.setText("");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                builder.setTitle("Do you want to delete this?");
                builder.setMessage("The selected row is: " + (position + 1)
                        + "\nThe database id is: " + adapter.getItemId(position));
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataSource.deleteChatMessage(msgList.get(position));
                        msgList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", (dialog, which) -> {});
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        listView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataSource.close();
    }



    class chatAdapter extends ArrayAdapter<ChatMsg> {

        //        Activity context;
        List<ChatMsg> list;
        private Activity context;

        public chatAdapter(Activity context, int resource, List<ChatMsg> objects) {
            super(context, resource, objects);
//            this.context = context;
            list = objects;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatMsg chatMessage = getItem(position);
            View view;
            LayoutInflater inflater = context.getLayoutInflater();
//          LayoutInflater inflater = LayoutInflater.from(getContext());
            if (chatMessage.send) {
                view = inflater.inflate(R.layout.send, parent, false);
                TextView sendMsg = (TextView) view.findViewById(R.id.send_msg);
                sendMsg.setText(chatMessage.message);
            } else {
                view = inflater.inflate(R.layout.receive, parent, false);
                TextView receiveMsg = (TextView) view.findViewById(R.id.receive_msg);
                receiveMsg.setText(chatMessage.message);
            }
            return view;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ChatMsg getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }
    }
    class ChatMsg {
        long id;
        String message;
        boolean send;

        public ChatMsg() {
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setSendOrRec(boolean sendOrRec) {
            this.send = sendOrRec;
        }
    }

    class ChatSQLiteHelper extends SQLiteOpenHelper {
        static final String TABLE_NAME = "message";
        static final String ID = "id";
        static final String CONTENT = "content";
        static final String TYPE = "type";
        static final String DATABASE_NAME = "message.db";
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_CREATE = "CREATE TABLE "
                + TABLE_NAME + "( " + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CONTENT + " TEXT, " + TYPE + " INTEGER NOT NULL);";

        public ChatSQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    class MessageDataSource {
        SQLiteDatabase database;
        ChatSQLiteHelper dbHelper;
        String[] allColumns = { ChatSQLiteHelper.ID, ChatSQLiteHelper.CONTENT, ChatSQLiteHelper.TYPE};

        public MessageDataSource(Context context) {
            dbHelper = new ChatSQLiteHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        public ChatMsg createChatMessage(String message, boolean type) {
            ContentValues values = new ContentValues();
            values.put(ChatSQLiteHelper.CONTENT, message);
            values.put(ChatSQLiteHelper.TYPE, type);
            long insertId = database.insert(ChatSQLiteHelper.TABLE_NAME, null, values);
            Cursor cursor = database.query(ChatSQLiteHelper.TABLE_NAME, allColumns,
                    ChatSQLiteHelper.ID + "=" + insertId,
                    null, null, null, null);
            cursor.moveToFirst();
            printCursor(cursor, database.getVersion());
            ChatMsg newMessage = cursorToChatMessage(cursor);
            cursor.close();
            return newMessage;
        }

        private ChatMsg cursorToChatMessage(Cursor cursor) {
            ChatMsg chatMessage = new ChatMsg();
            chatMessage.setId(cursor.getLong(0));
            chatMessage.setMessage(cursor.getString(1));
            chatMessage.setSendOrRec(cursor.getString(2).equals("1"));
            return chatMessage;
        }

        public void deleteChatMessage(ChatMsg chatMessage) {
            long id = chatMessage.id;
            database.delete(ChatSQLiteHelper.TABLE_NAME, ChatSQLiteHelper.ID + "=" + id, null);
        }

        public List<ChatMsg> getAllMessages() {
            List<ChatMsg> messages = new ArrayList<>();
            Cursor cursor = database.query(ChatSQLiteHelper.TABLE_NAME, allColumns,
                    null, null, null, null, null);
            cursor.moveToFirst();
            printCursor(cursor, database.getVersion());
            while (!cursor.isAfterLast()) {
                ChatMsg chatMessage = cursorToChatMessage(cursor);
                messages.add(chatMessage);
                cursor.moveToNext();
            }
            cursor.close();
            return messages;
        }

        public void printCursor(Cursor cursor, int version) {
            Log.e(TAG, "version: " + version);
            Log.e(TAG, "The number of columns: " + cursor.getColumnCount());
            Log.e(TAG, "The name of the columns: " + Arrays.toString(cursor.getColumnNames()));
            Log.e(TAG, "The number of rows: " + cursor.getCount());
            while (!cursor.isAfterLast()) {
                String result = cursor.getString(0) + ", "
                        + cursor.getString(1) + ", "
                        + getMessageType(cursor.getString(2));
                Log.e(TAG, "row content: " + result);
                cursor.moveToNext();
            }
            cursor.moveToFirst();
        }

        private String getMessageType(String sendBoolean) {
            if (sendBoolean.contentEquals("1")) {
                return "Send";
            } else {
                return "Receive";
            }
        }
    }



}