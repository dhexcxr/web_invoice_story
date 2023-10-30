package guiParts;

import java.util.List;
import org.eclipse.swt.widgets.Composite;

import objects.StoreLocation;

public class StoreLocationCombo extends MyCombo{
	
	public StoreLocationCombo(Composite parent, int style) {
		super(parent, style);
		
	}
	
	public StoreLocationCombo(List<StoreLocation> allStoreLocations, Composite parent, int style) {
		super(parent, style);
			// TODO make this better, probably make the All Locations object first
			// probably will entail making the All Locations object a better way in the StoreLocations object itself
		this.add("All Locations");
		this.setData("0", new StoreLocation(0, "All Locations"));
		for (StoreLocation storeLocation : allStoreLocations) {
			this.add(storeLocation.getName() + ": " + storeLocation.getAddress());
			this.setData(Long.toString(storeLocation.getDbPk()), storeLocation);
		}
	}

	public int getSelectedLocationId() {
		int selectedLocationIndex = this.getSelectionIndex();
		return (int) ((StoreLocation) this.getData(Integer.toString(selectedLocationIndex))).getStoreLocationId();
	}
	
}
