package com.example.io.tomcat.servlet;


import com.example.io.tomcat.http.GPRequest;
import com.example.io.tomcat.http.GPResponse;
import com.example.io.tomcat.http.GPServlet;

public class SecondServlet extends GPServlet {

	public void doGet(GPRequest request, GPResponse response) throws Exception {
		this.doPost(request, response);
	}

	public void doPost(GPRequest request, GPResponse response) throws Exception {
		response.write("This is Second Serlvet");
	}

}
