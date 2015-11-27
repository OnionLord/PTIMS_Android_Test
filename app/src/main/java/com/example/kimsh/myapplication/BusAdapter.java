package com.example.kimsh.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by KIMSH on 2015-08-22.
 */
public class BusAdapter extends BaseAdapter {

    private ArrayList<String> data;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String station;
    StringBuilder sBuffer;
    String json;
    String result ="";
    private TextView busstatus;

    class ViewHolder
    {
        public TextView headerText;
        public TextView busContent;
    }


    class BusThread extends Thread
    {

        private String lineno;
        private Handler handler;

        public BusThread(String lineno, Handler handler)
        {
            this.lineno = lineno;
            this.handler = handler;
        }

        @Override
        public void run()
        {

            while(true)
            {
                ArrayList<BusData> buses = getBuses(lineno, station);



                for(int i = 0 ; i < buses.size() ; i ++)
                {
                    BusData thisData = buses.get(i);
                    String busStateurl="http://192.168.0.6:8080/data/getData.jsp?busno="+thisData.busno+"&judge=yes";

                    Log.i("EE", busStateurl);
                    sBuffer = new StringBuilder();
                    try
                    {
                        URL url = new URL(busStateurl);
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                        if(conn != null)
                        {
                            conn.setConnectTimeout(20000);
                            conn.setUseCaches( false);

                            //데이터를 정상적으로 받으면
                            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
                                InputStreamReader isr
                                        = new InputStreamReader(conn.getInputStream());
                                //BufferedReader로 변환시킴(InputStreamReader는 잘 안쓴다)
                                BufferedReader br =
                                        new BufferedReader(isr);

                                while( true){
                                    String line = br.readLine();
                                    if(line == null)
                                        break;
                                    sBuffer.append(line);
                                }
                                br.close();
                                conn.disconnect();
                            }
                        }
                        json = sBuffer.toString();
                        Log.i("EE",json);
                    }
                    catch(Exception e)
                    {
                        json = "";
                    }
                    try
                    {
                        JSONObject obj = new JSONObject(json);

                        String judge = obj.getString("judge");
                        double co2 = obj.getDouble("co2");
                        double temperature = obj.getDouble("temperature");
                        double humidity = obj.getDouble("humidity");

                        if(judge.equals("congestion"))
                        {
                            judge = "혼잡";
                        }
                        else
                        {
                            judge = "여유 있음";
                        }


                        result += "차량번호 : " + thisData.getBusno()+"\n";
                        /*if(thisData.getLeftStation() == 2)
                        {
                            result += "전전\n";

                        }
                        else if(thisData.getLeftStation() == 1)
                        {
                            result += "전\n";
                        }
                        else if(thisData.getLeftStation() == 0)
                        {
                            result += "진입중\n";
                        }
                        else
                        {
                            result += thisData.getNowStation() + "\n";
                        }*/
                        result += judge + "("+ temperature + "°C, "+ humidity + "%, " + co2 +"ppm)";
                        result += "\n";
                        if(i < buses.size() - 1)
                            result += "\n";

                    }
                    catch(Exception e)
                    {

                    }
                }
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e)
                {

                }

                //
            }


        }
    }


    public ArrayList<BusData> getBuses(String lineno, String station)
    {
        return new SampleBusData().getBuses();
    }


    public BusAdapter(ArrayList<String> data, Context mContext, String station)
    {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.station = station;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View v = view;

        String title = data.get(position);

        final ViewHolder vh;

        if(v == null)
        {
            vh = new ViewHolder();
            v = mLayoutInflater.inflate(R.layout.view_row, viewGroup, false);

            vh.busContent = (TextView)v.findViewById(R.id.bus_content);
            vh.headerText = (TextView)v.findViewById(R.id.bus_title);

            v.setTag(vh);
        }
        else
        {
            vh = (ViewHolder)v.getTag();
        }

        vh.headerText.setText(title);
        busstatus = vh.busContent;

        Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what == 0)
                    vh.busContent.setText(result);
                result = "";

            }
        };


        if(position != 0)
        {
            vh.busContent.setVisibility(View.GONE);

        }
        else
        {
            BusThread thread = new BusThread("706", handler);
            thread.start();
        }

        return v;
    }
}
