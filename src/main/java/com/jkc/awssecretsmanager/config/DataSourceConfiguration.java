package com.jkc.awssecretsmanager.config;


import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author jyoti.chabria
 */

@Configuration
public class DataSourceConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfiguration.class);
    @Value("${spring.aws.secretsmanager.secretName}")
    private String secretName;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties appDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource getDataSource() {

        AwsConfig awsConfig = new AwsConfig();
        AWSSecretsManager awsSecretsManager = awsConfig.awsSecretsManager;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode secretsJson = null;

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId(secretName);
        GetSecretValueResult getSecretValueResponse = null;

        try {
            getSecretValueResponse = awsSecretsManager.getSecretValue(getSecretValueRequest);

        } catch (ResourceNotFoundException e) {
            log.error("The requested secret " + secretName + " was not found");
        } catch (InvalidRequestException e) {
            log.error("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            log.error("The request had invalid params: " + e.getMessage());
        }

        if (getSecretValueResponse == null) {
            return null;
        }

        // Decrypted secret using the associated KMS CMK
        String secret = getSecretValueResponse.getSecretString();
        if (secret == null) {
            log.error("The Secret String returned is null");
            return null;
        }
        try {
            secretsJson = objectMapper.readTree(secret);
        } catch (IOException e) {
            log.error("Exception while retreiving secret values: " + e.getMessage());
        }

        System.out.println("Secrets json - " + secretsJson);
        String host = secretsJson.get("host").textValue();
        String port = secretsJson.get("port").textValue();
        String dbname = secretsJson.get("dbname").textValue();
        String username = secretsJson.get("username").textValue();
        String password = secretsJson.get("password").textValue();
        appDataSourceProperties().setUrl("jdbc:mysql://" + host + ":" + port + "/" + dbname
            + "?allowPublicKeyRetrieval=true&useSSL=false");
        appDataSourceProperties().setUsername(username);
        appDataSourceProperties().setPassword(password);

        return appDataSourceProperties().initializeDataSourceBuilder().build();
    }
}