package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.assetsense.assetsoft.domain.Lookup;

public class LookupDao {
	HibernateTemplate template;

	public HibernateTemplate getTemplate() {
		return template;
	}

	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}

	// method to add lookup
	public void saveLookup(Lookup lookup) {
		template.save(lookup);
	}

	// method to update lookup
	public void updateLookup(Lookup lookup) {
		template.save(lookup);
	}

	// method to delete lookup
	public void deleteLookup(Lookup lookup) {
		template.delete(lookup);
	}

	// method to return one lookup of given id
	public Lookup getLookupById(long id) {
		Lookup lookup = (Lookup) template.get(Lookup.class, id);
		return lookup;
	}

	// Method to return lookup of given email
	public Lookup getLookupByEmail(String email) {
		Lookup lookup = (Lookup) template.get(Lookup.class, email);
		return lookup;
	}

	// method to return all lookups
	public List<Lookup> getLookups() {
		List<Lookup> lookups = new ArrayList<Lookup>();
		lookups = template.loadAll(Lookup.class);
		return lookups;
	}
}
