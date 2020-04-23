package com.gittors.gateway;

import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

public class HttpTest {

	public static void main(String[] args) {
		//https://projectreactor.io/docs/netty/release/reference/index.html#_connect
		/// HttpClient client = HttpClient.create().baseUrl("http://baidu.com");
		for(int i=0;i<1000;i++) {
			 HttpClientResponse response =
		                HttpClient.create()                   
		                          .get()                      
		                          .uri("http://localhost:8081/api/test") 
		                          .response()                 
		                          .block();
			 
			System.out.println(response.status().code());
		}
		
	}

}
