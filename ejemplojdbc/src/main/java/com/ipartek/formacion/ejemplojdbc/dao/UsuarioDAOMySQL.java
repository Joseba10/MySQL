package com.ipartek.formacion.ejemplojdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ipartek.formacion.ejemplojdbc.tipos.Usuario;

public class UsuarioDAOMySQL extends IpartekDAOMySQL implements UsuarioDAO {

	private final static String FIND_ALL = "Select * from usuarios";
	private final static String FIND_ID = "Select * from usuarios where id=?";
	private final static String INSERT = "Insert into usuarios(username,password,nombre_completo,id_roles)Values(?,?,?,?)";
	private final static String Update = "Update usuarios Set username=?,password=?,nombre_completo=?,id_roles=? where id=?";
	private final static String Delete = "Delete from usuarios where id=?";
	private PreparedStatement psFindAll, psFinById, psInsert, psUpdate, psDelete;
	public ResultSet rs = null;

	public Usuario[] findAll() {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

		try {
			psFindAll = con.prepareStatement(FIND_ALL);
			rs = psFindAll.executeQuery();
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

			try {
				if (rs != null)
					rs.close();

				if (psFindAll != null)
					psFindAll.close();

			} catch (SQLException e) {

			}

		}
		return usuarios.toArray(new Usuario[usuarios.size()]);
	}

	public Usuario findById(int id) {

		Usuario usuario = null;

		try {
			psFinById = con.prepareStatement(FIND_ID);

			psFinById.setInt(1, id);
			rs = psFinById.executeQuery(); // Conjunto de resultados que salen
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
			psInsert = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
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

		} catch (Exception e) {
			throw new DAOException("Error en Insert", e);
		} finally {

			cerrar(psDelete);
		}

	}

	private void cerrar(PreparedStatement ps) {
		cerrar(ps, null);
	}

	private void cerrar(PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				con.close();

			if (psFindAll != null)
				psFindAll.close();

		} catch (SQLException e) {

		}

	}

	public void update(Usuario usuario) {
		try {
			psUpdate = con.prepareStatement(Update);
			psUpdate.setString(1, usuario.getUsername());
			psUpdate.setString(2, usuario.getPassword());
			psUpdate.setString(3, usuario.getNombre_completo());
			psUpdate.setInt(4, usuario.getId_roles());

			psUpdate.setInt(5, usuario.getId());
			int res = psUpdate.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualizacion ha devuelto un valor " + res);

		} catch (Exception e) {
			throw new DAOException("Error al actualizar", e);
		}

	}

	public void delete(Usuario usuario) {

		delete(usuario.getId());
	}

	public void delete(int id) {
		try {
			psDelete = con.prepareStatement(Delete);
			psDelete.setInt(1, id);
			int res = psDelete.executeUpdate();
			if (res != 1)
				throw new DAOException("La actualizacion ha devuelto un valor " + res);
		} catch (Exception e) {

			throw new DAOException("Error en el delete ", e);
		}

	}

}
