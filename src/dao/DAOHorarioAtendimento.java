package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.HorarioAtendimento;
import modelo.Pessoa;

public class DAOHorarioAtendimento {
	
	private Connection c;
	private PreparedStatement sql;
	private ResultSet r;
	
	public DAOHorarioAtendimento() {	
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

	public ArrayList<HorarioAtendimento> listaHorarioAtendimento(HorarioAtendimento horarioAtendimento) { // método para retornar todas as especialidades
		
		String query = "SELECT h.idHorarioAtendimento, h.horaInicio, h.horaFim, h.duracaoConsulta, d.nome " +
					   "FROM horarioAtendimento AS h " +
					   "INNER JOIN medico as m ON h.idMedico = m.idMedico " +
					   "INNER JOIN mesAtendimento as e ON h.idMesAtendimento = e.idMesAtendimento " +
					   "INNER JOIN diaAtendimento as d ON h.idDiaAtendimento = d.idDiaAtendimento " + 
					   "WHERE m.idMedico = ? and e.nome = ?;";
		
		ArrayList<HorarioAtendimento> listaHorarioAtendimento = new ArrayList<HorarioAtendimento>();
			
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,horarioAtendimento.getMedico().getIdMedico());
			sql.setString(2,horarioAtendimento.getMesAtendimento());
			
			r = sql.executeQuery(); //recebe a consulta	
			
			while(r.next()) {
				HorarioAtendimento auxiliar = new HorarioAtendimento();
			    
				auxiliar.setIdHorarioAtendimento(r.getInt("idHorarioAtendimento"));
				auxiliar.setHoraInicio(toCalendar(r.getTime("horaInicio").getTime()));
				auxiliar.setHoraFim(toCalendar(r.getTime("horaFim").getTime()));
				auxiliar.setDuracaoConsulta(r.getInt("duracaoConsulta"));
				auxiliar.setDiaAtendimento(r.getString("nome"));

				listaHorarioAtendimento.add(auxiliar);
			}
			
			sql.close();
			return listaHorarioAtendimento;
	
		} catch(Exception ex) {
			System.out.println("Erro consultar: " + ex );
			return null;
		}
	}

	public ArrayList<String> listaHorarioAtendimento(Pessoa pessoa, String dia) { // método para retornar todas as especialidades
		
		String query = "SELECT h.horaInicio, h.horaFim, h.duracaoConsulta " +
					   "FROM horarioAtendimento AS h " +
					   "INNER JOIN medico as m ON h.idMedico = m.idMedico " +
					   "WHERE m.idMedico = ? and h.dia = ?;";
		
		ArrayList<String> listaHorario = new ArrayList<String>();
		
		try {
			sql = c.prepareStatement(query);
			
			sql.setInt(1,pessoa.getIdPessoa());
			sql.setString(2,dia);
			
			r = sql.executeQuery(); //recebe a consulta	
			
			if(r.next()) {
				listaHorario.add(r.getTime("horaInicio")+"");
				listaHorario.add(r.getTime("horaFim")+"");
				listaHorario.add(r.getInt("duracaoConsulta")+"");
				
				sql.close();
				return listaHorario;
			} else {
				sql.close();
				return listaHorario;
			}
	
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
