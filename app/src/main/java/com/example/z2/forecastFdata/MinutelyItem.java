package com.example.z2.forecastFdata;

import java.io.Serializable;

public class MinutelyItem implements Serializable {
	private int dt;
	private int precipitation;

	public void setDt(int dt){
		this.dt = dt;
	}

	public int getDt(){
		return dt;
	}

	public void setPrecipitation(int precipitation){
		this.precipitation = precipitation;
	}

	public int getPrecipitation(){
		return precipitation;
	}
}
