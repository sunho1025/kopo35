package AI;

import java.io.*; // 입출력 관련 클래스들
import java.net.*; // URL, HttpURLConnection 등 네트워크 관련 클래스들
import java.util.Scanner; // 사용자 입력을 받기 위한 클래스

//JSON 처리에 필요한 외부 라이브러리 (json-simple)
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class openai_01 {
	//이 메서드는 OpenAI 또는 LM Studio API에 JSON 데이터를 POST로 전송하고, 
	//응답을 문자열로 반환함
	public static String apiTestPost(String sendData) {
		URL url = null;
		String readLine=null;
		StringBuilder buffer = null;
		OutputStream outputStream =null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		HttpURLConnection urlConnection = null;
		
		// 연결/읽기 타임아웃 설정 (30초)
		int connTimeout = 30000;
		int readTimeout = 30000;
		
		// LM Studio 서버의 로컬 주소 (서버 포트는 1234)
		// 인증 토큰: LM Studio에서는 실제 토큰 대신 "Bearer lm-studio" 형식만 요구함
		String apiUrl = "http://localhost:1234/v1/chat/completions";
		String API_KEY = "Bearer "+ "lm-studio";
		
		String retStr="";
		try {
			// 1. URL 객체 생성 및 연결
			url = new URL(apiUrl);
			urlConnection = (HttpURLConnection)url.openConnection();

			// 2. 요청 설정
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(connTimeout);
			urlConnection.setReadTimeout(readTimeout);
			urlConnection.setRequestProperty("Authorization", API_KEY);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setDoOutput(true);
			urlConnection.setInstanceFollowRedirects(true);
			
			// 3. 요청 본문(JSON 데이터) 전송
			outputStream = urlConnection.getOutputStream();
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			bufferedWriter.write(sendData);
			bufferedWriter.flush();
			
			// 4. 응답 수신
			buffer = new StringBuilder();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 정상 응답이 온 경우
				bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
				while((readLine = bufferedReader.readLine()) != null) {
					buffer.append(readLine).append("\n");
				}
				
				System.out.println("응답이 왔습니다.=====");
				// 5. JSON 응답 파싱
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(buffer.toString());
				// 응답에서 "choices[0].message.content" 추출
				JSONArray choices = (JSONArray) jsonObject.get("choices");
				JSONObject first_choices = (JSONObject) choices.get(0);
				JSONObject message = (JSONObject) first_choices.get("message");
				String content = (String) message.get("content");
				retStr = content; // 최종 응답 텍스트 저장
			}
			else {
				// 에러 응답 처리
				retStr = String.format("에러코드 : %d\n 내용 : %s\n",
						urlConnection.getResponseCode(),
						urlConnection.getResponseMessage());
			}
		}
		// 예외 처리
		catch(Exception ex) {
			ex.printStackTrace();
			retStr = "Exception 발생 1";
		}
		finally {
			// 6. 리소스 정리 (자원 누수 방지)
			try {
				if (bufferedWriter != null) {bufferedWriter.close();}
				if (outputStream != null) {outputStream.close();}
				if (bufferedReader != null) {bufferedReader.close();}
			}
			catch(Exception ex) {
				ex.printStackTrace();
				retStr = "Exception 발생 2";
			}
		}
		// 7. 연결 해제
		urlConnection.disconnect();
		return retStr;
	}
	
	//사용자 입력을 OpenAI 요청 형식(JSON)으로 포장해주는 메서드
	public static String reqData(String msg) {

		// 요구하는 메시지 형식에 맞게 JSON 구성
		String send_data = String.format("""
				{
				  "model": "model-identifier",
				  "messages": [
				    {"role": "system", "content": "너는 학습하는 인공지능이야"},
				    {"role": "user", "content": "%s"}
				  ],
				  "temperature": 0.5
				}
				""", msg);
		msg = msg.replaceAll("\n"," ");
		return send_data;
	}
	
	// 메인 실행 메서드: 사용자 입력을 받아 챗봇 API에 요청하고 응답을 출력
	public static void main(String[] args) {
		System.out.printf("= 요청중입니다. ===\n");
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("나 >> ");
			String send_data = reqData(sc.nextLine());
			try {
				System.out.print("챗봇 >> ");
				System.out.printf(apiTestPost(send_data));
				System.out.print("\n");
			}
			catch (Exception e){
				e.printStackTrace();
			}
			if (send_data.equals("exit")) {
				break;
			}
		}
	}
}
