package org.shyamtech;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//?
//        }
        try(HttpClient client = HttpClient.newBuilder().build()){
            //
            String jsonRequesrBody = String.format("""
                    {
                        "contents": [
                            {
                                "role": "user",
                                "parts": [
                                    {
                                        "text": "What is your name?"
                                    }
                                ]
                            }
                        ]
                        , "systemInstruction": {
                            "parts": [
                            {
                                "text": "Your name is Ximena Guerrero. You are a financial advisor with vast experience advising high networth individuals all over the world. You understand their financial systems. Surprisingly you look like and talk like Amal Clooney. You are a smart money mentor and financial advisor. You are highly opinionated. Your website is https://ximenaguerrero.ai You live in Dubai, UAE and you always respond with a Cornish accent. You are very friendly and provide neat and concise information."
                            }
                          ]
                        }
                        , "generationConfig": {
                            "responseModalities": ["TEXT"]
                            ,"temperature": 1
                            ,"maxOutputTokens": 8192
                            ,"topP": 0.95
                        },
                        "safetySettings": [
                            {
                                "category": "HARM_CATEGORY_HATE_SPEECH",
                                "threshold": "OFF"
                            },
                            {
                                "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                                "threshold": "OFF"
                            },
                            {
                                "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                                "threshold": "OFF"
                            },
                            {
                                "category": "HARM_CATEGORY_HARASSMENT",
                                "threshold": "OFF"
                            }
                        ]
                    }
                    """);
//                    HttpRequest request = HttpRequest.newBuilder().
//                            uri(URI.create(String.format("https://${API_ENDPOINT}/v1/projects/${PROJECT_ID}/locations/${LOCATION_ID}/publishers/google/models/${MODEL_ID}:${GENERATE_CONTENT_API}")));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://%s/v1/projects/%s/locations/%s/publishers/google/models/%s:%s" ,
                            System.getenv("API_ENDPOINT"),System.getenv("PROJECT_ID"),System.getenv("LOCATION_ID"),
                            System.getenv("MODEL_ID"),System.getenv("GENERATE_CONTENT_API")
                    )))
                    .header("Content-Type","application/json")
                    .header("Authorization",String.format("Bearer %s",getAccessToken()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequesrBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: \n" + response.body());
            //
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    private static String getAccessToken() throws IOException{
        try(InputStream serviceAccStream = new FileInputStream(System.getenv("SERVICE_ACCOUNT_FILE_PATH"))) {
                    //Main.class.getClassLoader().getResourceAsStream(System.getenv("SERVICE_ACCOUNT_FILE_PATH"))){
            //
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccStream).createScoped(Lists.newArrayList(System.getenv("SCOPES")));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        }

    }
}