package app;

import java.sql.*;

public class DAO {
    private final static String URL = "jdbc:mysql://localhost:3306/office?serverTimezone=UTC";
    private final static String USER = "admin";
    private final static String PASSWORD = "admin";
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private Connection con;
    //Queries
    private final static String NEW_REQUEST_TYPE_QUERY = "INSERT INTO request_type (tag_name,avg_time) VALUES (?,?)";
    private final static String NEW_REQUEST_QUERY = "INSERT INTO request (ref_counter, ref_type, date) VALUES (?,?,NOW())";
    //Get how many customers have been served for each request type
    private final static String GET_STATS_BASE_SELECT = "SELECT RT.tag_name as Request_Type, COUNT(*) AS Customers";
    private final static String GET_STATS_BASE = "FROM request R, request_type RT " +
            "WHERE R.ref_type = RT.id " +
            "GROUP BY RT.tag_name ";
    //Get the number of customers each counter has served divided by request type
    private final static String GET_STATS_PER_COUNTER_SELECT = "SELECT RT.tag_name as Request_Type, R.ref_counter as Counter, COUNT(*) AS Customers";
    private final static String GET_STATS_PER_COUNTER = "FROM request R, request_type RT " +
            "WHERE R.ref_type = RT.id " +
            "GROUP BY R.ref_counter, RT.tag_name ";

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
    public void createRequestType(String tagName, Float avgTime) throws SQLException {
        PreparedStatement pst = con.prepareStatement(NEW_REQUEST_TYPE_QUERY);
        //Setting parameters
        pst.setString(1, tagName);
        pst.setFloat(2, avgTime);
        pst.execute(); //Executing query
    }

    public void insertRequest(Integer counterId, Integer reqTypeId) throws SQLException {
        PreparedStatement pst = con.prepareStatement(NEW_REQUEST_TYPE_QUERY);
        //Setting parameters
        pst.setInt(1, counterId);
        pst.setInt(2, reqTypeId);
        pst.execute(); //Executing query
    }

    public ResultSet getAllStats(Character DayWeekMonth) throws SQLException {
        Statement st = con.createStatement();
         switch (DayWeekMonth) {
            case 'w' : return st.executeQuery(GET_STATS_BASE_SELECT + ",WEEK(R.date) " +
                    GET_STATS_BASE + " ,WEEK(R.date) ORDER BY RT.tag_name ASC");
            case 'm' : return st.executeQuery(GET_STATS_BASE_SELECT + ",MONTH(R.date) " +
                    GET_STATS_BASE + " ,MONTH(R.date) ORDER BY RT.tag_name ASC");
            default : return st.executeQuery(GET_STATS_BASE_SELECT + ",R.date " +
                    GET_STATS_BASE + " ,R.date ORDER BY RT.tag_name ASC");
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
