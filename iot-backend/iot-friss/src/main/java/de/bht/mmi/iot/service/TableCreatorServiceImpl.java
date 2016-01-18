package de.bht.mmi.iot.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.Tables;
import de.bht.mmi.iot.constants.DbConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class TableCreatorServiceImpl implements TableCreatorService {

    @Autowired
    private AmazonDynamoDB dynamoDB;

    Logger LOGGER = LoggerFactory.getLogger(TableCreatorServiceImpl.class);

    public void createUserTable() {
        final CreateTableResult createTableResult = dynamoDB.createTable(
                Arrays.asList(new AttributeDefinition("username", ScalarAttributeType.S)),
                DbConstants.TABLENAME_USER,
                Arrays.asList(new KeySchemaElement("username", KeyType.HASH)),
                new ProvisionedThroughput(10L, 10L));
        LOGGER.info(generateTableStatusLogMessage(
                DbConstants.TABLENAME_USER, createTableResult.getTableDescription().getTableStatus()));
    }

    public void createSensorTable() {
        final CreateTableResult createTableResult = dynamoDB.createTable(
                Arrays.asList(new AttributeDefinition("id", ScalarAttributeType.S)),
                DbConstants.TABLENAME_SENSOR,
                Arrays.asList(new KeySchemaElement("id", KeyType.HASH)),
                new ProvisionedThroughput(10L, 10L));
        LOGGER.info(generateTableStatusLogMessage(
                DbConstants.TABLENAME_SENSOR, createTableResult.getTableDescription().getTableStatus()));
    }

    @Override
    public void createGatewayTable() {
        final CreateTableResult createTableResult = dynamoDB.createTable(
                Arrays.asList(new AttributeDefinition("id", ScalarAttributeType.S)),
                DbConstants.TABLENAME_GATEWAY,
                Arrays.asList(new KeySchemaElement("id", KeyType.HASH)),
                new ProvisionedThroughput(10L, 10L));
        LOGGER.info(generateTableStatusLogMessage(
                DbConstants.TABLENAME_GATEWAY, createTableResult.getTableDescription().getTableStatus()));
    }

    @Override
    public void createClusterTable() {
        final CreateTableResult createTableResult = dynamoDB.createTable(
                Arrays.asList(new AttributeDefinition("id", ScalarAttributeType.S)),
                DbConstants.TABLENAME_CLUSTER,
                Arrays.asList(new KeySchemaElement("id", KeyType.HASH)),
                new ProvisionedThroughput(10L, 10L));
        LOGGER.info(generateTableStatusLogMessage(
                DbConstants.TABLENAME_CLUSTER, createTableResult.getTableDescription().getTableStatus()));
    }

    public ArrayList<String> getTableNames() {
        final ListTablesResult listTablesResult = dynamoDB.listTables();
        ArrayList<String> tableNames = new ArrayList<String>();
        for (String tableName : listTablesResult.getTableNames()) {
            tableNames.add(tableName);
        }
        return tableNames;
    }

    public void deleteTable(String tableName) {
        if (Tables.doesTableExist(dynamoDB, tableName)) {
            final DeleteTableResult deleteTableResult = dynamoDB.deleteTable(tableName);
            LOGGER.info("Table: " + tableName + " deleted -- Table status: " + deleteTableResult.getTableDescription().getTableStatus());
        } else {
            LOGGER.info("Table: " + tableName + " doesn't exist");

        }
    }

    private String generateTableStatusLogMessage(String tableName, String status) {
        return String.format("Table '%s' status: %s", tableName, status);
    }

}