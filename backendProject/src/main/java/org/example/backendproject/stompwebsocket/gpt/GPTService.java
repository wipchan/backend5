package org.example.backendproject.stompwebsocket.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class GPTService {

    //json문자열 <-> 자바객체, json객체Add commentMore actions
    private final ObjectMapper mapper = new ObjectMapper();

    // gpt api key security 설정
    @Value("${open.api.key}")
    private String openAPI_KEY;

    @Value("${open.model}")
    private String openModel;

    public String gptMessage(String message) throws Exception {

        try {
        //API 호출을 위한 본문 작성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openModel);
        requestBody.put("input", message);


        //http 요청 작성 // gpt 용
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/responses"))
                .header("Authorization", "Bearer " + openAPI_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody))) //본문 삽입
                .build();

        //요청 전송 및 응답 수신
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //응답을 Json으로 파싱
        JsonNode jsonNode = mapper.readTree(response.body());
        System.out.println("gpt 응답 : " + jsonNode);

        //메세지 부분만 추출하여 반환
        // gpt 용

            String gptMessageResponse = jsonNode.get("output").get(0).get("content").get(0).get("text").asText();
            return gptMessageResponse;

        } catch (Exception e) {
            System.out.println(e);
            return "gpt 사용량이 많아 호출 실패";
        }
    }
}
