package com.mydeliveries.toastit.model;

public class NavItem {
	
private static String page,mobi;

	


	public NavItem() {
	}

	public NavItem(String page, String mobi) {
		
		NavItem.page = page;
		NavItem.mobi = mobi;
	
		
	}

	
	public  String getPage() {
		return page;
	}

	public  void setPage(String page) {
		NavItem.page = page;
	}

	public  String getMobi() {
	return mobi;
	}

	public  void setMobi(String mobi) {
	NavItem.mobi = mobi;
	}





}
