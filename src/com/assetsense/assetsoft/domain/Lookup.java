package com.assetsense.assetsoft.domain;

import java.io.Serializable;

public class Lookup implements Serializable {
	private static final long serialVersionUID = 1L;
	private long lookupId;
	private long catId;
	private String value;
	
	public Lookup() {}


	public long getLookupId() {
		return lookupId;
	}


	public void setLookupId(long id) {
		this.lookupId = id;
	}


	public long getCatId() {
		return catId;
	}


	public void setCatId(long catId) {
		this.catId = catId;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	
}
