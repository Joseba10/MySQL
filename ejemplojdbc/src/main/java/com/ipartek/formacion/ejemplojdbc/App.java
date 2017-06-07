package com.ipartek.formacion.ejemplojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ipartek.formacion.ejemplojdbc.dao.DAOException;
import com.ipartek.formacion.ejemplojdbc.dao.UsuarioDAO;
import com.ipartek.formacion.ejemplojdbc.dao.UsuarioDAOMySQL;
import com.ipartek.formacion.ejemplojdbc.tipos.Usuario;

public class App {

	public static void main(String[] args) {

		try {
			UsuarioDAO dao = new UsuarioDAOMySQL();

			for (Usuario u : dao.findAll())
				System.out.println(u);
		} catch (DAOException e) {
			// e.printStackTrace();
			e.getCause().printStackTrace();
		}

	}

	public static void mainBasico(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver").newInstance(); // Permite que el driver
																// este disponible

		String url = "jdbc:mysql://localhost/ipartek?user=root&password="; // Ponemos la
																			// direccion

		Connection con = DriverManager.getConnection(url); // Usa el driver para
															// conectarse a la url

		// Statement st = con.createStatement();

		String sql = "Select * from usuarios where id=?";
		PreparedStatement pst = con.prepareStatement(sql);
		int id = 1;
		pst.setInt(1, id);

		// String sql = "Select * from usuarios where id=" + id;

		ResultSet rs = pst.executeQuery(); // st.executeQuery(sql);

		while (rs.next())
			// Da un verdadero y falso y salta al siguiente registro
			System.out.println(rs.getString("username") + "," + rs.getString("nombre_completo"));
		// Recoge el dato del campo username
		rs.close();
		// st.close();
		pst.close();

		String sqlInsert = "Insert into usuarios (username,password,nombre_completo) " + "values (?,?,?)";

		PreparedStatement pstInsert = con.prepareStatement(sqlInsert);

		String username = "jdbcnuevo", password = "jdbcnuevopass", nombre_completo = "JDBC";

		pstInsert.setString(1, username);
		pstInsert.setString(2, password);
		pstInsert.setString(3, nombre_completo);

		int res = pstInsert.executeUpdate();

		System.out.println("Se ha modificado " + res + " registros");

		String sqlupdate = "Update usuarios set username=?,password=?,nombre_completo=? " + "where id=?";

		PreparedStatement pstupdate = con.prepareStatement(sqlupdate);

		username = "jdbcnuevo";
		password = "jdbcnuevopass";
		nombre_completo = "JDBC";

		pstupdate.setString(1, username);
		pstupdate.setString(2, password);
		pstupdate.setString(3, nombre_completo);

		res = pstupdate.executeUpdate();

		System.out.println("Se ha modificado " + res + " registros");

		con.close();

	}
}
