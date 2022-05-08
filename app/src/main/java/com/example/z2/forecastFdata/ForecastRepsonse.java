package com.example.z2.forecastFdata;

import java.util.List;

public class ForecastRepsonse {
	private String timezone;
	private int timezoneOffset;
	private List<DailyItem> daily;
	private double lon;
	private List<MinutelyItem> minutely;
	private double lat;

	public void setTimezone(String timezone){
		this.timezone = timezone;
	}

	public String getTimezone(){
		return timezone;
	}

	public void setTimezoneOffset(int timezoneOffset){
		this.timezoneOffset = timezoneOffset;
	}

	public int getTimezoneOffset(){
		return timezoneOffset;
	}

	public void setDaily(List<DailyItem> daily){
		this.daily = daily;
	}

	public List<DailyItem> getDaily(){
		return daily;
	}

	public void setLon(double lon){
		this.lon = lon;
	}

	public double getLon(){
		return lon;
	}

	public void setMinutely(List<MinutelyItem> minutely){
		this.minutely = minutely;
	}

	public List<MinutelyItem> getMinutely(){
		return minutely;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}
}