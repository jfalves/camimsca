package dao;

import implementacao.Endereco;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.*;

public class DAOCliente {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOCliente() {
		
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
	
	public void cadastrar(Pessoa pessoa) { // método para realizar um cadastro 
		String query   = "INSERT INTO endereco (logradouro, complemento, bairro, cep, cidade) VALUES(?,?,?,?,?)";
		String query1  = "INSERT INTO pessoa(matricula,senha,nome,cpf,rg,telefoneResidencia,telefoneCelular,dataNascimento,tipo,idEndereco)  VALUES(?,?,?,?,?,?,?,?,'C',LAST_INSERT_ID());";
		String query2  = "INSERT INTO cliente(idCliente) VALUES(LAST_INSERT_ID());";
		try {
		    c.setAutoCommit(false);

			sql = c.prepareStatement(query);
			
			sql.setString(1,pessoa.getEndereco().getLogradouro());
			sql.setString(2,pessoa.getEndereco().getComplemento());
			sql.setString(3,pessoa.getEndereco().getBairro());
			sql.setString(4,pessoa.getEndereco().getCep());
			sql.setString(5,pessoa.getEndereco().getCidade());
			
			// executar o sql
			sql.executeUpdate();
			
			sql = c.prepareStatement(query1);
			
			sql.setString(1,pessoa.getMatricula());
			sql.setString(2,pessoa.getSenha());
			sql.setString(3,pessoa.getNome());
			sql.setString(4,pessoa.getCpf());
			sql.setString(5,pessoa.getRg());
			sql.setString(6,pessoa.getTelefoneResidencia());
			sql.setString(7,pessoa.getTelefoneCelular());
			
			String dateFormat = "";   
			java.sql.Date bisDatum = null;   

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");               
  
			dateFormat = sdf.format(pessoa.getDataNascimento().getTime());   
			bisDatum = new java.sql.Date(sdf.parse(dateFormat).getTime());   

			sql.setDate(8,bisDatum);
			
			// executar o sql
			sql.executeUpdate();

			sql = c.prepareStatement(query2);
			
			//executar o sql
			sql.executeUpdate();
			
			c.commit();
		    
			sql.close();

		} catch(Exception e){
			System.out.println("Erro: " + e);
		}
	}
	
	public Cliente consultar(Cliente cliente) { // método para realizar uma consulta
		
		Endereco endereco = new Endereco();
		
		String query = "SELECT p.idPessoa, p.matricula, p.senha, p.nome, p.cpf, p.rg, " +
						"p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, e.idEndereco, e.logradouro, " +
						"e.complemento, e.bairro, e.cep, e.cidade " +
						"FROM pessoa AS p " +
						"INNER JOIN endereco as e ON p.idEndereco = e.idEndereco " +
						"WHERE p.matricula = ? and p.tipo = 'C';";
		
		try {
			
			sql = c.prepareStatement(query);
			
			sql.setString(1, cliente.getMatricula());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				
				cliente.setIdCliente(r.getInt("idPessoa"));
				cliente.setMatricula(r.getString("matricula"));
				cliente.setSenha(r.getString("senha"));
				cliente.setNome(r.getString("nome"));
				cliente.setCpf(r.getString("cpf"));
				cliente.setRg(r.getString("rg"));
				cliente.setTelefoneResidencia(r.getString("telefoneResidencia"));
				cliente.setTelefoneCelular(r.getString("telefoneCelular"));
				cliente.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));
				
				endereco.setIdEndereco(r.getInt("idEndereco"));
				endereco.setLogradouro(r.getString("logradouro"));
				endereco.setComplemento(r.getString("complemento"));
				endereco.setBairro(r.getString("bairro"));
				endereco.setCep(r.getString("cep"));
				endereco.setCidade(r.getString("cidade"));
				
				cliente.setEndereco(endereco);
				
				sql.close();
				return cliente;
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
	
