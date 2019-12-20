package com.example.hands_showprototype.AdminPlus;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hands_showprototype.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SQnodeClass extends ArrayAdapter<SupportersClass> {
    private Activity context;
    private List<SupportersClass> supporterslist;
    private QueryDocumentSnapshot Document;

    public SQnodeClass(Activity context, List<SupportersClass> supporterslist){
        super(context, R.layout.activity_sq_node,supporterslist);
        this.context=context;
        this.supporterslist=supporterslist;
        this.Document=Document;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View ListViewItem = inflater.inflate(R.layout.activity_sq_node,null,true);
        TextView textViewName = (TextView) ListViewItem.findViewById(R.id.textViewName);
        String UID = Document.getString("uid");
        textViewName.setText(Document.getString("name"));

        return ListViewItem;
    }
}
