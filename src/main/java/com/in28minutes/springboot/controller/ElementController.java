package com.in28minutes.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.springboot.model.Element;
import com.in28minutes.springboot.service.ElementService;

@RestController
public class ElementController {

	@Autowired
	private ElementService elementService;

	@GetMapping("/elements/{elementSymbol}")
	public Element retrieveElementForSymbol(@PathVariable String elementSymbol) {
		return elementService.retrieveStudent(elementSymbol);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/elements")
	public List<Element> retrieveAllElements() {
		return elementService.retrieveAllElements();
	}

}
