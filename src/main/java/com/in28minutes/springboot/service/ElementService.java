package com.in28minutes.springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.in28minutes.springboot.model.Element;

@Component(value = "elementService")
public class ElementService {

	private static List<Element> elements = new ArrayList<>();

	public static List<Element> getStudents() {
		return elements;
	}

	public static void setStudents(List<Element> elements) {
		ElementService.elements = elements;
	}

	static {
		// Initialize Data

		Element element1 = new Element("1", "Hydrogen", 1.0079, "H");
		Element element2 = new Element("2", "Helium", 4.0026, "He");
		Element element3 = new Element("3", "Lithium", 6.941, "Li");
		Element element4 = new Element("4", "Beryllium", 9.0122, "Be");
		Element element5 = new Element("5", "Boron", 10.811, "B");
		Element element6 = new Element("6", "Carbon", 12.0107, "C");
		Element element7 = new Element("7", "Nitrogen", 14.0067, "N");
		Element element8 = new Element("8", "Oxygen", 15.9994, "O");
		Element element9 = new Element("9", "Fluorine", 18.9984, "F");
		Element element10 = new Element("10", "Neon", 20.1797, "Ne");
		Element element11 = new Element("11", "Sodium", 22.9897, "Na");
		Element element12 = new Element("12", "Magnesium", 24.305, "Mg");
		Element element13 = new Element("13", "Aluminum", 26.9815, "Al");
		Element element14 = new Element("14", "Silicon", 28.0855, "Si");
		Element element15 = new Element("15", "Phosphorus", 30.9738, "P");
		Element element16 = new Element("16", "Sulfur", 32.065, "S");
		Element element17 = new Element("17", "Chlorine", 35.453, "Cl");
		Element element18 = new Element("18", "Argon", 39.948, "Ar");
		Element element19 = new Element("19", "Potassium", 39.0983, "K");
		Element element20 = new Element("20", "Calcium", 40.078, "Ca");

		elements.add(element1);
		elements.add(element2);
		elements.add(element3);
		elements.add(element4);
		elements.add(element5);
		elements.add(element6);
		elements.add(element7);
		elements.add(element8);
		elements.add(element9);
		elements.add(element10);
		elements.add(element11);
		elements.add(element12);
		elements.add(element13);
		elements.add(element14);
		elements.add(element15);
		elements.add(element16);
		elements.add(element17);
		elements.add(element18);
		elements.add(element19);
		elements.add(element20);
	}

	public List<Element> retrieveAllElements() {
		return elements;
	}

	public Element retrieveStudent(String elementSymbol) {
		for (Element element : elements) {
			if (element.getSymbol().equals(elementSymbol)) {
				return element;
			}
		}
		return null;
	}
}