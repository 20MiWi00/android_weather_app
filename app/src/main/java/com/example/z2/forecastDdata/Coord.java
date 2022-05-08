package com.example.z2.forecastDdata;

import com.google.gson.annotations.SerializedName;

public class Coord{

	@SerializedName("lon")
	private double lon;

	@SerializedName("lat")
	private double lat;

	public void setLon(int lon){
		this.lon = lon;
	}

	public double getLon(){
		return lon;
	}

	public void setLat(int lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}
}