	public int consultaQtdDependente(Cliente cliente) { // método para realizar uma consulta
		
		String query = "SELECT count(*) as contador FROM cliente as c " +
				"INNER JOIN dependente as d ON c.idCliente = d.idCliente " +
				"where c.idCliente = ?;";
		
		try {
			
			sql = c.prepareStatement(query);
			
			sql.setInt(1, cliente.getIdCliente());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				
				int contador = r.getInt("contador");
				
				sql.close();
				return contador;
			} else {
				sql.close();
				return -1;
			}
		}
		catch(Exception ex) {
			System.out.println("Erro: " + ex );
			return -1;
		}
	}	
	
	public Cliente consultaMatricula(Cliente cliente) { // método para realizar uma consulta
		Endereco endereco = new Endereco();
		
		try {
			sql = c.prepareStatement("SELECT p.idPessoa, p.matricula, p.senha, p.nome, p.cpf, p.rg," +
					"p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, e.idEndereco, e.logradouro, " +
					"e.complemento, e.bairro, e.cep, e.cidade " +
					"FROM pessoa AS p " +
					"INNER JOIN endereco AS e ON p.idEndereco = e.idEndereco " +
					"WHERE p.matricula = ? AND p.tipo = 'C';");
			
			sql.setString(1, cliente.getMatricula());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				cliente.setIdCliente(r.getInt("idPessoa"));
				cliente.setMatricula(r.getString("matricula"));
				cliente.setSenha(r.getString("senha"));
				cliente.setNome(r.getString("nome"));
				cliente.setCpf(r.getString("cpf"));
				cliente.setRg(r.getString("rg"));
				cliente.setTelefoneResidencia(r.getString("telefoneResidencia"));
				cliente.setTelefoneCelular(r.getString("telefoneCelular"));
				cliente.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));

				endereco.setIdEndereco(r.getInt("idEndereco"));
				endereco.setLogradouro(r.getString("logradouro"));
				endereco.setComplemento(r.getString("complemento"));
				endereco.setBairro(r.getString("bairro"));
				endereco.setCep(r.getString("cep"));
				endereco.setCidade(r.getString("cidade"));
				
				cliente.setEndereco(endereco);
				
				sql.close();
				return cliente;
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
	
	public ArrayList<Cliente> consultarTodos(int start, int limit) { // método para retornar todas as especialidades
		
		String query = "SELECT * " +
					   "FROM pessoa AS p " +
					   "INNER JOIN cliente as c ON p.idPessoa = c.idCliente " +
					   "LIMIT ?, ?;";
		
		ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,start);
			sql.setInt(2,limit);
			
			r = sql.executeQuery(); //recebe a consulta
			
