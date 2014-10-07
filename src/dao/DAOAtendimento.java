package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.Atendimento;
import modelo.Cliente;
import modelo.Especialidade;
import modelo.HorarioAtendimento;
import modelo.Medico;

public class DAOAtendimento {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOAtendimento() {	
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
	
	public boolean registraAgendamento(Atendimento atendimento) {
		
		String query = "INSERT INTO atendimento(data, hora, idCliente, idDependente, " +
				"idStatusAtendimento, idMedico) VALUES (?,?,?,?,1,?);";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setDate(1,toDate(atendimento.getData()));
			sql.setTime(2,toTime(atendimento.getHora()));
			
			if (atendimento.getCliente() != null) {
				sql.setInt(3,atendimento.getCliente().getIdCliente());
				sql.setNull(4,0);
			} else {
				sql.setInt(3,atendimento.getDependente().getCliente().getIdCliente());
				sql.setInt(4,atendimento.getDependente().getIdDependente());
			}
			
			sql.setInt(5,atendimento.getMedico().getIdMedico());
			
			sql.executeUpdate(); //recebe a consulta
			
			return true;
		} catch(Exception ex) {
			System.out.println("Erro agendar: " + ex );
			return false;
		}
	}
	
	public boolean registraAtendimento(Atendimento atendimento) {
		
		String query = "UPDATE atendimento SET idStatusAtendimento = " +
					   "(SELECT idStatusAtendimento FROM statusAtendimento WHERE nome = ?) " +
					   "WHERE idAtendimento = ?;";
		
		String query1 = "INSERT INTO prontuario (idProntuario, receituario, prontuario, idAtendimento) VALUES (?,?,?,?);";
		
		try {
		    c.setAutoCommit(false);
		    
			sql = c.prepareStatement(query);
			
			sql.setString(1, atendimento.getStatus());
			sql.setInt(2, atendimento.getIdAtendimento());
			
			sql.executeUpdate(); //recebe a consulta
			
			sql = c.prepareStatement(query1);
			
			sql.setInt(1,atendimento.getIdAtendimento());
			sql.setString(2,atendimento.getProntuario().getProntuario());
			sql.setString(3,atendimento.getProntuario().getReceituario());
			sql.setInt(4,atendimento.getIdAtendimento());
			
			sql.executeUpdate(); //recebe a consulta
			
			c.commit();
		    
			sql.close();

			return true;
		} catch(Exception ex) {
			System.out.println("Erro agendar: " + ex );
			return false;
		}
	}
	
	public ArrayList<Atendimento> listaAtendimento(Atendimento atendimento) { // método para retornar todas as especialidades
		
		String query = "SELECT a.idAtendimento, a.data, a.hora, s.nome as nomeStatus, p.nome as nomeCliente " +
					 "FROM atendimento as a " +
					 "INNER JOIN pessoa as p ON p.idPessoa = a.idCliente " +
					 "INNER JOIN statusAtendimento as s ON s.idStatusAtendimento = a.idStatusAtendimento " +
					 "WHERE a.idMedico = ? AND a.data = CURDATE() AND a.idStatusAtendimento IN (1,2,3) " +
					 "ORDER BY a.hora;";
		
		ArrayList<Atendimento> listaAtendimento = new ArrayList<Atendimento>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,atendimento.getMedico().getIdMedico());
			
			r = sql.executeQuery(); //recebe a consulta
			
			while(r.next()) {
				Atendimento auxiliar = new Atendimento();
			    
				auxiliar.setIdAtendimento(r.getInt("idAtendimento"));
				auxiliar.setData(toCalendar(r.getTime("data").getTime()));
				auxiliar.setHora(toCalendar(r.getTime("hora").getTime()));
				auxiliar.setStatus(r.getString("nomeStatus"));

				Cliente cliente = new Cliente();
				cliente.setNome(r.getString("nomeCliente"));
				
				auxiliar.setCliente(cliente);

				listaAtendimento.add(auxiliar);
			}
			
