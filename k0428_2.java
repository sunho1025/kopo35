package k40428;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class k0428_2 { //클래스 선언

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException { // 메인에서 프로그램 시작
		//JDBC 드라이버 클래스를 동적으로 로드함
		//드라이버는 자바 프로그램이 MySQL과 통신할 수 있게 해주는 라이브러리
		//반드시 연결 전에 드라이버를 메모리에 올려야 함
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		//MySQL 서버에 연결 (호스트: localhost, 포트: 33062, DB명: kopoctc, 사용자: root, 비밀번호: kopo35)
		//연결 성공 시 Connection 객체(k35_con)가 생성됨
		Connection k35_con = DriverManager.getConnection("jdbc:mysql://localhost:33062/kopoctc","root","kopo35");
		
		//SQL 구문을 실행할 수 있는 Statement 객체 생성
		//Statement는 SQL문을 DB에 전달하고 결과를 받아오는 기능을 함
		Statement k35_stmt = k35_con.createStatement();

		// 읽을 파일 객체 생성 (CSV 파일 경로 지정)
		File k35_f = new File("C:\\Users\\USER\\한국교통안전공단_전국공영주차장정보.csv");
		// 파일을 읽어오기 위한 BufferedReader 객체 생성
		BufferedReader k35_br = new BufferedReader(new FileReader(k35_f));
		
		String k35_readtxt; // 파일에서 한 줄을 읽어올 변수 선언
		
		// 첫 번째 줄 읽기 (헤더 정보)
		if ((k35_readtxt = k35_br.readLine()) == null) {
			//파일이 비어있으면 아래 문구 출력
			System.out.print("빈 파일입니다\n");
		}
		// 읽은 첫 줄(헤더)을 콤마(,) 기준으로 나누어 배열로 저장
		String[] k35_field_name = k35_readtxt.split(",");
		
		int k35_LineCnt = 0; //데이터 입력 줄수를 세기 위한 카운터
		
		// CSV파일에서 데이터를 한 줄씩 읽어오기
		while((k35_readtxt = k35_br.readLine()) != null) {
			// 읽은 줄을 콤마(,) 기준으로 나누어 배열로 저장
			String[] k35_field = k35_readtxt.split(",");
			// INSERT 쿼리문을 저장할 변수
			String k35_Querytxt;
			k35_Querytxt = String.format("insert into parking_zone ("
					//parking_zone 테이블에 삽입할 SQL문 작성
					//문자열 안에 %s를 각 필드 데이터로 채워 넣음.
					+ "manage_id, parking_zone_name, longitude, latitude, parking_zone_category,"
					+ "parking_zone_type, place_addr_road, place_addr_land, parking_space, oper_day,"
					+ "week_oper_start_time, week_oper_end_time, sat_oper_start_time, sat_oper_end_time,"
					+ "holi_oper_start_time, holi_oper_end_time, parking_cost, mange_office, city, country,"
					+ "coor_x, coor_y, space_code, phone, write_date)"
			        + "values ('%s', '%s', '%s', '%s', '%s',"
			        + "'%s', '%s', '%s', '%s', '%s',"
			        + "'%s', '%s', '%s', '%s', '%s',"
			        + "'%s', '%s', '%s', '%s', '%s',"
			        + "'%s', '%s', '%s', '%s', '%s'"
			        + ");",
					k35_field[0], k35_field[1], k35_field[2], k35_field[3], k35_field[4],
					k35_field[5], k35_field[6], k35_field[7], k35_field[8], k35_field[9],
					k35_field[10], k35_field[11], k35_field[12], k35_field[13], k35_field[14],k35_field[15],
					k35_field[16], k35_field[17], k35_field[18], k35_field[19], k35_field[20],k35_field[21],
					k35_field[22], k35_field[23], k35_field[24]);
			k35_stmt.execute(k35_Querytxt);
			k35_LineCnt++;
			//삽입 성공 시, 메세지 출력
			System.out.printf("%d번째 항목 insert OK [%s]\n",k35_LineCnt, k35_Querytxt);
		}
		k35_br.close(); //// 파일 닫기 (파일 읽기 종료)
		k35_stmt.close(); //Statement 객체 닫기  (자원 해제)
		k35_con.close();  //Connection 객체 닫기  (DB연결 해제)
	}
		
}