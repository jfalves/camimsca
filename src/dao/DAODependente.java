package dao;

import implementacao.Endereco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.Cliente;
import modelo.Dependente;

public class DAODependente extends DAOPessoa {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAODependente() {	
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

	public void cadastrar(Dependente dependente) { // método para realizar um cadastro 
		String query  =	"INSERT INTO pessoa(matricula,nome,cpf,rg,telefoneResidencia,telefoneCelular,dataNascimento,tipo,idEndereco)  VALUES(?,?,?,?,?,?,?,'D',?);";
		String query1 = "INSERT INTO dependente(idDependente, idCliente)  VALUES( LAST_INSERT_ID(),?);";
		try {
		    c.setAutoCommit(false);

			sql = c.prepareStatement(query);

			sql.setString(1,dependente.getMatricula());
			sql.setString(2,dependente.getNome());
			sql.setString(3,dependente.getCpf());
			sql.setString(4,dependente.getRg());
			sql.setString(5,dependente.getTelefoneResidencia());
			sql.setString(6,dependente.getTelefoneCelular());
			
			String dateFormat = "";   
			java.sql.Date bisDatum = null;   
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");               
			
			dateFormat = sdf.format(dependente.getDataNascimento().getTime());   
			bisDatum = new java.sql.Date(sdf.parse(dateFormat).getTime());     
			
			sql.setDate(7,bisDatum);
			sql.setInt(8, dependente.getEndereco().getIdEndereco());
			
			// executar o sql
			sql.executeUpdate();

			sql = c.prepareStatement(query1);

			sql.setInt(1, dependente.getCliente().getIdCliente());
			
			sql.executeUpdate();	
			
			c.commit();
		    
			sql.close();

		} catch(Exception e){
			System.out.println("Erro: " + e);
		}
	}

	public Dependente consultar(Dependente dependente) { // método para realizar uma consulta
		
		Calendar dataNascimento = new GregorianCalendar();
		
		Cliente cliente = new Cliente();
		
		String query = "SELECT p.idPessoa, p.matricula, p.senha, p.nome, p.cpf, p.rg, " +
						"p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, c.matricula AS matriculaTitular " +
						"FROM pessoa AS p " +
						"INNER JOIN dependente AS d ON d.idDependente = p.idPessoa " +
						"INNER JOIN pessoa AS c ON d.idCliente = c.idPessoa " +
						"WHERE p.matricula = ? and p.tipo = 'D';";

		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, dependente.getMatricula());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				dependente.setIdDependente(r.getInt("idPessoa"));
				dependente.setMatricula(r.getString("matricula"));
				dependente.setSenha(r.getString("senha"));
				dependente.setNome(r.getString("nome"));
				dependente.setCpf(r.getString("cpf"));
				dependente.setRg(r.getString("rg"));
				dependente.setTelefoneResidencia(r.getString("telefoneResidencia"));
				dependente.setTelefoneCelular(r.getString("telefoneCelular"));
				
				dataNascimento.setTime(r.getDate("dataNascimento"));				
				dependente.setDataNascimento(dataNascimento);
				
				cliente.setMatricula(r.getString("matriculaTitular"));				
				dependente.setCliente(cliente);
				
				sql.close();
				return dependente;
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

	public Dependente consultaMatricula(Dependente dependente) { // método para realizar uma consulta
		Endereco endereco = new Endereco();
		Cliente cliente = new Cliente();
		
		try {
			sql = c.prepareStatement("SELECT p.idPessoa, p.matricula, p.senha, p.nome, p.cpf, p.rg," +
					"p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, e.idEndereco, e.logradouro, " +
					"e.complemento, e.bairro, e.cep, e.cidade, d.idCliente " +
					"FROM pessoa AS p " +
					"INNER JOIN endereco AS e ON p.idEndereco = e.idEndereco " +
					"INNER JOIN dependente as d ON p.idPessoa = d.idDependente " +
					"WHERE p.matricula = ? AND p.tipo = 'D';");
			
			sql.setString(1, dependente.getMatricula());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				dependente.setIdDependente(r.getInt("idPessoa"));
				dependente.setMatricula(r.getString("matricula"));
				dependente.setSenha(r.getString("senha"));
				dependente.setNome(r.getString("nome"));
				dependente.setCpf(r.getString("cpf"));
				dependente.setRg(r.getString("rg"));
				dependente.setTelefoneResidencia(r.getString("telefoneResidencia"));
				dependente.setTelefoneCelular(r.getString("telefoneCelular"));
				dependente.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));
				
				endereco.setIdEndereco(r.getInt("idEndereco"));
				endereco.setLogradouro(r.getString("logradouro"));
				endereco.setComplemento(r.getString("complemento"));
				endereco.setBairro(r.getString("bairro"));
				endereco.setCep(r.getString("cep"));
				endereco.setCidade(r.getString("cidade"));
				
				cliente.setIdCliente(r.getInt("idCliente"));
				
				dependente.setEndereco(endereco);
				dependente.setCliente(cliente);
				
				sql.close();
				return dependente;
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

	
	public void alterar(Dependente dependente) { // método para realizar uma alteraração
		
		String query = "UPDATE pessoa SET nome = ?, telefoneResidencia = ?, " +
					   "telefoneCelular = ?, dataNascimento = ?, rg = ?, cpf = ? " +
					   "WHERE idPessoa = ?;";
		
		try {
			
			sql = c.prepareStatement(query);
			
			sql.setString(1,dependente.getNome());
			sql.setString(2,dependente.getTelefoneResidencia());
			sql.setString(3,dependente.getTelefoneCelular());
			
			String dateFormat = "";   
			java.sql.Date bisDatum = null;   
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");               
			
			dateFormat = sdf.format(dependente.getDataNascimento().getTime());   
			bisDatum = new java.sql.Date(sdf.parse(dateFormat).getTime());  
			
			sql.setDate(4,bisDatum);
			sql.setString(5,dependente.getRg());
			sql.setString(6,dependente.getCpf());
			sql.setInt(7,dependente.getIdPessoa());

			sql.executeUpdate();
			sql.close();
			
		} catch(Exception e){
			System.out.println("Erro Alterar: " + e);
		}
	}
	
	public void excluir(Dependente dependente) { // método para realizar uma exclusão
		try {
		    c.setAutoCommit(false);
		    
			sql = c.prepareStatement("DELETE FROM pessoa WHERE idPessoa = ?;");
			
			sql.setInt(1,dependente.getIdPessoa());
			
			sql.executeUpdate();
			
			sql = c.prepareStatement("DELETE FROM dependente WHERE idDependente = ?;");
			
			sql.setInt(1,dependente.getIdPessoa());
			
			sql.executeUpdate();
			
			c.commit();
			
			sql.close();
		} catch(Exception e) {
			System.out.println("Erro: " + e);
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
