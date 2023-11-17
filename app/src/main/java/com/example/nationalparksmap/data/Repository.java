package com.example.nationalparksmap.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nationalparksmap.controller.AppController;
import com.example.nationalparksmap.model.Activities;
import com.example.nationalparksmap.model.EntranceFees;
import com.example.nationalparksmap.model.Images;
import com.example.nationalparksmap.model.OperatingHours;
import com.example.nationalparksmap.model.Park;
import com.example.nationalparksmap.model.StandardHours;
import com.example.nationalparksmap.model.Topics;
import com.example.nationalparksmap.utils.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    static List<Park> parkList = new ArrayList<>();

    public static void getParks(final AsyncResponse callBack, String stateCode) {
        String url = util.getParksUrl(stateCode);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url , null,
                        response -> {

                            try {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Park park = new Park();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    park.setId(jsonObject.getString("id"));
                                    park.setFullName(jsonObject.getString("fullName"));
                                    park.setLatitude(jsonObject.getString("latitude"));
                                    park.setLongitude(jsonObject.getString("longitude"));
                                    park.setParkCode(jsonObject.getString("parkCode"));
                                    park.setStates(jsonObject.getString("states"));

                                    JSONArray imageList = jsonObject.getJSONArray("images");
                                    List<Images> list = new ArrayList<>();
                                    for(int j=0;j<imageList.length();j++){
                                        Images images = new Images();
                                        images.setCredit(imageList.getJSONObject(j).getString("credit"));
                                        images.setTitle(imageList.getJSONObject(j).getString("title"));
                                        images.setCaption(imageList.getJSONObject(j).getString("caption"));
                                        images.setAltText(imageList.getJSONObject(j).getString("altText"));
                                        images.setUrl(imageList.getJSONObject(j).getString("url"));

                                        list.add(images);
                                    }
                                    park.setImages(list);

                                    park.setWeatherInfo(jsonObject.getString("weatherInfo"));
                                    park.setName(jsonObject.getString("name"));
                                    park.setDesignation(jsonObject.getString("designation"));
                                    park.setDescription(jsonObject.getString("description"));

                                    // Activities
                                    JSONArray activityList = jsonObject.getJSONArray("activities");
                                    List<Activities> activitiesList= new ArrayList<>();
                                    for(int j=0;j<activityList.length();j++){
                                        Activities activities =new Activities();
                                        activities.setId(activityList.getJSONObject(j).getString("id"));
                                        activities.setName(activityList.getJSONObject(j).getString("name"));

                                        activitiesList.add(activities);
                                    }
                                    park.setActivities(activitiesList);

                                    // Topics
                                    JSONArray topicList = jsonObject.getJSONArray("topics");
                                    List<Topics> topicsList= new ArrayList<>();
                                    for(int j=0;j<topicList.length();j++){
                                        Topics topics =new Topics();
                                        topics.setId(topicList.getJSONObject(j).getString("id"));
                                        topics.setName(topicList.getJSONObject(j).getString("name"));
                                        topicsList.add(topics);
                                    }
                                    park.setTopics(topicsList);

                                    // Entrance Fee
                                    JSONArray feesList = jsonObject.getJSONArray("entranceFees");
                                    List<EntranceFees> entFeeList= new ArrayList<>();
                                    for(int j=0;j<feesList.length();j++){
                                        EntranceFees entranceFees = new EntranceFees();
                                        entranceFees.setDescription(feesList.getJSONObject(j).getString("description"));
                                        entranceFees.setTitle(feesList.getJSONObject(j).getString("title"));
                                        entranceFees.setCost(feesList.getJSONObject(j).getString("cost"));
                                        entFeeList.add(entranceFees);
                                    }
                                    park.setEntranceFees(entFeeList);

                                    // OP Hours
                                    JSONArray opHours = jsonObject.getJSONArray("operatingHours");
                                    List<OperatingHours> operatingHours =new ArrayList<>();
                                    for (int j = 0; j < opHours.length(); j++) {
                                        OperatingHours op = new OperatingHours();
                                        op.setDescription(opHours.getJSONObject(j).getString("description"));
                                        StandardHours standardHours = new StandardHours();
                                        JSONObject hours = opHours.getJSONObject(j).getJSONObject("standardHours");

                                        standardHours.setMonday(hours.getString("monday"));
                                        standardHours.setTuesday(hours.getString("tuesday"));
                                        standardHours.setWednesday(hours.getString("wednesday"));
                                        standardHours.setThursday(hours.getString("thursday"));
                                        standardHours.setFriday(hours.getString("friday"));
                                        standardHours.setSaturday(hours.getString("saturday"));
                                        standardHours.setSunday(hours.getString("sunday"));

                                        op.setStandardHours(standardHours);
                                        operatingHours.add(op);
                                    }
                                    park.setOperatingHours(operatingHours);

                                    park.setDirectionsInfo(jsonObject.getString("directionsInfo"));

                                    parkList.add(park);

                                }
                                if (callBack != null) {
                                    callBack.processPark(parkList);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }, Throwable::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}

