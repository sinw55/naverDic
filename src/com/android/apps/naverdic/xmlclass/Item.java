package com.android.apps.naverdic.xmlclass;

import java.util.ArrayList;
import java.util.List;


public class Item {
	List<RNum> rns;
	
	public Item() {
		rns = new ArrayList<RNum>();
	}

	public List<RNum> getRns() {
		return rns;
	}

	public void setRns(List<RNum> rns) {
		this.rns = rns;
	}
	
	public void addRNum(RNum rn) {
		rns.add(rn);
	}
	
	
}
