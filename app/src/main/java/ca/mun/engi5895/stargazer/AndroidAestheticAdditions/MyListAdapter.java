package ca.mun.engi5895.stargazer.AndroidAestheticAdditions;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import ca.mun.engi5895.stargazer.R;

/**
 * Custom ListAdapter for turning the hash map of satellite data into the expandable list view
 */
public class MyListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitle;
    private HashMap<String, List<String>> listDetail;

    public MyListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.listTitle = expandableListTitle;
        this.listDetail = expandableListDetail;
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @return
     */
    @Override
    public Object getGroup(int listPosition) {
        return this.listTitle.get(listPosition);
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @return
     */
    @Override
    public int getGroupCount() {
        return this.listTitle.size();
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @return
     */
    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.list_parent, null);
            }
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @param expandedListPosition
     * @return
     */
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.listDetail.get(this.listTitle.get(listPosition)).get(expandedListPosition);
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @param expandedListPosition
     * @return
     */
    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @param expandedListPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.list_child, null);
            }
        }
        TextView expandedListTextView = convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @return
     */
    @Override
    public int getChildrenCount(int listPosition) {
        return this.listDetail.get(this.listTitle.get(listPosition)).size();
    }


    /**
     * Overriden method of BaseExpandableListAdapter
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Overriden method of BaseExpandableListAdapter
     * @param listPosition
     * @param expandedListPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}