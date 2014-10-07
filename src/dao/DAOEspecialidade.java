package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.Especialidade;
import modelo.PrecoEspecialidade;

public class DAOEspecialidade {
	
	//Cria objetos SQL
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOEspecialidade() {
		
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
	
	public void cadastrar(Especialidade especialidade) { // método para realizar um cadastro 

		String query = "INSERT INTO especialidade (nome, descricao) VALUES (?, ?);";
		String query1 = "insert into valorEspecialidade(idValorEspecialidade, dataInicio, dataFim, valor) values (LAST_INSERT_ID(),?,?,?);";
		String query2 = "insert into vigencia(idVigencia, idEspecialidade, idValorEspecialidade) values (LAST_INSERT_ID(),LAST_INSERT_ID(),LAST_INSERT_ID());";
		
		try {
			
		    c.setAutoCommit(false);
		    
			sql = c.prepareStatement(query);
			
			sql.setString(1, especialidade.getNome());
			sql.setString(2, especialidade.getDescricao());
			
			// executar o sql
			sql.executeUpdate();
			
			sql = c.prepareStatement(query1);
			
			sql.setDate(1, toDate(especialidade.getPrecoEspecialidade().getDataInicio()));
			sql.setDate(2, toDate(especialidade.getPrecoEspecialidade().getDataFim()));
			sql.setDouble(3, especialidade.getPrecoEspecialidade().getValor());
			
			// executar o sql
			sql.executeUpdate();
			
			sql = c.prepareStatement(query2);
			
			// executar o sql
			sql.executeUpdate();
			
			c.commit();
			
			sql.close();
			
		} catch(Exception e){
			
			System.out.println("Erro cadastrar: " + e);
		}
	}

	public Especialidade consultar(Especialidade especialidade) { // método para realizar uma consulta
		
		String query = "SELECT e.idEspecialidade, e.nome, e.descricao " +
					   "FROM especialidade AS e WHERE e.nome = ?;";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, especialidade.getNome());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				especialidade.setIdEspecialidade(r.getInt("idEspecialidade"));
				especialidade.setNome(r.getString("nome"));
				especialidade.setDescricao(r.getString("descricao"));
				
				sql.close();
				return especialidade;
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
	
	public ArrayList<Especialidade> consultarTodos(int start, int limit) { // método para realizar uma consulta
		
		String query = "SELECT e.idEspecialidade, e.nome, e.descricao, ve.dataInicio, ve.dataFim, ve.valor " +
					   "FROM especialidade AS e " +
					   "INNER JOIN valorEspecialidade as ve ON ve.idValorEspecialidade = e.idEspecialidade " +
					   "LIMIT ?, ?;";
		
		ArrayList<Especialidade> especialidades = new ArrayList<Especialidade>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,start);
			sql.setInt(2,limit);
			
			r = sql.executeQuery(); //recebe a consulta
	
			while(r.next()) {
				Especialidade especialidade = new Especialidade();
				PrecoEspecialidade precoEspecialidade = new PrecoEspecialidade();
				
				especialidade.setIdEspecialidade(r.getInt("idEspecialidade"));
				especialidade.setNome(r.getString("nome"));
				especialidade.setDescricao(r.getString("descricao"));
				
				precoEspecialidade.setDataInicio(toCalendar(r.getDate("dataInicio").getTime()));
				precoEspecialidade.setDataFim(toCalendar(r.getDate("dataFim").getTime()));
				precoEspecialidade.setValor(r.getDouble("valor"));
				
				especialidade.setPrecoEspecialidade(precoEspecialidade);
				
				especialidades.add(especialidade);
			} 
			
			sql.close();
			return especialidades;
		}
		catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	public void alterar(Especialidade especialidade) { // método para realizar uma alteração
		
		String query = "UPDATE especialidade SET descricao = ? " +
					   "WHERE idEspecialidade = ?;";
		
		String query1 = "UPDATE valorEspecialidade SET dataInicio = ?, dataFim = ?, valor = ? " +
		   "WHERE idValorEspecialidade = ?;";
		
		try {
		    c.setAutoCommit(false);
			
			sql = c.prepareStatement(query);
			
			sql.setString(1,especialidade.getDescricao());
			sql.setInt(2,especialidade.getIdEspecialidade());
			
			sql.executeUpdate();
			
			sql = c.prepareStatement(query1);
			
			sql.setDate(1, toDate(especialidade.getPrecoEspecialidade().getDataInicio()));
			sql.setDate(2,toDate(especialidade.getPrecoEspecialidade().getDataFim()));
			sql.setDouble(3, especialidade.getPrecoEspecialidade().getValor());
			
			sql.executeUpdate();
			
			c.commit();
			
			sql.close();
		} catch(Exception e){
			System.out.println("Erro alterar: " + e);
		}
	}
	
	public void excluir(Especialidade especialidade) { // método para realizar uma exclusão
		
		String query = "DELETE FROM especialidade WHERE idEspecialidade = ?;";
		String query1 = "DELETE FROM valorEspecialidade WHERE idValorEspecialidade = ?;";
		String query2 = "DELETE FROM vigencia WHERE idVigencia = ?;";
		
		try {
		    c.setAutoCommit(false);
			
			sql = c.prepareStatement(query);
			
			sql.setInt(1,especialidade.getIdEspecialidade());
			
			sql.executeUpdate();

			sql = c.prepareStatement(query1);
			
			sql.setInt(1,especialidade.getIdEspecialidade());
			
			sql.executeUpdate();
			
			sql = c.prepareStatement(query2);
			
			sql.setInt(1,especialidade.getIdEspecialidade());
			
			sql.executeUpdate();
			
			c.commit();
			
			sql.close();
		} catch(Exception e) {
			System.out.println("Erro excluir: " + e);
		}
	}

	public ArrayList<Especialidade> listaEspecialidadeCombo() { // método para retornar todas as especialidades
		
		String query = "SELECT e.idEspecialidade, e.nome " +
					   "FROM especialidade AS e " +
					   "ORDER BY e.nome;";
		
		ArrayList<Especialidade> listaPlanoSaude = new ArrayList<Especialidade>();
		
		try {
			sql = c.prepareStatement(query);
			
			r = sql.executeQuery(); //recebe a consulta
			
			while(r.next()) {
				Especialidade especialidade = new Especialidade();
				
				especialidade.setIdEspecialidade(r.getInt("idEspecialidade"));
				especialidade.setNome(r.getString("nome"));
				
				listaPlanoSaude.add(especialidade);
			}
			
			sql.close();
			return listaPlanoSaude;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	
	public int qtdRegistro() { // conta a quantidade de registros na tabela
		
		String query = "SELECT count(*) " +
					   "FROM especialidade AS e;";
		
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
	
	//Converte o tipo CALENDAR em DATE
	private Date toDate(Calendar calendar) {
		Date data = new Date(calendar.getTimeInMillis());
		
		return data;
	}

	//Converte o tipo TIME em CALENDAR
	private Calendar toCalendar(long hora) {
		Calendar calendario = new GregorianCalendar();
		
		calendario.setTimeInMillis(hora);
		
		return calendario;
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