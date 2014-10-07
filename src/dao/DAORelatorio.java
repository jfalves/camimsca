package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.Cliente;
import modelo.Especialidade;
import modelo.Medico;

public class DAORelatorio {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAORelatorio() {	
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

	public ArrayList<Cliente> listaCliente(Especialidade especialidade, Calendar dataInicio, Calendar dataFim) { // método para realizar uma consulta
		
		String query = "select dataNascimento from pessoa as p " +
						"inner join atendimento as a on a.idCliente = p.idPessoa or a.idDependente = p.idPessoa " +
						"inner join medico as m on m.idMedico = a.idMedico " +
						"inner join  especialidade as e on e.idEspecialidade = m.idEspecialidade " +
						"where e.nome = ? and a.data between ? and ? " +
						"group by idPessoa;";
					   
		ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, especialidade.getNome());
			sql.setDate(2, toDate(dataInicio));
			sql.setDate(3, toDate(dataFim));
			
			r = sql.executeQuery(); //recebe a consulta
	
			while(r.next()) {
				Cliente cliente = new Cliente();
				
				cliente.setDataNascimento(toCalendar(r.getDate("dataNascimento").getTime()));
				
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
	
	public ArrayList<String> listaEspecialidade(Calendar dataInicio, Calendar dataFim) { // método para realizar uma consulta
		
		String query = "select e.nome, count(a.idAtendimento) as qtd " +
				"from atendimento as a " +
				"inner join medico as m on m.idMedico = a.idMedico " +
				"inner join especialidade as e on e.idEspecialidade = m.idEspecialidade " +
				"where a.data between ? and ? " +
				"group by m.idEspecialidade;";
					   
		ArrayList<String> listaEspecialidade = new ArrayList<String>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setDate(1, toDate(dataInicio));
			sql.setDate(2, toDate(dataFim));
			
			r = sql.executeQuery(); //recebe a consulta
	
			while(r.next()) {
				String especialidade,qtdAtendimento = null;
				
				especialidade = r.getString("nome");
				qtdAtendimento = r.getInt("qtd")+"";
				
				listaEspecialidade.add(especialidade);
				listaEspecialidade.add(qtdAtendimento);
			}
			
			sql.close();
			return listaEspecialidade;
		}
		catch(Exception ex) {
			System.out.println("Erro: " + ex );
			return null;
		}
	}
	
	public int listaAtendimento(Medico medico, Calendar dataInicio, Calendar dataFim) { // método para realizar uma consulta
		
		String query = "select count(*) as qtd " +
					"from atendimento as a " +
					"inner join pessoa as p on p.idPessoa = a.idMedico " +
					"where p.nome = ? and tipo = 'M' and a.data between ? and ?;";
		
		int qtdAtendimento = 0;
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, medico.getNome());
			sql.setDate(2, toDate(dataInicio));
			sql.setDate(3, toDate(dataFim));
			
			r = sql.executeQuery(); //recebe a consulta
	
			if(r.next()) {

				qtdAtendimento = r.getInt("qtd");	
			}
			
			sql.close();
			return qtdAtendimento;
		}
		catch(Exception ex) {
			System.out.println("Erro: " + ex );
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
