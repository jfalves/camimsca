package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import modelo.PlanoSaude;

public class DAOPlanoSaude {

	//Cria objetos SQL
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOPlanoSaude() {
		
		try {
			
			//carrega o driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//define parametros da conexao
			String url = "jdbc:mysql://localhost:3306/sca_camim";
			c = DriverManager.getConnection(url,"root","mysql");
			
			sql = null;
			r = null;
		}
		catch(Exception e) {
			
			System.out.println("ERRO :" + e);
		}
	}

	public void cadastrar(PlanoSaude planoSaude) { // método para realizar um cadastro 
		
		String query = "INSERT INTO planosaude (idadeMinima, idadeMaxima, valorFaixa, idTipoPlano) VALUES (?, ?, ?, " +
					   "(SELECT t.idTipoPlano " +
					   "FROM tipoplano AS t " +
					   "WHERE t.nome = ?));";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1, planoSaude.getIdadeMinima());
			sql.setInt(2, planoSaude.getIdadeMaxima());
			sql.setDouble(3, planoSaude.getValorFaixa());
			sql.setString(4, planoSaude.getTipo());
			
			// executar o sql
			sql.executeUpdate();
			sql.close();
			
		} catch(Exception e){
			System.out.println("Erro cadastrar: " + e);
		}
	}
	
	public PlanoSaude consultar(PlanoSaude planoSaude) { // método para realizar uma consulta
		
		String query = "SELECT p.idPlanoSaude,  p.idadeMinima, p.idadeMaxima, p.valorFaixa, t.nome " +
					   "FROM planosaude AS p " +
					   "INNER JOIN tipoplano AS t ON p.idTipoPlano = t.idTipoPlano " +
					   "WHERE p.idadeMinima = ? and p.idadeMaxima = ? and t.nome = ?;";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1, planoSaude.getIdadeMinima());
			sql.setInt(2, planoSaude.getIdadeMaxima());
			sql.setString(3, planoSaude.getTipo());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				planoSaude.setIdPlanoSaude(r.getInt("idPlanoSaude"));
				planoSaude.setIdadeMinima(r.getInt("idadeMinima"));
				planoSaude.setIdadeMaxima(r.getInt("idadeMaxima"));
				planoSaude.setValorFaixa(r.getDouble("valorFaixa"));
				planoSaude.setTipo(r.getString("nome"));
				
				sql.close();
				return planoSaude;
			} else {
				sql.close();
				return null;
			}
		}
		catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
public ArrayList<PlanoSaude> consultarTodos(int start, int limit) { // método para retornar todas as especialidades
		
		String query = "SELECT p.idPlanoSaude, p.idadeMinima, p.idadeMaxima, p.valorFaixa, t.nome " +
					   "FROM planosaude AS p " +
					   "INNER JOIN tipoplano as t ON p.idTipoPLano = t.idTipoPlano " +
					   "LIMIT ?, ?;";
		
		ArrayList<PlanoSaude> listaPlanoSaude = new ArrayList<PlanoSaude>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,start);
			sql.setInt(2,limit);
			
			r = sql.executeQuery(); //recebe a consulta
			
			while(r.next()) {
				PlanoSaude planoSaude = new PlanoSaude();
				
				planoSaude.setIdPlanoSaude(r.getInt("idPlanoSaude"));
				planoSaude.setIdadeMinima(r.getInt("idadeMinima"));
				planoSaude.setIdadeMaxima(r.getInt("idadeMaxima"));
				planoSaude.setValorFaixa(r.getDouble("valorFaixa"));
				planoSaude.setTipo(r.getString("nome"));
				
				listaPlanoSaude.add(planoSaude);
			}
			
			sql.close();
			return listaPlanoSaude;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}	
	
	public void alterar(PlanoSaude planoSaude) { // método para realizar uma alteração
		
		String query = "UPDATE planoSaude SET valorFaixa = ? " +
					   "WHERE idPlanoSaude = ?;";
		try {
			sql = c.prepareStatement(query);
			
			sql.setDouble(1,planoSaude.getValorFaixa());
			sql.setInt(2,planoSaude.getIdPlanoSaude());
			
			sql.executeUpdate();
			sql.close();
		} catch(Exception e){
			System.out.println("Erro alterar: " + e);
		}
	}
	
	public void excluir(PlanoSaude planoSaude) { // método para realizar uma exclusão
		String query = "DELETE FROM planosaude WHERE idPlanoSaude = ?;";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,planoSaude.getIdPlanoSaude());
			
			sql.executeUpdate();
			sql.close();
		} catch(Exception e) {
			System.out.println("Erro excluir: " + e);
		}
	}	
	
	/*public PlanoSaude consultaTipoPlano(PlanoSaude planoSaude, String tipoPlano) { // método para realizar uma consulta
		String query = "SELECT t.idTipoPlano " +
					"FROM tipoplano AS t " +
					"WHERE t.nome = ?;";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, tipoPlano);
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				planoSaude.setIdTipoPlano(r.getInt("idTipoPlano"));

				
				sql.close();
				return planoSaude;
			} else {
				sql.close();
				return null;
			}
		}
		catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}*/
	
	public ArrayList<PlanoSaude> listaTipoPlanoSaude() { // método para retornar todas os tipos de plano de saude
		
		String query = "SELECT t.nome " +
					   "FROM tipoplano AS t " +
					   "ORDER BY t.nome;";
		
		ArrayList<PlanoSaude> listaTipoPlanoSaude = new ArrayList<PlanoSaude>();
		
		try {
			sql = c.prepareStatement(query);
			
			r = sql.executeQuery(); //recebe a consulta
			
			while(r.next()) {
				PlanoSaude planoSaude = new PlanoSaude();
				
				planoSaude.setTipo(r.getString("nome"));
				
				listaTipoPlanoSaude.add(planoSaude);
			}
			
			sql.close();
			return listaTipoPlanoSaude;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}

	public boolean verificaFaixa(PlanoSaude planoSaude) { // método para realizar uma consulta
		String query = "SELECT p.idadeMinima, p.idadeMaxima " +
					   "FROM planosaude AS p " +
					   "INNER JOIN tipoplano AS t ON p.idTipoPlano = t.idTipoPlano " +
					   "WHERE t.nome = ?;";
		
		PlanoSaude auxiliar = new PlanoSaude();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, planoSaude.getTipo());
			
			r = sql.executeQuery(); //recebe a consulta
	
			while(r.next()) {
				auxiliar.setIdadeMinima(r.getInt("idadeMinima"));
				auxiliar.setIdadeMaxima(r.getInt("idadeMaxima"));
				
				if((planoSaude.getIdadeMinima() >= auxiliar.getIdadeMinima() && planoSaude.getIdadeMinima() <= auxiliar.getIdadeMaxima()) || (planoSaude.getIdadeMaxima() >= auxiliar.getIdadeMinima() && planoSaude.getIdadeMaxima() <= auxiliar.getIdadeMaxima())) {
					return false;
				}
			} 
			sql.close();
			return true;
		}
		catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return false;
		}
	}
	
	public int qtdRegistro() { // conta a quantidade de registros na tabela
		
		String query = "SELECT count(*) " +
					   "FROM planoSaude AS p;";
		
		try {
			sql = c.prepareStatement(query);
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				int count = r.getInt(1);
				
				sql.close();
				return count;
			} else {
				sql.close();
				return 0;
			}
		}
		catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return 0;
		}
	}	
	
	public void finalize() { // método para tratar pendências
		try {
			c.close();
		}
		catch(Exception ex) {
			System.out.println("Erro: " + ex);
		}
	}	
}
