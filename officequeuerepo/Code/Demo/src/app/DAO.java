package app;

import java.sql.*;

public class DAO {
    private final static String URL = "jdbc:mysql://localhost:3306/office?serverTimezone=UTC";
    private final static String USER = "root";
    private final static String PASSWORD = "root";
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final Connection con;
    //Queries
    private final static String NEW_REQUEST_QUERY = "INSERT INTO request (ref_counter, type_tag_name, date) VALUES (?,?,NOW())";
    //Get how many customers have been served for each request type
    private final static String GET_STATS_BASE_SELECT = "SELECT R.type_tag_name as Request_Type, COUNT(*) AS Customers";
    private final static String GET_STATS_BASE = "FROM request R GROUP BY R.type_tag_name ";
    //Get the number of customers each counter has served divided by request type
    private final static String GET_STATS_PER_COUNTER_SELECT = "SELECT R.type_tag_name as Request_Type, R.ref_counter as Counter, COUNT(*) AS Customers";
    private final static String GET_STATS_PER_COUNTER = "FROM request R GROUP BY R.ref_counter, R.type_tag_name ";

    //Constructor
    public DAO() throws SQLException, ClassNotFoundException {
    	Class.forName(DRIVER); //Loading driver
    	con = DriverManager.getConnection(URL, USER, PASSWORD); //Getting connection
    }

    //Closing connection
    public void close() throws SQLException {
        con.close(); //Closing Connection
    }

    //DB query functions
    public void insertRequest(Integer counterId, String reqTypeTagName) throws SQLException {
        PreparedStatement pst = con.prepareStatement(NEW_REQUEST_QUERY);
        //Setting parameters
        pst.setInt(1, counterId);
        pst.setString(2, reqTypeTagName);
        pst.execute(); //Executing query
    }

    public ResultSet getAllStats(Character DayWeekMonth) throws SQLException {
        Statement st = con.createStatement();
         switch (DayWeekMonth) {
            case 'w' : return st.executeQuery(GET_STATS_BASE_SELECT + ",WEEK(R.date) " +
                    GET_STATS_BASE + " ,WEEK(R.date) ORDER BY R.type_tag_name ASC");
            case 'm' : return st.executeQuery(GET_STATS_BASE_SELECT + ",MONTH(R.date) " +
                    GET_STATS_BASE + " ,MONTH(R.date) ORDER BY R.type_tag_name ASC");
            default : return st.executeQuery(GET_STATS_BASE_SELECT + ",R.date " +
                    GET_STATS_BASE + " ,R.date ORDER BY R.type_tag_name ASC");
        }
    }

    public ResultSet getAllStatsByCounter(Character DayWeekMonth) throws SQLException {
        Statement st = con.createStatement();
        switch (DayWeekMonth) {
            case 'w' : return st.executeQuery(GET_STATS_PER_COUNTER_SELECT + ",WEEK(R.date) " +
                    GET_STATS_PER_COUNTER + " ,WEEK(R.date) ORDER BY R.ref_counter ASC");
            case 'm' : return st.executeQuery(GET_STATS_PER_COUNTER_SELECT + ",MONTH(R.date) " +
                    GET_STATS_PER_COUNTER + " ,MONTH(R.date) ORDER BY R.ref_counter ASC");
            default : return st.executeQuery(GET_STATS_PER_COUNTER_SELECT + ",R.date " +
                    GET_STATS_PER_COUNTER + " ,R.date ORDER BY R.ref_counter ASC");
        }
    }
}
