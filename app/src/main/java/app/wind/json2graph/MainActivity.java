package app.wind.json2graph;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;


public class MainActivity extends Activity {

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series, series1;
    private String[] arrData = null;
    private JSONArray jArr = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        series = new LineGraphSeries<DataPoint>();
        series1 = new LineGraphSeries<DataPoint>();

        new AsyncTaskJson().execute();


    }

    public class AsyncTaskJson extends AsyncTask<String, String, String> {

        String url = "http://api.thingspeak.com/channels/9/feed.json";
        JSONArray jArr = null;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for (int i = 0; i < 5; i++) {
                Log.e("check", "arr  " + arrData[i]);
            }

            GraphView graph = (GraphView) findViewById(R.id.graph);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(arrData);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            series1.setColor(Color.GREEN);
            graph.addSeries(series);
            graph.addSeries(series1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            try {

                JSON jSon = new JSON();
                JSONObject jObj = jSon.getJsonUrl(url);
                jArr = jObj.getJSONArray("feeds");
                arrData = new String[5];
                int j = 0;
                Log.e("check", "lenght " + jArr.length());
                for (int i = 0; i <= jArr.length(); i++) {

                    if (i >= 95) {

                        JSONObject c = jArr.getJSONObject(i);
                        String time = c.getString("created_at").substring(11, 19);
                        arrData[j] = time;
                        int ainLight = Integer.parseInt(c.getString("field1"));
                        Double tempDouble = Double.parseDouble(c.getString("field2"));
                        DecimalFormat dm = new DecimalFormat("0");
                        int ainTemp = Integer.parseInt(dm.format(tempDouble));
                        Log.e("check", "Decimal " + ainTemp);
                        series.appendData(new DataPoint(i, ainLight), true, 10);
                        series1.appendData(new DataPoint(i, ainTemp), true, 10);
                        j++;
                    }

                }


            } catch (JSONException e) {

            }

            return null;
        }


    }




}
