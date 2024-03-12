package com.assetsense.assetsoft.dto;

import java.io.Serializable;

public class LookupDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private long lookupId;
	private long catId;
	private String value;

	public LookupDTO() {
	}

	public long getLookupId() {
		return lookupId;
	}

	public void setLookupId(long lookupId) {
		this.lookupId = lookupId;
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

	@Override
	public String toString() {
		return "LookupDTO [lookupId=" + lookupId + ", catId=" + catId + ", value=" + value + "]";
	}

}
