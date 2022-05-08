package com.example.z2.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GeoResponse {

	@SerializedName("Response")
	private List<ResponseItem> response;

	public void setResponse(List<ResponseItem> response){
		this.response = response;
	}

	public List<ResponseItem> getResponse(){
		return response;
	}
}