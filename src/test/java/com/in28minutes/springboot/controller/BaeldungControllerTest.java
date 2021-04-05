package com.in28minutes.springboot.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BaeldungController.class, secure = false)
public class BaeldungControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void retrieveGetHelloDetails() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/hello").accept(MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("The response: "+result.getResponse());
		String expected = "hello";

		assertEquals(expected, result.getResponse().getContentAsString());
	}
	@Test
	public void retrievePostHelloDetails() throws Exception {
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/baeldung")
				.accept(MediaType.ALL_VALUE).content("hello")
				.contentType(MediaType.ALL);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		String expected = "hello";
		assertEquals(expected, response.getContentAsString());
	}
}
