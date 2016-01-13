package com.mmadapps.practiseproject.Utils;

import android.util.Log;

import com.mmadapps.practiseproject.Flight.beans.flightdetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gangadhar.g on 1/10/2016.
 */
public class JsonParser {

    private String getObjectvalue(JSONObject listObj, String keyOfObject) {
        try {
            try {
                String mKeyValue = listObj.getString(keyOfObject).toString();
                if (mKeyValue == null || mKeyValue.length() == 0 || mKeyValue.equals("null")) {
                } else {
                    return mKeyValue;
                }
            } catch (NullPointerException nullExp) {
            }
        } catch (JSONException e) {
        }
        return "";
    }




    public ArrayList<flightdetails> parsingflightdetails(String jResult) {
        ArrayList<flightdetails> stockReports = null;
        try {
            JSONObject Obj = new JSONObject(jResult);
            String isError = getObjectvalue(Obj, "IsError");
            if (isError.equalsIgnoreCase("false")) {
                JSONObject responseobj= Obj.getJSONObject("ResponseObject");
                JSONArray responearray=responseobj.getJSONArray("scheduledFlights");

                if (responearray == null || responearray.length() == 0) {
                    Log.e("StockReport Parsing", "ResponseArray is null");
                } else {
                    int len = responearray.length();
                    stockReports = new ArrayList<>();
                    for (int i = 0; i < len; i++) {
                        JSONObject stockObject = responearray.getJSONObject(i);
                        flightdetails sr = new flightdetails();
                        sr.setMcarrierFsCode(getObjectvalue(stockObject, "carrierFsCode"));
                        sr.setMflightNumber(getObjectvalue(stockObject, "flightNumber"));
                        sr.setMdepartureAirportFsCode(getObjectvalue(stockObject, "departureAirportFsCode"));
                        sr.setMarrivalAirportFsCode(getObjectvalue(stockObject, "arrivalAirportFsCode"));
                        sr.setMdepartureTime(getObjectvalue(stockObject, "departureTime"));
                        sr.setMarrivalTime(getObjectvalue(stockObject, "arrivalTime"));
                        stockReports.add(sr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockReports;
    }

}
