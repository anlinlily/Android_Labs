package com.example.androidlabs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private List<ChatMsg> msgList = new ArrayList<>();
    private Button sendBtn, receiveBtn;
    private EditText type;
    private ListView listView;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendBtn = (Button) findViewById(R.id.sendB);
        receiveBtn = (Button) findViewById(R.id.receiveB);
        type = (EditText) findViewById(R.id.typeHere);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MsgAdapter(ChatRoomActivity.this, R.layout.activity_chat_room, msgList);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMsg sendMsg = new ChatMsg(type.getText().toString(), true);
                msgList.add(sendMsg);
                adapter.notifyDataSetChanged();
                type.setText("");
            }
        });

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMsg receiveMsg = new ChatMsg(type.getText().toString(), false);
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
                        msgList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        listView.setAdapter(adapter);
    }


    class ChatMsg {
        private String msg;
        private boolean send;

        public ChatMsg(String msg, boolean send) {
            this.msg = msg;
            this.send = send;
        }
    }

    class MsgAdapter extends ArrayAdapter<ChatMsg> {

        //        Activity context;
        List<ChatMsg> list;

        public MsgAdapter(Context context, int resource, List<ChatMsg> objects) {
            super(context, resource, objects);
//            this.context = context;
            list = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatMsg message = getItem(position);
            View view;
//            LayoutInflater inflater = context.getLayoutInflater();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (message.send) {
                view = inflater.inflate(R.layout.send, parent, false);
                TextView sendMsg = (TextView) view.findViewById(R.id.send_msg);
                sendMsg.setText(message.msg);
            } else {
                view = inflater.inflate(R.layout.receive, parent, false);
                TextView receiveMsg = (TextView) view.findViewById(R.id.receive_msg);
                receiveMsg.setText(message.msg);
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
            return (long) position;
        }
    }
}