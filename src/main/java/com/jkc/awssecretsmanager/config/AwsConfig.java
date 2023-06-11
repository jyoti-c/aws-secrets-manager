package com.jkc.awssecretsmanager.config;

import static com.amazonaws.services.secretsmanager.model.GetSecretValueRequest.*;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.CreateSecretResult;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jyoti.chabria
 */
public class AwsConfig {

    public AWSSecretsManager awsSecretsManager;


    public AwsConfig() {
        AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(
            new BasicAWSCredentials("<YOUR_AWS_ACCESS_KEY>", "<YOUR_AWS_SECRET_KEY>"));
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard()
            .withCredentials(credentials);
        awsSecretsManager = clientBuilder.build();
    }

    /**
     * Create a new AWS secret
     *
     * @param secretName String secret name
     * @param secretValue String secret value
     * @return String secret ARN
     */
    public String createSecret(String secretName, String secretValue) {
        String arn = "";
        try {
            CreateSecretRequest request = new CreateSecretRequest()
                .withName(secretName)
                .withSecretString(secretValue);

            CreateSecretResult result = awsSecretsManager.createSecret(request);
            arn = result.getARN();
            System.out.println("Created secret with ARN: " + arn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arn;
    }

    /**
     * Get AWS secret
     *
     * @param secretName String secret name
     * @param secretValue String secret value
     * @return boolean
     */
    public boolean getSecret(String secretName, String secretValue) {
        try {
            GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(
                secretName);
            GetSecretValueResult getSecretValueResult = awsSecretsManager.getSecretValue(
                getSecretValueRequest);
            String secret = getSecretValueResult.getSecretString();
            if (secret.equals(secretValue)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
