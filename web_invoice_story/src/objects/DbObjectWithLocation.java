package objects;

import web_invoice_story.Application;

public abstract class DbObjectWithLocation extends DbObject {
	
	private long locationId;
	private boolean locationMatters = false;
	private boolean locationThroughCustomer = false;

	public long getLocationId() {
		return locationId == 0 ? 1 : locationId	;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	
	public boolean doesLocationMatter() {
		return locationMatters;
	}

	public void setLocationMatters(boolean locationMatters) {
		this.locationMatters = locationMatters;
	}

	public boolean isLocationThroughCustomer() {
		return locationThroughCustomer;
	}

	public void setLocationThroughCustomer(boolean locationThroughCustomer) {
		this.locationThroughCustomer = locationThroughCustomer;
	}
	
	@Override
	public String getSearchQuery() {
		// return subclass's correct DB search query 
		int selectedLocation = Application.getSettings().getCurrentLocation();
		
		// TODO these location queries probably need to go in the DbObjects themselves
			// with an if(currentLocation != 0) {[add this to query]} or something like that
			// there would be a copy of the query in each Object (potentially duplicating code)
			// but some objects, like ProjectSite, need their own special version of the locationId code
			// /and/ there would be no silly boolean to check, which i sorta feel like is
			// "against" "good" "OO practices" (but what are rules and guidelines for if not being broken,
			// i mean really?)
		if (selectedLocation != 0 && doesLocationMatter()) {	// TODO with this class, I can remove this boolean check and just worry about the
			// modify search query to only show results from selected location		// isLocationThroughCustomer() query below
			StringBuilder searchQueryWithLoc = new StringBuilder(searchQuery);
			
			// add location details
			searchQueryWithLoc.insert(searchQueryWithLoc.indexOf("WHERE") - 1,	// NOTE TODO why am I joining the entire storeLocation table on here?
					"\nINNER JOIN web_invoice_story.storeLocation on storeLocation.locationId = "
					+ this.getTableName() + ".locationId"
					+ " AND storeLocation.locationId = " + selectedLocation);
//			searchQueryWithLoc.insert(searchQueryWithLoc.indexOf(";"), " AND sl.locationId = " + selectedLocation);	// CONT i tried this at first
			searchQuery = searchQueryWithLoc.toString();
		} else if (selectedLocation != 0 && isLocationThroughCustomer()) {
			// modify search query to only show results from selected location
			StringBuilder searchQueryWithLoc = new StringBuilder(searchQuery);
			
			// add location details
			searchQueryWithLoc.insert(searchQueryWithLoc.indexOf("WHERE") - 1,
					"\nINNER JOIN web_invoice_story.storeLocation on storeLocation.locationId = customer.locationId"
					+ " AND storeLocation.locationId = " + selectedLocation);
			searchQuery = searchQueryWithLoc.toString();
		}
		
		return super.getSearchQuery();
	}

}
