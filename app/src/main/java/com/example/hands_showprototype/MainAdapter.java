package com.example.hands_showprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] lettersNames;
    private int[] letterNameByImage;

    public MainAdapter(Context c, String[] lettersNames,int[] letterNameByImage)
    {
        this.context=c;
        this.lettersNames=lettersNames;
        this.letterNameByImage=letterNameByImage;

    }
    @Override
    public int getCount() {
        return this.lettersNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater==null)
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view==null)
            view=inflater.inflate(R.layout.row_item,null);
        ImageView imageView=view.findViewById(R.id.image_View);
        TextView textView=view.findViewById(R.id.text_view);

        imageView.setImageResource(letterNameByImage[i]);
        textView.setText(lettersNames[i]);
        return view;
    }
}
