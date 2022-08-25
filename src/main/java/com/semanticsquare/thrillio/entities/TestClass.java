package com.semanticsquare.thrillio.entities;

public class TestClass {
	private TestClass() {}

	public static TestClass getInstance() {
		return new TestClass();
	}

	public static void main(String[] args) {
		TestClass testClass = TestClass.getInstance();

		System.out.println(testClass);
	}
}

