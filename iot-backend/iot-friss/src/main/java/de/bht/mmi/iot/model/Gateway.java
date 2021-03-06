package de.bht.mmi.iot.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import de.bht.mmi.iot.constants.DbConstants;

import javax.validation.constraints.NotNull;

@DynamoDBTable(tableName = DbConstants.TABLENAME_GATEWAY)
public class Gateway {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String owner;

    public Gateway(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public Gateway() { }

    @DynamoDBHashKey(attributeName = DbConstants.ATTRIBUTE_ID)
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gateway)) return false;

        Gateway gateway = (Gateway) o;

        return !(id != null ? !id.equals(gateway.id) : gateway.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
