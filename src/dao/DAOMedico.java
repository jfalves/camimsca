package dao;

import implementacao.Endereco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.Medico;
import modelo.Pessoa;

public class DAOMedico {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOMedico() {	
		try {
			//carregar o driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//fazer a conexão
			String url = "jdbc:mysql://localhost:3306/sca_camim";
			c = DriverManager.getConnection(url,"root","mysql");
			
			sql = null;
			r = null;
		}
		catch(Exception e) {
			System.out.println("ERRO :" + e);
		}
	} 

	public Medico consultaMedicoNome(Medico medico) { // método para realizar uma consulta
		try {
			sql = c.prepareStatement("SELECT p.idPessoa, p.matricula, p.nome " +
					"FROM pessoa AS p " +
					"WHERE p.nome = ?;");
			
			sql.setString(1, medico.getNome());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				medico.setIdMedico(r.getInt("idPessoa"));
				medico.setMatricula(r.getString("matricula"));
				medico.setNome(r.getString("nome"));
				
				sql.close();
				return medico;
			} else {
				sql.close();
				return null;
			}
		}
		catch(Exception ex) {
			System.out.println("Erro: " + ex );
			return null;
		}
	}
	
	public Medico consultaMatricula(Medico medico) { // método para realizar uma consulta
		Endereco endereco = new Endereco();
		
		String query = "SELECT p.idPessoa, p.senha, p.nome, p.cpf, p.rg, " +
						"p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, p.tipo, e.idEndereco, e.logradouro, " +
						"e.complemento, e.bairro, e.cep, e.cidade " +
						"FROM pessoa AS p " +
						"INNER JOIN endereco AS e ON p.idEndereco = e.idEndereco " +
						"WHERE p.matricula = ? and p.tipo = 'M';";
		
		try {
			
			sql = c.prepareStatement(query);
			
			sql.setString(1, medico.getMatricula());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				medico.setIdMedico(r.getInt("idPessoa"));
				medico.setSenha(r.getString("senha"));
				medico.setNome(r.getString("nome"));
				medico.setCpf(r.getString("cpf"));
				medico.setRg(r.getString("rg"));
				medico.setTelefoneResidencia(r.getString("telefoneResidencia"));
				medico.setTelefoneCelular(r.getString("telefoneCelular"));
				medico.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));
				medico.setTipo(r.getString("tipo"));
				
				endereco.setIdEndereco(r.getInt("idEndereco"));
				endereco.setLogradouro(r.getString("logradouro"));
				endereco.setComplemento(r.getString("complemento"));
				endereco.setBairro(r.getString("bairro"));
				endereco.setCep(r.getString("cep"));
				endereco.setCidade(r.getString("cidade"));
				
				medico.setEndereco(endereco);
				
				sql.close();
				return medico;
			} else {
				sql.close();
				return null;
			}
		}
		catch(Exception ex) {
			System.out.println("Erro funcionario: " + ex );
			return null;
		}
	}
	
	public ArrayList<Pessoa> listaMedicoCombo(String especialidade) { // método para retornar todas as especialidades
		
		String query = "SELECT p.nome " +
					   "FROM medico AS m " +
					   "INNER JOIN especialidade as e ON m.idEspecialidade = e.idEspecialidade " +
					   "INNER JOIN pessoa as p ON m.idMedico = p.idPessoa " +
					   "WHERE e.nome = ? " +
					   "ORDER BY p.nome;";
		
		ArrayList<Pessoa> listaMedicoCombo = new ArrayList<Pessoa>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1,especialidade);
			
			r = sql.executeQuery(); //recebe a consulta
			
			
			while(r.next()) {
				Pessoa pessoa = new Medico();
				
				pessoa.setNome(r.getString("nome"));
				
				listaMedicoCombo.add(pessoa);
			}
			
			sql.close();
			return listaMedicoCombo;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
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
