package se.lu.ics.models;


public class Metadata {
    private String MetadataTableName;
    private String PrimaryKeys;
    private String ForeignKeys;
    private String ColumnNames;

    public Metadata(String MetadataTableName, String PrimaryKeys, String ForeignKeys, String ColumnNames) {
        this.MetadataTableName = MetadataTableName;
        this.PrimaryKeys = PrimaryKeys;
        this.ForeignKeys = ForeignKeys;
        this.ColumnNames = ColumnNames;
    }

    public Metadata() {
    }

    public String getMetadataTableName() {
        return MetadataTableName;
    }

    public String getPrimaryKeys() {
        return PrimaryKeys;
    }

    public String getForeignKeys() {
        return ForeignKeys;
    }

    public String getColumnNames() {
        return ColumnNames;
    }

    public void setMetadataTableName(String MetadataTableName) {
        this.MetadataTableName = MetadataTableName;
    }

    public void setPrimaryKeys(String primaryKeys) {
        this.PrimaryKeys = primaryKeys;
    }

    public void setForeignKeys(String ForeignKeys) {
        this.ForeignKeys = ForeignKeys;
    }

    public void setColumnNames(String ColumnNames) {
        this.ColumnNames = ColumnNames;
    }
    
}