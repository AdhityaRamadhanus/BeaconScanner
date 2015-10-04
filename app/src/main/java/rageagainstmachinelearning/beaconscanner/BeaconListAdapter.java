package rageagainstmachinelearning.beaconscanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by nao on 10/4/15.
 */
public class BeaconListAdapter extends ArrayAdapter<Beacon> {
    Context context;
    int layoutResourceId;
    ArrayList<Beacon> BeaconList;
    public BeaconListAdapter(Context context,int Resource, int ResourceTextId,ArrayList<Beacon> data) {
        super(context, Resource,ResourceTextId, data);
        this.layoutResourceId = Resource;
        this.context = context;
        this.BeaconList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BeaconHolder holder = null;
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new BeaconHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.icon);
            holder.FirstLine = (TextView)row.findViewById(R.id.firstLine);
            holder.SecondLine = (TextView) row.findViewById(R.id.secondLine);
            row.setTag(holder);
        }
        else {
            holder = (BeaconHolder)row.getTag();
        }

        Beacon beacon = BeaconList.get(position);
        holder.FirstLine.setText(beacon.getBluetoothAddress()+" ID: "+beacon.getId2().toString());
        holder.SecondLine.setText("Distance : "+String.format("%.3f", beacon.getDistance())+" Tx : "+beacon.getTxPower());
        return row;
    }

    static class BeaconHolder {
        ImageView imgIcon;
        TextView FirstLine;
        TextView SecondLine;
    }
}
