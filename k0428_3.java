package k40428;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class k0428_3 { //클래스 선언

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
		
		// 기준 좌표 설정 (위도, 경도)
		double k35_lat = 37.3860521;
		double k35_lng = 127.1214038;
		
		String k35_Querytxt;
		
		// 조회할 SQL 쿼리문 작성
		// 다양한 조회 조건을 테스트할 수 있도록 쿼리를 변경할 수 있음+
		//가까운 주차장을 찾는 쿼리
		k35_Querytxt = String.format("select * from parking_zone  where "
				+ "SQRT(POWER(latitude-%f, 2) + POWER(longitude-%f, 2)) = "
				+ "(select MIN(SQRT(POWER(latitude-%f,2)+ POWER(longitude-%f,2)))"
				+ "from parking_zone);", k35_lat, k35_lng, k35_lat, k35_lng);
		
		//parking_zone 데이터 전체를 조회하는 쿼리
		//k35_Querytxt = "select * from parking_zone;";
		
		//요금정보가 무료인 데이터만 조회하는 쿼리
		//k35_Querytxt = "select * from parking_zone where parking_cost = '무료';";
		
		//설치지역이 '합천군'인 데이터만 조회하는 쿼리
		//k35_Querytxt = "select * from parking_zone where country = '합천군';";
		
		//SQL 구문을 실행하고 Resultset형태로 반환된 결과를 k35_rset에 저장 
		ResultSet k35_rset = k35_stmt.executeQuery(k35_Querytxt);
		
		int k35_iCnt = 1; //읽어드린 줄을 계산하기 위한 변수
		//k35_rset에 저장된 데이터를 한줄씩 읽어오기
		// .next()는 커서를 다음줄로 이동시키고, 데이터가 있으면 true, 없으면 false 반환
		while (k35_rset.next()) {
			System.out.printf("*(%d)*****************************\n", k35_iCnt++);
			System.out.printf("관리번호             : %s\n", k35_rset.getString(1));
			System.out.printf("주차장 명            : %s\n", k35_rset.getString(2));
			System.out.printf("경도                 : %s\n", k35_rset.getString(3));
			System.out.printf("위도                 : %s\n", k35_rset.getString(4));
			System.out.printf("주차장 구분          : %s\n", k35_rset.getString(5));
			System.out.printf("주차장 유형          : %s\n", k35_rset.getString(6));
			System.out.printf("도로명 주소          : %s\n", k35_rset.getString(7));
			System.out.printf("지번 주소            : %s\n", k35_rset.getString(8));
			System.out.printf("주차구획수           : %s\n", k35_rset.getString(9));
			System.out.printf("운영요일             : %s\n", k35_rset.getString(10));
			System.out.printf("평일 운영시작시각    : %s\n", k35_rset.getString(11));
			System.out.printf("평일 운용종료시각    : %s\n", k35_rset.getString(12));
			System.out.printf("토요일 운영시작시각  : %s\n", k35_rset.getString(13));
			System.out.printf("토요일 운영종료시각  : %s\n", k35_rset.getString(14));
			System.out.printf("공휴일 운영시작시각  : %s\n", k35_rset.getString(15));
			System.out.printf("공휴일 운영종료시각  : %s\n", k35_rset.getString(16));
			System.out.printf("요금정보             : %s\n", k35_rset.getString(17));
			System.out.printf("관리기관명           : %s\n", k35_rset.getString(18));
			System.out.printf("지역구분             : %s\n", k35_rset.getString(19));
			System.out.printf("지역구분_sub         : %s\n", k35_rset.getString(20));
			System.out.printf("x좌표                : %s\n", k35_rset.getString(21));
			System.out.printf("y좌표                : %s\n", k35_rset.getString(22));
			System.out.printf("지역코드             : %s\n", k35_rset.getString(23));
			System.out.printf("연락처               : %s\n", k35_rset.getString(24));
			System.out.printf("데이터기준일자       : %s\n", k35_rset.getString(25));
			System.out.printf("********************************\n");
			
		}
		System.out.printf("%d건 출력", k35_iCnt-1);
		k35_rset.close(); // ResultSet 객체 닫기  (자원 해제)
		k35_stmt.close(); //Statement 객체 닫기  (자원 해제)
		k35_con.close();  //Connection 객체 닫기  (DB연결 해제)
	}
}