package se.lu.ics.models;

import java.util.HashSet;
import java.util.Set;

import se.lu.ics.data.SupplierDAO;

public class Supplier {
    private String SupplierId;
    private String SupplierName;

    // Constructor
    public Supplier(String SupplierId, String SupplierName) {
        this.SupplierId = SupplierId;
        this.SupplierName = SupplierName;
    }

    public Supplier() {
    }

    // Getters

    public String getSupplierId() {
        return SupplierId;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    // Setters

    public void setSupplierId(String SupplierId) {
        this.SupplierId = SupplierId;
    }

    public void setSupplierName(String SupplierName) {
        this.SupplierName = SupplierName;
    }


    // Methods

    //generate unique id
    public static String generateId() {
        Set<String> usedIds = new HashSet<>();
        
        // Iterate through the existing suppliers to collect used IDs
        for (Supplier supplier : SupplierDAO.getActiveAndInActive()) {
            usedIds.add(supplier.getSupplierId());
        }
    
        // Find the first gap in the sequence
        for (int i = 1; i <= usedIds.size() + 1; i++) {
            String idToCheck = "S" + i;
            if (!usedIds.contains(idToCheck)) {
                return idToCheck;
            }
        }
        return null;
    }
    
}
