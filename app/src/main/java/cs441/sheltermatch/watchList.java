package cs441.sheltermatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

public class watchList extends BaseAdapter {

    // Gets the watchlist from the MainActivity Class
    private static cards watchList[] = MainActivity.getWatchlist();
    Context context;

    @Override
    public int getCount() {
        return watchList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.watchlist, parent, false);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        TextView textView_name = (TextView)convertView.findViewById(R.id.name);
        textView_name.setText(watchList[position].getName());
        return null;
    }
}