			while(r.next()) {
				Cliente cliente = new Cliente();
				
				cliente.setIdCliente(r.getInt("idPessoa"));
				cliente.setMatricula(r.getString("matricula"));
				cliente.setSenha(r.getString("senha"));
				cliente.setNome(r.getString("nome"));
				cliente.setCpf(r.getString("cpf"));
				cliente.setRg(r.getString("rg"));
				cliente.setTelefoneResidencia(r.getString("telefoneResidencia"));
				cliente.setTelefoneCelular(r.getString("telefoneCelular"));
				cliente.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));
				
				listaCliente.add(cliente);
			}
			
			sql.close();
			return listaCliente;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	public void alterar(Cliente cliente) { // método para realizar uma alteraração
		
		String query = "UPDATE pessoa SET nome = ?, telefoneResidencia = ?, " +
					   "telefoneCelular = ?, dataNascimento = ?, rg = ?, cpf = ? " +
					   "WHERE idPessoa = ?;";
		
		String query1 = "UPDATE endereco as e SET logradouro = ?, complemento = ?, bairro = ?, " +
						"cep = ?, cidade = ?  " +
						"WHERE idEndereco = ?;";
		
		try {
		    c.setAutoCommit(false);
		    
			sql = c.prepareStatement(query);
			
			sql.setString(1,cliente.getNome());
			sql.setString(2,cliente.getTelefoneResidencia());
			sql.setString(3,cliente.getTelefoneCelular());
			
			String dateFormat = "";   
			java.sql.Date bisDatum = null;   

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");               
			if(cliente.getDataNascimento()!=null){   
			        dateFormat = sdf.format(cliente.getDataNascimento().getTime());   
			        bisDatum = new java.sql.Date(sdf.parse(dateFormat).getTime());   
			    } 
			
			sql.setDate(4,bisDatum);
			sql.setString(5,cliente.getRg());
			sql.setString(6,cliente.getCpf());
			sql.setInt(7,cliente.getIdCliente());
			
			sql.executeUpdate();
			
			
			sql = c.prepareStatement(query1);
			
			sql.setString(1,cliente.getEndereco().getLogradouro());
			sql.setString(2,cliente.getEndereco().getComplemento());
			sql.setString(3,cliente.getEndereco().getBairro());
			sql.setString(4,cliente.getEndereco().getCep());
			sql.setString(5,cliente.getEndereco().getCidade());
			sql.setInt(6,cliente.getEndereco().getIdEndereco());
			
			sql.executeUpdate();
			
			c.commit();
			
			sql.close();
		} catch(Exception e){
			System.out.println("Erro Alterar: " + e);
		}
	}
	
	public void excluir(Cliente cliente) { // método para realizar uma exclusão
		try {
		    c.setAutoCommit(false);
		    
			sql = c.prepareStatement("DELETE FROM pessoa WHERE idPessoa = ?;");
			
			sql.setInt(1,cliente.getIdCliente());
			
			sql.executeUpdate();
			
			sql = c.prepareStatement("DELETE FROM cliente WHERE idCliente = ?;");
			
			sql.setInt(1,cliente.getIdCliente());
			
			sql.executeUpdate();
			
			sql = c.prepareStatement("DELETE FROM endereco WHERE idEndereco = ?;");
			
			sql.setInt(1,cliente.getEndereco().getIdEndereco());
			
			sql.executeUpdate();
			
			c.commit();
			
			sql.close();
		} catch(Exception e) {
			System.out.println("Erro: " + e);
		}
	}

	public ArrayList<Pessoa> listaCliente(Pessoa pessoa) { // método para realizar uma consulta
		Endereco endereco = new Endereco();
		
		String query = "SELECT p.matricula, p.nome, p.cpf, p.rg, p.dataNascimento, " +
					   "p.telefoneResidencia, p.telefoneCelular, e.logradouro, " +
					   "e.complemento, e.bairro, e.cep, e.cidade " +
					   "FROM pessoa as p " +
					   "INNER JOIN endereco as e ON p.idEndereco = e.idEndereco " +
					   "WHERE p.nome like ? and p.tipo = 'C';";
					   
		ArrayList<Pessoa> listaCliente = new ArrayList<Pessoa>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, pessoa.getNome());
			
			r = sql.executeQuery(); //recebe a consulta
	
			while(r.next()) {
				Pessoa cliente = new Cliente();
				
				cliente.setMatricula(r.getString("matricula"));
				cliente.setSenha(r.getString("senha"));
				cliente.setNome(r.getString("nome"));
				cliente.setCpf(r.getString("cpf"));
				cliente.setRg(r.getString("rg"));
				cliente.setTelefoneResidencia(r.getString("telefoneResidencia"));
				cliente.setTelefoneCelular(r.getString("telefoneCelular"));
				cliente.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));
				
				endereco.setIdEndereco(r.getInt("idEndereco"));
				endereco.setLogradouro(r.getString("logradouro"));
				endereco.setComplemento(r.getString("complemento"));
				endereco.setBairro(r.getString("bairro"));
				endereco.setCep(r.getString("cep"));
				endereco.setCidade(r.getString("cidade"));
				
				cliente.setEndereco(endereco);
				
				listaCliente.add(cliente);
			}
			
			sql.close();
			return listaCliente;
		}
		catch(Exception ex) {
			System.out.println("Erro: " + ex );
			return null;
		}
	}

	public int qtdRegistro() { // conta a quantidade de registros na tabela
		
		String query = "SELECT count(*) " +
					   "FROM cliente AS c;";
		
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

