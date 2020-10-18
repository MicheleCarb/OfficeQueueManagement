package app;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		try {
			DAO dao = new DAO();
			dao.getAllStats('d');
			dao.close();
		} catch (ClassNotFoundException | SQLException exc){
			exc.printStackTrace();
		}

	}

}
