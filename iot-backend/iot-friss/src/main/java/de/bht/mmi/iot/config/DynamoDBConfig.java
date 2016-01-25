package de.bht.mmi.iot.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.apache.commons.lang3.StringUtils;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:app.properties")
@EnableDynamoDBRepositories(basePackages = "de.bht.mmi.iot.repository")
public class DynamoDBConfig {

    @Autowired
    private Environment env;

    private String amazonDynamoDBEndpoint;

    private String amazonAWSAccessKey;

    private String amazonAWSSecretKey;

    private Long readCapacityUnitsPerSecond;

    private Long writeCapacityUnitsPerSecond;

    private ProvisionedThroughput provisionedThroughput;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        final AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
        if (StringUtils.isNotEmpty(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }
        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

    @Bean
    public DynamoDB dynamoDB() {
        return new DynamoDB(amazonDynamoDB());
    }

    @Bean
    public DynamoDBMapper dynamoDbMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @PostConstruct
    private void init() {
        amazonDynamoDBEndpoint = env.getRequiredProperty("amazon.dynamodb.endpoint");
        amazonAWSAccessKey = env.getRequiredProperty("amazon.aws.accesskey");
        amazonAWSSecretKey = env.getRequiredProperty("amazon.aws.secretkey");

        readCapacityUnitsPerSecond = Long.valueOf(env.getRequiredProperty("amazon.dynamodb.read_capacity_units"));
        writeCapacityUnitsPerSecond = Long.valueOf(env.getRequiredProperty("amazon.dynamodb.write_capacity_units"));
        provisionedThroughput = new ProvisionedThroughput(readCapacityUnitsPerSecond, writeCapacityUnitsPerSecond);
    }

    public Long getReadCapacityUnitsPerSecond() {
        return readCapacityUnitsPerSecond;
    }

    public Long getWriteCapacityUnitsPerSecond() {
        return writeCapacityUnitsPerSecond;
    }

    public ProvisionedThroughput getProvisionedThroughput() {
        return provisionedThroughput;
    }

}