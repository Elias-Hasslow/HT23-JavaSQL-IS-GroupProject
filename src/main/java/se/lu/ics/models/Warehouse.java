package se.lu.ics.models;

import java.util.HashSet;
import java.util.Set;

import se.lu.ics.data.WarehouseDAO;

public class Warehouse {
    private String WarehouseId;
    private String WarehouseName;
    private int MaxCapacity;

    // Constructor

    public Warehouse(String WarehouseId, String WarehouseName, int maxCapacity) {
        this.WarehouseId = WarehouseId;
        this.WarehouseName = WarehouseName;
    }

    public Warehouse() {
    }

    // Getters

    public String getWarehouseId() {
        return WarehouseId;
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public int getMaxCapacity() {
        return MaxCapacity;
    }




    // Setters

    public void setWarehouseId(String WarehouseId) {
        this.WarehouseId = WarehouseId;
    }

    public void setWarehouseName(String WarehouseName) {
        this.WarehouseName = WarehouseName;
    }

    public void setMaxCapacity(int MaxCapacity) {
        this.MaxCapacity = MaxCapacity;
    }

    // Methods

       public static String generateId() {
        Set<String> usedIds = new HashSet<>();
        
        // Iterate through the existing suppliers to collect used IDs
        for (Warehouse warehouse : WarehouseDAO.getWarehouses()) {
            usedIds.add(warehouse.getWarehouseId());
        }
    
        // Find the first gap in the sequence
        for (int i = 1; i <= usedIds.size() + 1; i++) {
            String idToCheck = "W" + i;
            if (!usedIds.contains(idToCheck)) {
                return idToCheck;
            }
        }
        return null;
    }
}
