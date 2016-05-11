package com.prappz.envi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prappz.envi.R;
import com.prappz.envi.adapter.MessageListAdapter;
import com.prappz.envi.application.PreferenceManager;

import java.util.List;

/**
 * Created by royce on 05-05-2016.
 */
public class ChatFragment extends Fragment {

    ListView listView;
    MessageListAdapter messageListAdapter;
    EditText messageBox;
    ImageView messageSend;

    public ChatFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.message_list);
        messageBox = (EditText) view.findViewById(R.id.message_box);
        messageSend = (ImageView) view.findViewById(R.id.message_send);

        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        setData();
    }


    private void setData() {
        ParseQuery<ParseObject> userParseQuery = ParseQuery.getQuery("Message");
//        userParseQuery.whereEqualTo("zip", PreferenceManager.getInstance(getActivity()).getString("ZIP"));
        // todo add the above code to restrict messages by ZIP CODE
        userParseQuery.setLimit(20);
        userParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    messageListAdapter = new MessageListAdapter(objects, getActivity());
                    listView.setAdapter(messageListAdapter);
                }
            }
        });
    }


    private void sendMessage() {
        if (messageBox.getText().toString().trim().replaceAll(" ", "").contentEquals(""))
            Toast.makeText(getActivity(), "You are trying to send an empty message", Toast.LENGTH_SHORT).show();
        else {
            hideKeyboard();
            Toast.makeText(getActivity(), "Sending message...", Toast.LENGTH_LONG).show();
            final ParseObject message = ParseObject.create("Message");
            message.put("from", PreferenceManager.getInstance(getActivity()).getString("name"));
            message.put("zip", PreferenceManager.getInstance(getActivity()).getString("ZIP"));
            message.put("content", messageBox.getText().toString());
            messageBox.setText("");
            message.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
                        messageListAdapter.add(message);
                        listView.setStackFromBottom(true);
                    } else
                        Toast.makeText(getActivity(), "Message sending failed due to reason " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
