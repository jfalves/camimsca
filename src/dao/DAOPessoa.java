package dao;

import implementacao.Endereco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import modelo.Pessoa;

public class DAOPessoa {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOPessoa() {
		try {
			//carregar o driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//fazer a conexão
			String url = "jdbc:mysql://localhost:3306/sca_camim";
			c = DriverManager.getConnection(url,"root","mysql");
			sql = null;
			r = null;
		}
		catch(Exception ex) {
			System.out.println("Erro:" + ex);
		}
	}
	
	//Método para a carga do objeto Pessoa
	public Pessoa carregaPessoa(Pessoa pessoa) {
		String query = "SELECT p.idPessoa, p.matricula, p.senha, p.nome, p.cpf, p.rg, " +
					   "p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, p.tipo, " +
					   "e.idEndereco, e.logradouro, e.complemento, e.bairro, e.cep, e.cidade " +
					   "FROM pessoa AS p INNER JOIN endereco AS e ON p.idEndereco = e.idEndereco " +
					   "WHERE p.matricula = ? AND p.senha = ?;";
		
		Endereco endereco = new Endereco();
		
		try {
			sql = c.prepareStatement(query);
			sql.setString(1, pessoa.getMatricula());
			sql.setString(2, pessoa.getSenha());
			
			r = sql.executeQuery(); //recebe a consulta
			
			if(r.next()) {
				pessoa.setIdPessoa(r.getInt("idPessoa"));
				pessoa.setMatricula(r.getString("matricula"));
				pessoa.setSenha(r.getString("senha"));
				pessoa.setNome(r.getString("nome"));
				pessoa.setCpf(r.getString("cpf"));
				pessoa.setRg(r.getString("rg"));
				pessoa.setTelefoneResidencia(r.getString("telefoneResidencia"));
				pessoa.setTelefoneCelular(r.getString("telefoneCelular"));
				//pessoa.setDataNascimento(r.getDate("dataNascimento"));
				pessoa.setTipo(r.getString("tipo"));
				
				endereco.setIdEndereco(r.getInt("idEndereco"));
				endereco.setLogradouro(r.getString("logradouro"));
				endereco.setComplemento(r.getString("complemento"));
				endereco.setBairro(r.getString("bairro"));
				endereco.setCep(r.getString("cep"));
				endereco.setCidade(r.getString("cidade"));
				
				pessoa.setEndereco(endereco);
				
				sql.close();
				return pessoa;
			} else {
				sql.close();
				return null;
			}
		} catch(Exception ex) {
			System.out.println("Erro: " + ex);
			return null;
		}		
	}
	
	public ArrayList<Pessoa> pesquisar(String nome) {
		String query = "SELECT p.idPessoa, p.matricula, p.senha, p.nome, p.cpf, p.rg, " +
					   "p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, p.tipo, " +
					   "FROM pessoa AS p " +
					   "WHERE p.nome like ?";
		
		ArrayList<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		Pessoa pessoa = new Pessoa();
		
		try {
			sql = c.prepareStatement(query);
			sql.setString(1, nome);
			
			r = sql.executeQuery(); //recebe a consulta
			
			while(r.next()) {
				pessoa.setIdPessoa(r.getInt("idPessoa"));
				pessoa.setMatricula(r.getString("matricula"));
				pessoa.setSenha(r.getString("senha"));
				pessoa.setNome(r.getString("nome"));
				pessoa.setCpf(r.getString("cpf"));
				pessoa.setRg(r.getString("rg"));
				pessoa.setTelefoneResidencia(r.getString("telefoneResidencia"));
				pessoa.setTelefoneCelular(r.getString("telefoneCelular"));
				//pessoa.setDataNascimento(r.getDate("dataNascimento"));
				pessoa.setTipo(r.getString("tipo"));
				
				listaPessoa.add(pessoa);
			}
			
			sql.close();
			return listaPessoa;
			
		} catch(Exception ex) {
			System.out.println("Erro: " + ex);
			return null;
		}		
	}
	
	public boolean verificaMatricula(String matricula) {
		String query = "SELECT * " +
		   				"FROM pessoa AS p " +
		   				"WHERE p.matricula = ?";
		
		try {
			sql = c.prepareStatement(query);
			sql.setString(1, matricula);
			
			r = sql.executeQuery(); //recebe a consulta
			
			if(r.next()) {
				return true;
			} else {
				return false;
			}
			
		} catch(Exception ex) {
			System.out.println("Erro: " + ex);
			return false;
		}		
	}
}
