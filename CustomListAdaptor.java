package basicandroid.com.getjsondata;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CustomListAdaptor extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Place> placeList;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdaptor(Activity activity, List<Place> placeList) {
        this.activity = activity;
        this.placeList = placeList;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int location) {
        return placeList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_profile,null);
        }
        if(imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbnail = (NetworkImageView)convertView.findViewById(R.id.thumbnail);
        TextView placenew = (TextView)convertView.findViewById(R.id.place_id);
        TextView description = (TextView)convertView.findViewById(R.id.description_id);
        TextView besttime = (TextView)convertView.findViewById(R.id.besttime_id);
        TextView airport = (TextView)convertView.findViewById(R.id.airport_id);
        TextView railwaystation = (TextView)convertView.findViewById(R.id.railwaystation_id);

        Place place = placeList.get(position);

        thumbnail.setImageUrl(place.getThumbnailUrl(),imageLoader);
        placenew.setText("Place : " +place.getPlace());
        description.setText("Description : " +place.getDescription());
        besttime.setText("Best time to Visit : " +place.getBesttime());
        airport.setText("Nearest Airport : " +place.getAirport());
        railwaystation.setText("Railway Station : " +place.getRailwaystation());


        return convertView;
    }
}
