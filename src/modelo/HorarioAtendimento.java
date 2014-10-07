package modelo;

import java.util.Calendar;

public class HorarioAtendimento {

	//Atributos
	private int		 idHorarioAtendimento;
	private Calendar horaInicio;
	private Calendar horaFim;
	private int		 duracaoConsulta;
	private Medico	 medico;
	private String	 mesAtendimento;
	private String	 diaAtendimento;	

	//construtor vazio da classe horarioAtendimento
	public HorarioAtendimento() {
		this.idHorarioAtendimento = 0;
		this.horaInicio = null;
		this.horaFim = null;
		this.duracaoConsulta = 0;
		this.medico = null;
		this.mesAtendimento = null;
		this.diaAtendimento = null;
	}
	
	//Métodos set's e get's
	public int getIdHorarioAtendimento() {
		return idHorarioAtendimento;
	}
	
	public void setIdHorarioAtendimento(int idHorarioAtendimento) {
		this.idHorarioAtendimento = idHorarioAtendimento;
	}
	
	public Calendar getHoraInicio() {
		return horaInicio;
	}
	
	public void setHoraInicio(Calendar horaInicio) {
		this.horaInicio = horaInicio;
	}
	
	public Calendar getHoraFim() {
		return horaFim;
	}
	
	public void setHoraFim(Calendar horaFim) {
		this.horaFim = horaFim;
	}
	
	public int getDuracaoConsulta() {
		return duracaoConsulta;
	}
	
	public void setDuracaoConsulta(int duracaoConsulta) {
		this.duracaoConsulta = duracaoConsulta;
	}
	
	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}	
	
	public String getMesAtendimento() {
		return mesAtendimento;
	}

	public void setMesAtendimento(String mesAtendimento) {
		this.mesAtendimento = mesAtendimento;
	}

	public String getDiaAtendimento() {
		return diaAtendimento;
	}
	
	public void setDiaAtendimento(String diaAtendimento) {
		this.diaAtendimento = diaAtendimento;
	}
}