			sql.close();
			return listaAtendimento;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	public ArrayList<Atendimento> listaStatus() { // método para retornar todas as especialidades
		
		String query = "SELECT s.nome " +
					 "FROM statusAtendimento as s " +
					 "WHERE s.idStatusAtendimento IN (2,5) " +
					 "ORDER BY s.nome;";
		
		ArrayList<Atendimento> listaStatus = new ArrayList<Atendimento>();
		
		try {
			sql = c.prepareStatement(query);
			
			r = sql.executeQuery(); //recebe a consulta

			while(r.next()) {
				Atendimento auxiliar = new Atendimento();
			   
				auxiliar.setStatus(r.getString("nome"));
				
				listaStatus.add(auxiliar);
			}
			
			sql.close();
			return listaStatus;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	public ArrayList<Atendimento> listaAtendimentoConsultaFinalizado(Cliente cliente) { // método para retornar todas as especialidades
		
		String query = "SELECT a.idAtendimento, a.data, a.hora, s.nome as nomeStatus, p.nome as nomeMedico, e.nome as nomeEspecialidade " +
					 "FROM atendimento as a " +
					 "INNER JOIN cliente as c ON c.idCliente = a.idCliente " +
					 "INNER JOIN statusAtendimento as s ON s.idStatusAtendimento = a.idStatusAtendimento " +
					 "INNER JOIN pessoa as p ON p.idPessoa = a.idMedico " +
					 "INNER JOIN medico as m ON m.idMedico = p.idPessoa " +
					 "INNER JOIN especialidade as e ON e.idEspecialidade = m.idEspecialidade " +
					 "INNER JOIN pagamentoConsulta as pa ON pa.idAtendimento = a.idAtendimento " +
					 "WHERE a.idStatusAtendimento = 5 AND c.idCliente = ? AND pa.idStatusPagamentoConsulta = 1;";
		
		ArrayList<Atendimento> listaAtendimentoConsultaFinalizado = new ArrayList<Atendimento>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,cliente.getIdCliente());
			
			r = sql.executeQuery(); //recebe a consulta

			while(r.next()) {
				Atendimento auxiliar = new Atendimento();
			    
				auxiliar.setIdAtendimento(r.getInt("idAtendimento"));
				auxiliar.setData(toCalendar(r.getTime("data").getTime()));
				auxiliar.setHora(toCalendar(r.getTime("hora").getTime()));
				auxiliar.setStatus(r.getString("nomeStatus"));

				Especialidade especialidade = new Especialidade();
				especialidade.setNome(r.getString("nomeEspecialidade"));
				
				Medico medico = new Medico();
				medico.setNome(r.getString("nomeMedico"));
				medico.setEspecialidade(especialidade);
				
				auxiliar.setMedico(medico);
				
				listaAtendimentoConsultaFinalizado.add(auxiliar);
			}
			
			sql.close();
			return listaAtendimentoConsultaFinalizado;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	public void alteraStatus(Atendimento atendimento) {
		
		String query = "UPDATE atendimento SET idStatusAtendimento = " +
					   "(SELECT idStatusAtendimento FROM statusAtendimento WHERE nome = ?) " +
					   "WHERE idAtendimento = ?;";
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setString(1, atendimento.getStatus());
			sql.setInt(2, atendimento.getIdAtendimento());
			
			sql.executeUpdate();
			
			sql.close();
		} catch(Exception e){
			System.out.println("Erro Alterar: " + e);
		}
	}	
	
	public Atendimento consultaStatus(Atendimento atendimento) { // método para consultar o status de um atendimento
		
		String query = "SELECT nome FROM statusAtendimento AS sa " +
					   "INNER JOIN atendimento AS a ON sa.idStatusAtendimento = a.idStatusAtendimento " +
					   "WHERE a.idAtendimento = ?";
		
		Atendimento auxiliar = new Atendimento();
		
		try {
			sql = c.prepareStatement(query);

			sql.setInt(1, atendimento.getIdAtendimento());
			
			r = sql.executeQuery(); //recebe a consulta
			
			if(r.next()) {

				auxiliar.setStatus(r.getString("nome"));
			}
			
			sql.close();
			return auxiliar;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}
	
	public boolean verificaDisponibilidade(HorarioAtendimento horarioAtendimento) { // método para retornar todas as especialidades
		
		String query = "SELECT * " +
					   "FROM atendimento " +
					   "WHERE idMedico = ? and hora = ? and idStatusAtendimento = 1;";
		
		Time horaInicio = new Time(horarioAtendimento.getHoraInicio().getTimeInMillis());
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,horarioAtendimento.getMedico().getIdMedico());
			sql.setTime(2,horaInicio);
			
			r = sql.executeQuery(); //recebe a consulta

			//Se houver registro retorna False, caso contrário retorna True
			if(r.next()) {
				sql.close();
				return false;
			} else {
				sql.close();
				return true;
			}
		} catch(Exception ex) {
			System.out.println("Erro consult2ar: " + ex );
			return false;
		}
	}
	
	public boolean verificaDuplicidade(Atendimento atendimento) { // método para retornar todas as especialidades
		
		String query = "SELECT * " +
					   "FROM atendimento as a " +
					   "INNER JOIN medico as m ON a.idMedico = m.idMedico " +
					   "INNER JOIN especialidade as e ON m.idEspecialidade = e.idEspecialidade " +
					   "WHERE a.idCliente = ? AND a.idDependente IS NULL AND e.idEspecialidade = (select idEspecialidade from medico where idMedico = ?) and a.data = ?;";

		String query1 =	"SELECT * " +
		   				"FROM atendimento as a " +
		   				"INNER JOIN medico as m ON a.idMedico = m.idMedico " +
		   				"INNER JOIN especialidade as e ON m.idEspecialidade = e.idEspecialidade " +
		   				"WHERE a.idDependente = ? AND e.idEspecialidade = (select idEspecialidade from medico where idMedico = ?) and a.data = ?;";
		
		try {
			
			if(atendimento.getCliente() != null) {
				
				sql = c.prepareStatement(query);
			
				sql.setInt(1,atendimento.getCliente().getIdCliente());
				sql.setInt(2,atendimento.getMedico().getIdMedico());
				sql.setDate(3,toDate(atendimento.getData()));
				
				r = sql.executeQuery(); //recebe a consulta
			} else {
				
				sql = c.prepareStatement(query1);
				
				sql.setInt(1,atendimento.getDependente().getIdDependente());
				sql.setInt(2,atendimento.getMedico().getIdMedico());
				sql.setDate(3,toDate(atendimento.getData()));
				
				r = sql.executeQuery(); //recebe a consulta
			}
			
			//Se houver registro retorna False, caso contrário retorna True
			if(r.next()) {
				sql.close();
				return false;
			} else {
				sql.close();
				return true;
			}
		} catch(Exception ex) {
			System.out.println("Erro consulta1r: " + ex );
			return false;
		}
	}
	
	//Converte o tipo TIME em CALENDAR
	private Calendar toCalendar(long hora) {
		Calendar calendario = new GregorianCalendar();
		
		calendario.setTimeInMillis(hora);
		
		return calendario;
	}
	
	//Converte o tipo CALENDAR em DATE
	private Date toDate(Calendar calendar) {
		Date data = new Date(calendar.getTimeInMillis());
		
		return data;
	}
	
	//Converte o tipo CALENDAR em TIME
	private Time toTime(Calendar calendar) {
		
		Time tempo = new Time(calendar.getTimeInMillis());
		
		return tempo;
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
