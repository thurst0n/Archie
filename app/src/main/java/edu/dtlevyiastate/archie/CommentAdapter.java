package edu.dtlevyiastate.archie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/** Custom Data Adapter to hold data and then distribute it to the view as the list is scrolled
 * Individual row data is held inside a RowItem object. The Adapter then holds these in an ArrayList
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class CommentAdapter extends ArrayAdapter<CommentItem> {
    //Context for the current view
    Context context;


    public CommentAdapter(Context context, ArrayList<CommentItem> items) {
        super(context, R.layout.list_comment_layout, items);
        this.context = context;
    }

    /**
     * getView method returns the individual row views inflated so they can be displayed in the list
     * @param position position of the RowItem being requested in the ArrayList
     * @param convertView view to hold the entire row view before it is passed back to the ListView
     * @param parent reference to the ListView where the row will be shown.
     * @return view is given back to the listView so only displayed items + 1 on each side will stay
     *          android in memory
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.list_comment_layout, parent, false);
        }

        TextView txtComment = (TextView) view.findViewById(R.id.Comment);
        TextView txtPosted = (TextView) view.findViewById(R.id.Posted);
        TextView txtPoster = (TextView) view.findViewById(R.id.Poster);

        txtComment.setText(getItem(position).getComment());
        txtPosted.setText(getItem(position).getPosted());
        txtPoster.setText(getItem(position).getPoster());
        return view;
    }

}
