package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import modelo.*;

public class DAOPagamentoConsulta {

	//Cria objetos SQL
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOPagamentoConsulta() {
		
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

	public PagamentoConsulta consultaValor(PagamentoConsulta pagamentoConsulta) {
		
		String query = "SELECT p.idPagamentoConsulta, p.valor " +
		   "FROM pagamentoConsulta AS p " +
		   "WHERE p.idAtendimento = ?";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,pagamentoConsulta.getAtendimento().getIdAtendimento());
			
			r = sql.executeQuery(); //recebe a consulta	
			
			if(r.next()) {
			    
				pagamentoConsulta.setIdPagamentoConsulta(r.getInt("idPagamentoConsulta"));
				pagamentoConsulta.setValor(r.getDouble("valor"));
			}
			
			sql.close();
			return pagamentoConsulta;
			
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	public void geraPagamentoConsulta(Atendimento atendimento) {
		
		String query = "INSERT INTO pagamentoConsulta (idPagamentoConsulta, data, hora, valor, idFuncionario, idAtendimento, idStatusPagamentoConsulta, idFormaPagamento) " +
				"VALUES (?, null, null, (select valor from valorEspecialidade where dataFim is null AND idValorEspecialidade in (select idEspecialidade from medico where idMedico = ?))" +
				", null, ?, 1, null);";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,atendimento.getIdAtendimento());
			sql.setInt(2,atendimento.getMedico().getIdMedico());
			sql.setInt(3,atendimento.getIdAtendimento());
			
			// executar o sql
			sql.executeUpdate();

			
			sql.close();
			
		} catch(Exception ex) {
			System.out.println("Erro Insert: " + ex );
		}
	}
	
	public ArrayList<PagamentoConsulta> listaFormaPagamento() { // método para retornar todas os tipos de plano de saude
		
		String query = "SELECT fp.nome " +
					   "FROM formaPagamento AS fp " +
					   "ORDER BY fp.nome;";
		
		ArrayList<PagamentoConsulta> listaFormaPagamento = new ArrayList<PagamentoConsulta>();
		
		try {
			sql = c.prepareStatement(query);
			
			r = sql.executeQuery(); //recebe a consulta
			
			while(r.next()) {
				PagamentoConsulta pagamentoConsulta = new PagamentoConsulta();
				
				pagamentoConsulta.setFormaPagamento(r.getString("nome"));
				
				listaFormaPagamento.add(pagamentoConsulta);
			}
			
			sql.close();
			return listaFormaPagamento;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}

	public void realizaPagamento (PagamentoConsulta pagamentoConsulta){
		
		String query = "UPDATE pagamentoConsulta " +
					   "SET data=CURDATE(), hora=CURTIME(), idFuncionario=?, idStatusPagamentoConsulta=2, " +
					   "idFormaPagamento=(select idFormaPagamento from formaPagamento where nome=?) " +
					   "WHERE idPagamentoConsulta=?;";
		
		try {
		    
			sql = c.prepareStatement(query);
			
			sql.setInt(1,pagamentoConsulta.getFuncionario().getIdFuncionario());
			sql.setString(2,pagamentoConsulta.getFormaPagamento());
			sql.setInt(3,pagamentoConsulta.getIdPagamentoConsulta());
			
			sql.executeUpdate();
			
			sql.close();
		} catch(Exception e){
			System.out.println("Erro Alterar: " + e);
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
