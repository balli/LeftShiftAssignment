package com.example.maheshbaligar.lsdemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MAHESH BALIGAR on 20-04-2016.
 */
public class WeatherListAdapter extends ArrayAdapter<WeatherBean> {

    private Context context;
    private int resId;
    public ArrayList<WeatherBean> list_data;



    public WeatherListAdapter(Context context, int textViewResourceId,
                                      ArrayList<WeatherBean> data) {
        super(context, textViewResourceId, data);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.resId = textViewResourceId;
        this.list_data = data;


    }

    private class ViewHolder {
        TextView Date, Main_temp,Min_temp,Max_Temp,Eve_Temp,Night_Temp;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(resId, null);
            holder = new ViewHolder();
            holder.Date = (TextView) convertView
                    .findViewById(R.id.date);
            holder.Main_temp = (TextView) convertView
                    .findViewById(R.id.Main_weather);

            holder.Min_temp = (TextView) convertView
                    .findViewById(R.id.Min_temp);

            holder.Max_Temp = (TextView) convertView
                    .findViewById(R.id.Max_temp);

            holder.Eve_Temp = (TextView) convertView
                    .findViewById(R.id.Eve_temp);

            holder.Night_Temp = (TextView) convertView
                    .findViewById(R.id.Night_temp);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.Date.setText(list_data.get(position).getDate());
        holder.Main_temp.setText(list_data.get(position).getMain_Temp());

        holder.Min_temp.setText(list_data.get(position).getMin_Temp()+" Cel");
        holder.Max_Temp.setText(list_data.get(position).getMax_Temp()+" Cel");

        holder.Eve_Temp.setText(list_data.get(position).getEve_Temp()+" Cel");
        holder.Night_Temp.setText(list_data.get(position).getNight_Temp()+" Cel");

        return convertView;

    }

//	@Override
//	public Filter getFilter() {
//		// TODO Auto-generated method stub
//		if (holderFilter == null) {
//			holderFilter = new HolderFilter();
//		}
//		return holderFilter;
//	}

    @Override
    public int getCount() {
        return list_data.size();
    }

//	private class HolderFilter extends Filter {
//		@SuppressLint("DefaultLocale")
//		@Override
//		protected FilterResults performFiltering(CharSequence constraint) {
//			FilterResults results = new FilterResults();
//			if (constraint == null || constraint.length() == 0) {
//				results.values = searchtList;
//				results.count = searchtList.size();
//			} else {
//				List<Attendence_Bean> nHolderList = new ArrayList<Attendence_Bean>();
//				nHolderList.clear();
//				for (Attendence_Bean h : searchtList) {
//					if (h.getCname().toUpperCase()
//							.contains(constraint.toString().toUpperCase())) {
//						nHolderList.add(h);
//					}
//				}
//				results.values = nHolderList;
//				results.count = nHolderList.size();
//			}
//			return results;
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		protected void publishResults(CharSequence constraint,
//				FilterResults results) {
//			if (results.count == 0) {
//				list_data = (ArrayList<Attendence_Bean>) results.values;
//				notifyDataSetChanged();
//
//			} else {
//
//				list_data = (ArrayList<Attendence_Bean>) results.values;
//				notifyDataSetChanged();
//			}
//
//		}
//	}

}
