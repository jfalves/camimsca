package dao;

import implementacao.Endereco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.*;

public class DAOFuncionario {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOFuncionario() {
		try {
			//carregar o driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//funcionarioazer a conexão
			String url = "jdbc:mysql://localhost:3306/sca_camim";
			c = DriverManager.getConnection(url,"root","mysql");
			
			sql = null;
			r = null;
		}
		catch(Exception e) {
			System.out.println("ERRO :" + e);
		}
	}
	
	//Método para a carga do objeto Funcionario
	public Funcionario carregaFuncionario(Funcionario funcionario) {
		String query = "SELECT f.ctps, f.dataAdmissao, f.idStatusFuncionario " +
					   "FROM funcionario AS f " +
					   "WHERE f.idFuncionario = ?;";
		
		try {
			sql = c.prepareStatement(query);
			sql.setInt(1, funcionario.getIdPessoa());
			
			r = sql.executeQuery(); //recebe a consulta
			
			if(r.next()) {
				funcionario.setCtps(r.getString("ctps"));
				funcionario.setDataAdmissao(toCalendar(r.getDate("dataAdmissao").getTime()));
				
				sql.close();
				return funcionario;
			} else {
				sql.close();
				return null;
			}
		} catch(Exception ex) {
			System.out.println("Erro: " + ex);
			return null;
		}		
	}
	
	public void cadastrar(Funcionario funcionario) { // método para realizar um cadastro 
		String query = "BEGIN;" +
					"START TRANSACTION;" +
					"INSERT INTO endereco (logradouro,complemento,bairro,cep,cidade,estado)VALUES(?,?,?,?,?,?);" +
					"INSERT INTO pessoa VALUES(?,?,?,?,?,?,?,?,LAST_INSERT_ID())" +
					"COMMIT;" +
					"ROLLBACK;";
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, funcionario.getEndereco().getLogradouro());
			sql.setString(2, funcionario.getEndereco().getComplemento());
			sql.setString(3, funcionario.getEndereco().getBairro());
			sql.setString(4, funcionario.getEndereco().getCep());
			sql.setString(5, funcionario.getEndereco().getCidade());
			
			sql.setString(7,funcionario.getMatricula());
			sql.setString(8,funcionario.getSenha());
			sql.setString(9,funcionario.getNome());
			sql.setString(10,funcionario.getCpf());
			sql.setString(11,funcionario.getRg());
			sql.setString(12,funcionario.getTelefoneResidencia());
			sql.setString(13,funcionario.getTelefoneCelular());
			//sql.setDate	 (14,(Date)funcionario.getDataNascimento());
			
			// executar o sql
			sql.executeUpdate();
			sql.close();
					
			//insere o último registro cadastrado de pessoa na tabela cliente
			sql = c.prepareStatement("INSERT INTO funcionariouncionario (idfuncionariouncionario,funcionariouncao,ctps,dataAdmissao," +
					"dataDemissao,idStatusfuncionariouncionario) " +
					"VALUES (LAST_INSERT_ID()),");
			
			// executar o sql
			sql.executeUpdate();
			sql.close();
			
		} catch(Exception e){
			System.out.println("Erro: " + e);
		}			
	}
	
	public Funcionario consultaMatricula(Funcionario funcionario) { // método para realizar uma consulta
		Endereco endereco = new Endereco();
		
		String query = "SELECT p.idPessoa, p.senha, p.nome, p.cpf, p.rg, " +
						"p.telefoneResidencia, p.telefoneCelular, p.dataNascimento, p.tipo, e.idEndereco, e.logradouro, " +
						"e.complemento, e.bairro, e.cep, e.cidade " +
						"FROM pessoa AS p " +
						"INNER JOIN endereco AS e ON p.idEndereco = e.idEndereco " +
						"WHERE p.matricula = ? and p.tipo = 'F';";
		
		try {
			
			sql = c.prepareStatement(query);
			
			sql.setString(1, funcionario.getMatricula());
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {
				funcionario.setIdFuncionario(r.getInt("idPessoa"));
				funcionario.setSenha(r.getString("senha"));
				funcionario.setNome(r.getString("nome"));
				funcionario.setCpf(r.getString("cpf"));
				funcionario.setRg(r.getString("rg"));
				funcionario.setTelefoneResidencia(r.getString("telefoneResidencia"));
				funcionario.setTelefoneCelular(r.getString("telefoneCelular"));
				funcionario.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));
				funcionario.setTipo(r.getString("tipo"));
				
				endereco.setIdEndereco(r.getInt("idEndereco"));
				endereco.setLogradouro(r.getString("logradouro"));
				endereco.setComplemento(r.getString("complemento"));
				endereco.setBairro(r.getString("bairro"));
				endereco.setCep(r.getString("cep"));
				endereco.setCidade(r.getString("cidade"));
				
				funcionario.setEndereco(endereco);
				
				sql.close();
				return funcionario;
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
	
	public void listaStatusFuncionario() {
		String query = "SELECT nome funcionarioROM statusfuncionariouncionario ORDER BY idStatusfuncionariouncionario;";
		try {
			sql = c.prepareStatement(query);
			
			sql.executeUpdate();
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
		} catch(Exception ex) {
			System.out.println("Erro: " + ex);
		}
	}
}
