package rageagainstmachinelearning.beaconscanner;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;
import java.util.ArrayList;
import java.util.Collection;


public class RangeActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager;
    private ArrayList<Beacon> Beacons;
    private BeaconListAdapter BeaconsAdapter;

    private ListView Beaconlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);

        /*Initialize Beacon List etc */
        Beaconlist = (ListView) findViewById(R.id.list_beacon);
        Beacons = new ArrayList<Beacon>();
        BeaconsAdapter = new BeaconListAdapter(this, R.layout.list_item_row,R.id.firstLine,  Beacons);
        Beaconlist.setAdapter(BeaconsAdapter);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for (Beacon b : beacons) {
                        if (!Beacons.contains(b)) {
                            Beacons.add(b);
                        } else {
                            Beacons.set(Beacons.indexOf(b), b);
                        }
                    }
                    Log.d("BeaconScanner", "Change Data");
                    notifAdapter();
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("CB10023F-A318-3394-4199-A8730C7C1AEC", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void notifAdapter() {
        runOnUiThread(new Runnable() {
            public void run() {
                BeaconsAdapter.notifyDataSetChanged();
            }
        });
    }
}
