package br.com.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.coldigogeladeiras.jdbcinterface.MarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;

//**************** Cadastro Marca **********


import java.sql.PreparedStatement;
import java.sql.SQLException;

//**************** Consulta Busca *******

import com.google.gson.JsonObject; 
import java.sql.ResultSet;
import java.sql.Statement;


public class JDBCMarcaDAO implements MarcaDAO{

		private Connection conexao;
		
		public JDBCMarcaDAO(Connection conexao) {
			this.conexao = conexao;
		}
	
		public List<Marca> buscar(){
			
			String comando = "SELECT * FROM marcas";
			
			List<Marca> listMarcas = new ArrayList<Marca>();
			
			Marca marca = null;
			
			try {
				
				Statement stmt = conexao.createStatement();
				
				ResultSet rs = stmt.executeQuery(comando);
				
				while (rs.next()) {
					
					marca = new Marca();
					
					int id = rs.getInt("id");
					String nome = rs.getString("nome");
					int status = rs.getInt("status");
					
					marca.setId(id);
					marca.setNome(nome);
					marca.setStatus(status);
					
					listMarcas.add(marca);
				}
				
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			return listMarcas;
		}
		
		
		
		//******************* Cadastrando Marca **************
		
			public boolean inserir(Marca marca) {
				
				String comando = "INSERT INTO marcas "
						+ "(id, nome) "
						+ "VALUES (?, ?)";
				
			PreparedStatement p;
			
			try  {
				
				
				p = this.conexao.prepareStatement(comando);
				
				//substitui os ? pelos valores da marca
				p.setInt(1, marca.getId());
				p.setString(2, marca.getNome());
				p.execute();
				
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				return false;
			}
			return true;
			}
			
			//**************** Buscar/Marcas ****************
			
			public List<JsonObject> buscarPorNome(String nome){
				String comando = "SELECT * FROM marcas ";
				
				if(!nome.equals("")) {
					
					comando += "WHERE nome LIKE '%" + nome + "%' ";
					
				}
				
				comando +="ORDER BY marcas.nome ASC";
				
				List<JsonObject> listaMarcas = new ArrayList<JsonObject>();
				JsonObject marca = null;
				
				try {
					
					Statement stmt = conexao.createStatement();
					ResultSet rs = stmt.executeQuery(comando);
					
					while (rs.next()) {
						
						int id = rs.getInt("id");
						String nomeMarca = rs.getString("nome");
						int status = rs.getInt("status");
						
						marca = new JsonObject();
						marca.addProperty("id",id);
						marca.addProperty("nomeMarca", nomeMarca);
						marca.addProperty("status", status);
						
						listaMarcas.add(marca);
			
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return listaMarcas;
			}
			//******************** Valida Marca  *****************************
			// Verifico se existe algum produto vinculado no id da marca.
			
			public boolean verificaDados(int id) {
				
				String comando = "SELECT * FROM produtos WHERE marcas_id = ? ";
				
							
				PreparedStatement p;
				
				try {
					
					p = this.conexao.prepareStatement(comando);
					p.setInt(1, id);
					
					ResultSet rs = p.executeQuery();
					
					if(rs.next()) {
						return true;
					}else {
						return false;
					}
					
				}catch(SQLException e) {
					e.printStackTrace();
					return false;
				}
				
				
			}
			
			
			
			//******************** Excluir Marcas *******************************
			
			public boolean deletar (int id) {
				String comando = "DELETE FROM marcas WHERE id = ?";
				PreparedStatement p ;
				try {
				p = this.conexao.prepareStatement(comando);
				p.setInt(1, id);
				p.execute();

				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				
				
				return true;
				
			}
			
			//********************* Editar ********************************
			
			public Marca buscarPorId(int id) {
				
				String comando = "SELECT * FROM marcas WHERE marcas.id = ?";
				Marca marca = new Marca();
				try {
					PreparedStatement p = this.conexao.prepareStatement(comando);
					p.setInt(1, id);
					ResultSet rs = p.executeQuery();
					while(rs.next()) {
						
						
						String nome = rs.getString("nome");
					
						marca.setId(id);
						marca.setNome(nome);
						
						
						
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
						
				
				return marca;
			}
			
			//********************** A��o Bot�o Salvar ***************************
			
			
			public boolean alterar(Marca marca) {
				
				String comando = "UPDATE marcas "
						+ "SET nome=?"
						+ " WHERE id=?";
				
				PreparedStatement p;
				
				try {
					
					p = this.conexao.prepareStatement(comando);
					
					p.setString(1, marca.getNome());
					p.setInt(2, marca.getId());
					
					p.executeUpdate();
					
				}catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
			
			//********************************* Editando Status ***************************//
			
public boolean editarStatus(Marca marca) {
				
				String comando = "UPDATE marcas SET status=? WHERE id=? ";
				
			PreparedStatement p;
			
			try  {
				
				
				p = this.conexao.prepareStatement(comando);
				
				//substitui os ? pelos valores da marca
				
				p.setInt(1, marca.getStatus());
				p.setInt(2, marca.getId());
				
				p.execute();
				
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				return false;
			}
			return true;
			}
		
		
		
}

