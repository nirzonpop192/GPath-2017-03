package com.siddiquinoor.restclient.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.siddiquinoor.restclient.R;

import java.util.ArrayList;

/**
 * Created by USER on 10/16/2015.
 */
public class SummaryAssignListModelAdapter  extends BaseAdapter{
    private ArrayList<SummaryAssignListModel> arrayList=new ArrayList<SummaryAssignListModel>();
    private Activity mactivity;
    private LayoutInflater inflater;

    public SummaryAssignListModelAdapter(Activity mactivity, ArrayList<SummaryAssignListModel> arrayList) {
        this.mactivity = mactivity;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public SummaryAssignListModel getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        ViewHolder holder;
        if (inflater==null){
            inflater= (LayoutInflater) mactivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView==null){
            row=inflater.inflate(R.layout.list_row_summary_assigne_list,null);
            holder=new ViewHolder();
            holder.tv_id= (TextView) row.findViewById(R.id.tv_row_assign_id);
            holder.tv_name= (TextView) row.findViewById(R.id.tv_row_assign_name);
            holder.tv_regDate= (TextView) row.findViewById(R.id.tv_row_assign_regDate);

           row.setTag(holder);
        }
        else {
            holder= (ViewHolder) row.getTag();
        }
        SummaryAssignListModel assignData=getItem(position);

        holder.tv_id.setText(assignData.getCustomId());
        holder.tv_name.setText(assignData.getMemberName());
        holder.tv_regDate.setText(assignData.getRegDate());

        return row;
    }
    static class ViewHolder{
        TextView tv_id;
        TextView tv_name;
        TextView tv_regDate;
    }
}
