package br.com.coldigogeladeiras.rest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;

//****** Cadastra Marca ****

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import com.google.gson.Gson;


//***** Consulta Buscar *******

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

//****** Excluir Marca *******
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;

//*******A��o Botao Salvar ********
import javax.ws.rs.PUT;


@Path("marca")
public class MarcaRest extends UtilRest {

		@GET
		@Path("/buscar")
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscar() {
			
			
			
			try {
			
			List<Marca> listaMarcas = new ArrayList<Marca>();
			
		
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscar();
		
			conec.fecharConexao();
			return this.buildResponse(listaMarcas);
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
			
			
		}
		
		
		//************ Cadastra Marca **************
		
		@POST
		@Path("/inserir")
		@Consumes("application/*")
		public Response inserir(String marcaParam) {
			
			try {
				
				Marca marca = new Gson().fromJson(marcaParam, Marca.class);
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				
				JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
				boolean retorno = jdbcMarca.inserir(marca);
				
				String msg = "";
				if(retorno) {
					msg = "Marca cadatrada com sucesso!";
					
				}else {
					msg = "Erro ao cadastrar marca.";
				}
				
				conec.fecharConexao();
				
				return this.buildResponse(msg);
				
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
			
			
			
		}
		
		//*************** Consulta/Busca Marca *************
		
		@GET
		@Path("/buscarMarca")
		@Consumes("application/*")
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscarPorNome(@QueryParam("valorBusca") String nome) {
			
			try {
				
					List<JsonObject> listaMarcas = new ArrayList<JsonObject>();
					
					Conexao conec = new Conexao();
					Connection conexao = conec.abrirConexao();
					JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
					
					
					listaMarcas = jdbcMarca.buscarPorNome(nome);
					
					
					conec.fecharConexao();
					
					String json = new Gson().toJson(listaMarcas);
					return this.buildResponse(json);
				
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
		}
		
		//**************** Excluir ********************************
		
		@DELETE
		@Path("/excluir/{id}")
		@Consumes("application/*")
		public Response excluir (@PathParam("id") int id) {
			
			try {
			    	Conexao conec = new Conexao();
			    	Connection conexao = conec.abrirConexao();
			    	JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			    	
			    	
			    	boolean verificaDados = jdbcMarca.verificaDados(id); //Aqui eu crio um boolean que recebe o resultado da minha fun��o validativa
			    				    				    	
			    	
			    	String msg = "";
			    	
			    	if(!verificaDados) {    //Valido o retorno da minha funcao
			    	
			    	
			    	boolean retorno = jdbcMarca.deletar(id);
			    	
			    
			    	if(retorno) {
			    		msg = "Marca exclu�da com sucesso!";
			    	}else {
			    		msg = "Erro ao excluir marca. ";
			    	}
			    	
			    	}else {
			    		msg = "Erro. N�o � poss�vel excluir uma marca com produto cadastrado";
			    	}
			    	
			    	conec.fecharConexao();
			    	
			    	return this.buildResponse(msg);
				
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
			
			
		}
		
		//****************** EditarMarca *****************************
		
		@GET
		@Path("/buscarPorId")
		@Consumes("application/*")
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscarPorId(@QueryParam("id") int id) {
			try {
				
				Marca marca = new Marca();
				
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
				
				marca = jdbcMarca.buscarPorId(id);
				
				
				
				conec.fecharConexao();
				
				return this.buildResponse(marca);
				
				
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
		}
		
		//******************** A��o BOTAO SALVAR *****************
		
		@PUT
		@Path("/alterar")
		@Consumes("application/*")
		public Response alterar(String marcaParam) {
			
			try {
				
				Marca marca = new Gson().fromJson(marcaParam, Marca.class);
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
				
				boolean retorno = jdbcMarca.alterar(marca);
				
				String msg = "";
				if (retorno) {
					msg = "Marca alterada com sucesso!";
				}else {
					msg = "Erro ao alterar marca.";
				}
				
				conec.fecharConexao();
				return this.buildResponse(msg);
				
			}catch(Exception e) {
				
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
				
			}
			
			
		}
		
		
		//*************************************** REST Altera Status ***********************************//
		
		@PUT
		@Path("/alterarStatus")
		@Consumes("application/*")	
		public Response alterarStatus(String marcaParam) {
				
				
			try {
				
				Marca marca = new Gson().fromJson(marcaParam, Marca.class);
				
				Conexao conec = new Conexao();
				Connection conexao = conec.abrirConexao();
				JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
				
				boolean retorno = jdbcMarca.editarStatus(marca); 
				
				String mensagem = "";
				
				if (retorno) {
					mensagem = "Status alterado com sucesso !";
				} else {
					mensagem = "Erro ao alterar status da marca !";
				}
				
				conec.fecharConexao();
				
				return this.buildResponse(mensagem);
				
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			} 
		}
		
		
		
		
		
		
		
		
}
