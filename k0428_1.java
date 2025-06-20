package k40428;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class k0428_1 { //클래스 선언
	public static void main(String[] args) {
		//JDBC 객체들 선언 (초기에는 null로 설정)
		Connection k35_con = null;    // DB 연결 객체
		Statement k35_stmt = null;    // SQL 실행을 위한 객체
	
		try {
			//JDBC 드라이버 클래스를 동적으로 로드함
			//드라이버는 자바 프로그램이 MySQL과 통신할 수 있게 해주는 라이브러리
			//반드시 연결 전에 드라이버를 메모리에 올려야 함
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//MySQL 서버에 연결 (호스트: 192.168.23.230, 포트: 3306, DB명: kopo35, 사용자: root, 비밀번호: kopo35)
			//연결 성공 시 Connection 객체(k35_con)가 생성됨
			k35_con = DriverManager.getConnection("jdbc:mysql://192.168.23.230:3306/kopo35","root","kopo35");
			
			//SQL 구문을 실행할 수 있는 Statement 객체 생성
			//Statement는 SQL문을 DB에 전달하고 결과를 받아오는 기능을 함
			k35_stmt = k35_con.createStatement();
			
			// SQL 구문 실행: parking_zone 테이블 생성
			// DEFAULT CHARSET=utf8 설정으로 한글 데이터 저장 가능
			k35_stmt.execute("create table parking_zone("		    // freewifi 테이블 생성
					+ "manage_id	int not null primary key," 		// 관리번호, null값을 가질수 없음, 기본키 
					+ "parking_zone_name	varchar(200),"			// 주차장 명
					+ "longitude	double,"						// 경도
					+ "latitude	double,"						    // 위도
					+ "parking_zone_category	varchar(200),"		// 주차장 구분
					+ "parking_zone_type	varchar(200),"			// 주차장 유형 
					+ "place_addr_road	varchar(200),"				// 도로명 주소
					+ "place_addr_land	varchar(200),"				// 지번 주소
					+ "parking_space	varchar(200),"				// 주차구획수
					+ "oper_day	varchar(200),"						// 운영요일
					+ "week_oper_start_time	varchar(200),"			// 평일 운영시작시각
					+ "week_oper_end_time	varchar(200),"			// 평일 운용종료시각
					+ "sat_oper_start_time	varchar(200),"			// 토요일 운영시작시각
					+ "sat_oper_end_time	varchar(200),"			// 토요일 운영종료시각
					+ "holi_oper_start_time	varchar(200),"			// 공휴일 운영시작시각
					+ "holi_oper_end_time	varchar(200),"			// 공휴일 운영종료시각					
					+ "parking_cost	varchar(200),"					// 요금정보					
					+ "mange_office varchar(200),"					// 관리기관 명
					+ "city	varchar(50),"							// 지역구분
					+ "country	varchar(200),"						// 지역구분_sub
					+ "coor_x	double,"							// x좌표
					+ "coor_y	double,"							// y좌표
					+ "space_code	int,"							// 지역코드
					+ "phone	varchar(200),"						// 연락처
					+ "write_date	date"							// 작성일자
					+ ") DEFAULT CHARSET=utf8;");
			
		} catch (SQLException e) {
			// SQL 명령 실행이나 DB 연결 중 오류 발생 시
			// 예외가 발생하면 오류 메시지와 오류 번호를 출력
			System.out.println("데이터베이스 오류 : " + e.getMessage());
			System.out.println("데이터베이스 오류번호 : " + e.getErrorCode());
		
		} catch (ClassNotFoundException e) {
			// JDBC 드라이버 클래스를 찾을 수 없는 경우
			// 드라이버를 못 찾았을 때 오류 메시지 출력
			System.out.println("드라이버를 찾을 수 없습니다 : " + e.getMessage());
		} finally {
			// DB 연결, Statement, ResultSet은 사용이 끝나면 close() 호출해서 메모리 해제해야 함
			// 항상 생성한 순서의 반대 순서로 닫는 게 안전
			try {
				if(k35_stmt != null) k35_stmt.close();
				if(k35_con != null) k35_con.close();
			} catch (SQLException e) {
				// 리소스를 닫는 도중 오류가 발생할 수도 있기 때문에 별도로 예외 처리
				System.out.println("리소스를 닫는 도중 오류 발생 : "+ e.getMessage());
			}
		}
	}
}