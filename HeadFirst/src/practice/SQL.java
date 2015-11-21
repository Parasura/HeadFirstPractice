package practice;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class SQL {
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/parasu_db", "root", "root");
		Statement st = (Statement) con.createStatement();
		// st.execute("insert into RCA_ARH_CKT_IR_METADATA (ckt,snc,routetype,routeid) values ('ckt1','snc2','route6',6)");

		ResultSet rs = st.executeQuery("select ckt,snc,routetype,routeid from RCA_ARH_CKT_IR_METADATA ");
		
		while(rs.next()){
			String ckt = rs.getString(1);
			String snc = rs.getString(2);			
			String routeType = rs.getString(3);
			int routeId = rs.getInt(4);
			
			new SNCRouteData().routeTypetoId.put(routeType, routeId);
		}
		
		System.out.println();
	}
	
	public static class SNCRouteData{
		Map<String ,Integer> routeTypetoId = new HashMap<String ,Integer>();
		
	}
	
	
}