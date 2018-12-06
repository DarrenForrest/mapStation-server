package com.bonc.microapp.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Demo {

	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		String currSun = dateFm.format(date);
		System.out.println(currSun);
	}
}
