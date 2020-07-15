package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView weatherTemp;
    TextView uv,minTemperature,maxTemperature,currentTemperature;



    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String uvRating,min,max,currentTemp,iconName;
        private Bitmap currentWeather;

        public final static String IMAGE_URL = "http://openweathermap.org/img/w/";



        @Override
        protected String doInBackground(String... args) {

            try {
                String url = args[0];

                //create a URL object of what server to contact:
                URL url1 = new URL(url);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();

                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);


                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //Create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            Log.e("AsyncTask", "Current temp.: " + currentTemp);
                            publishProgress(25);
                            Thread.sleep(500);
                            min = xpp.getAttributeValue(null, "min");
                            Log.e("AsyncTask", "Min: "+ min);
                            publishProgress(50);
                            Thread.sleep(500);
                            max = xpp.getAttributeValue(null, "max");
                            Log.e("AsyncTask", "Max: "+ max);
                            publishProgress(75);

                        } else if (xpp.getName().equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                            String fileName = iconName + ".png";
                            Log.e("AsyncTask", "Icon name: "+ iconName);
                            if (fileExistance(fileName)) {

                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(getBaseContext().getFileStreamPath(fileName));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                currentWeather = BitmapFactory.decodeStream(fis);

                            } else {
                                currentWeather = HTTPUtils.getImage(IMAGE_URL + fileName);
                                FileOutputStream outputStream = null;
                                try {
                                    outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    currentWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                } catch (Exception e) {
                                    Log.e("AsyncTask", "Error");
                                }
                            }
                            publishProgress(100);
                        }
                    }
                    xpp.next();
                }

                //create the network connection for UV:
                URL UVurl = new URL("https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                response = UVConnection.getInputStream();




                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject jsonObject = new JSONObject(result);
                uvRating = String.valueOf(jsonObject.getDouble("value"));
                Log.e("AsyncTask", "UV: " +uvRating);
                return null;
            }
            catch(Exception e){
                Log.e("Not working!", e.getMessage() );
            }

            return "Done";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            currentTemperature.setText("Current temperature: "+currentTemp+ "°C");
            minTemperature.setText("Minimum temperature: "+min+ "°C");
            maxTemperature.setText("Maximum temperature: "+max+ "°C");
            uv.setText("UV Rating: " + uvRating);
            weatherTemp.setImageBitmap(currentWeather);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery fq = new ForecastQuery();
        fq.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");

        weatherTemp = findViewById(R.id.weatherTemperature);
        currentTemperature = findViewById(R.id.currentTemperature);
        maxTemperature = findViewById(R.id.maxTemperature);
        minTemperature = findViewById(R.id.minTemperature);
        uv = findViewById(R.id.uvRating);
        progressBar = findViewById(R.id.ProgressBar);

        progressBar.setVisibility(View.VISIBLE);

    }
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();   }


    private static class HTTPUtils {
        public static Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        public  static Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

    }

}
