package com.romu2000.wetterApp;

import com.romu2000.wetterApp.model.LngLatModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
class WetterAppApplicationTests {

	@Test
	void contextLoads() throws JSONException, IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://maps.googleapis.com/maps/api/geocode/json?address=Stuttgart&key=AIzaSyBs3AMqs8KkY-iyJuTcVAJtOidZBQawx_Q"))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		JSONObject stadtZuLngLat = new JSONObject(response.body());
		JSONArray results = stadtZuLngLat.getJSONArray("results");
		JSONObject nuller = results.getJSONObject(0);
		JSONObject geometry =  nuller.getJSONObject("geometry");
		JSONObject location = geometry.getJSONObject("location");
		Double lng = (Double) location.get("lng");
		Double lat = (Double) location.get("lat");

		LngLatModel stadt = new LngLatModel();
		stadt.setLat(BigDecimal.valueOf(lat));
		stadt.setLng(BigDecimal.valueOf(lng));
		System.out.println(stadt.getLat() + "  :   " + stadt.getLng());
	}

}
