package de.mcdorians.codebase.locbano;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONResponse {
	
	public static String getJSONResponse(Object obj){
		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();		
		String json = gson.toJson(obj);
		return json;
	}

}
