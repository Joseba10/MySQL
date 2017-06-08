package com.ipartek.formacion.ejemplojdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ipartek.formacion.ejemplojdbc.tipos.Usuario;

public class UsuarioDAOMySQL implements UsuarioDAO {

	private Connection con;
	private String url = "jdbc:mysql://localhost/ipartek";
	private String mysqluser = "root";
	private String mysqlpassword = "";
	private final static String FIND_ALL = "Select * from usuarios";
	private final static String FIND_ID = "Select * from usuarios where id=?";
	private final static String INSERT = "Insert into usuarios(username,password,nombre_completo,id_roles)Values(?,?,?,?)";
	private final static String Update = "Update usuarios Set username=?,password=?,nombre_completo=?,id_roles=? where id=?";
	private final static String Delete = "Delete from usuarios where id=?";
	private PreparedStatement psFindAll, psFinById, psInsert, psUpdate, psDelete;

	public UsuarioDAOMySQL() {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, mysqluser, mysqlpassword);

			psFindAll = con.prepareStatement(FIND_ALL);
			psFinById = con.prepareStatement(FIND_ID);
			psInsert = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			psUpdate = con.prepareStatement(Update);
			psDelete = con.prepareStatement(Delete);

		} catch (InstantiationException e) {

			throw new DAOException(e.getMessage(), e);
		} catch (IllegalAccessException e) {

			throw new DAOException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {

			throw new DAOException("No se ha encontrado el driver de MySQL", e);
		} catch (SQLException e) {

			throw new DAOException("Error de conexion a la base de datos", e);
		} catch (Exception e) {
			throw new DAOException("Error no esperado", e);
		} finally {

			// try {
			// con.close();
			// } catch (SQLException e) {
			//
			// throw new DAOException("Error al cerrar", e);
			// }
		}
	}

	public Usuario[] findAll() {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		try {

			ResultSet rs = psFindAll.executeQuery();

			Usuario usuario;
			while (rs.next()) {

				// System.out.println(rs.getString("username"));
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setId_roles(rs.getInt("id_roles"));
				usuario.setNombre_completo(rs.getString("nombre_completo"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));

				usuarios.add(usuario);

			}

		} catch (SQLException e) {

			throw new DAOException("Error en FindAll", e);
		} finally {
			//
			// if (con != null)
			// try {
			// // con.close();
			// } catch (SQLException e) {
			// throw new DAOException("Error en FinAll", e);
			//
			// }

		}
		return usuarios.toArray(new Usuario[usuarios.size()]);
	}

	public Usuario findById(int id) {

		Usuario usuario = null;

		try {
			psFinById.setInt(1, id);
			ResultSet rs = psFinById.executeQuery(); // Conjunto de resultados que salen
														// de la consulta
			if (rs.next()) {

				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setId_roles(rs.getInt("id_roles"));
				usuario.setNombre_completo(rs.getString("nombre_completo"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));
			}
		} catch (SQLException e) {
			throw new DAOException("Error en FindById", e);
		}

		return usuario;

	}

	public int insert(Usuario usuario) {

		try {

			psInsert.setString(1, usuario.getUsername());
			psInsert.setString(2, usuario.getPassword());
			psInsert.setString(3, usuario.getNombre_completo());
			psInsert.setInt(4, usuario.getId_roles());

			int res = psInsert.executeUpdate();

			if (res != 1)
				throw new DAOException("La insercion ha devuelto un valor " + res);

			ResultSet generatedKeys = psInsert.getGeneratedKeys();

			if (generatedKeys.next())
				return generatedKeys.getInt(1);

			else
				throw new DAOException("No se ha recibido la clave generada");

		} catch (SQLException e) {
			throw new DAOException("Error en Insert", e);
		}

	}

	public void update(Usuario usuario) {
		try {
			psUpdate.setString(1, usuario.getUsername());
			psUpdate.setString(2, usuario.getPassword());
			psUpdate.setString(3, usuario.getNombre_completo());
			psUpdate.setInt(4, usuario.getId_roles());

			psUpdate.setInt(5, usuario.getId());
			int res = psUpdate.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualizacion ha devuelto un valor " + res);

		} catch (SQLException e) {
			throw new DAOException("Error al actualizar", e);
		}

	}

	public void delete(Usuario usuario) {

		delete(usuario.getId());
	}

	public void delete(int id) {
		try {
			psDelete.setInt(1, id);
			int res = psDelete.executeUpdate();
			if (res != 1)
				throw new DAOException("La actualizacion ha devuelto un valor " + res);
		} catch (SQLException e) {

			throw new DAOException("Error en el delete ", e);
		}

	}

}
