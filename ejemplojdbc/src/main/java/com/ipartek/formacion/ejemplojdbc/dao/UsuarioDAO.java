package com.ipartek.formacion.ejemplojdbc.dao;

import com.ipartek.formacion.ejemplojdbc.tipos.Usuario;

public interface UsuarioDAO {

	public Usuario[] findAll(); // Select

	public Usuario findById(int id);

	public void insert(Usuario usuario);

	public void update(Usuario usuario);

	public void delete(Usuario usuario);

	public void delete(int id);

}
