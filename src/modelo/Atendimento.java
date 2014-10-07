package modelo;

import implementacao.Prontuario;

import java.util.Calendar;

public class Atendimento {
	
	//Atributos
	private int 	   idAtendimento;
	private String 	   status;
	private Prontuario prontuario;
	private Calendar   data;
	private Calendar   hora;
	private Cliente    cliente;
	private Dependente dependente;
	private Medico     medico;
	
	//construtor vazio da classe atendimento
	public Atendimento() {
		this.idAtendimento = 0;
		this.status = null;
		this.prontuario = null;
		this.data = null;
		this.hora = null;
		this.cliente = null;
		this.dependente = null;
		this.medico = null;
	}
	
	//Métodos set's e get's
	public int getIdAtendimento() {
		return idAtendimento;
	}
	
	public void setIdAtendimento(int idAtendimento) {
		this.idAtendimento = idAtendimento;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Prontuario getProntuario() {
		return prontuario;
	}
	
	public void setProntuario(Prontuario prontuario) {
		this.prontuario = prontuario;
	}
	
	public Calendar getData() {
		return data;
	}
	
	public void setData(Calendar data) {
		this.data = data;
	}
	
	public Calendar getHora() {
		return hora;
	}
	
	public void setHora(Calendar hora) {
		this.hora = hora;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public Dependente getDependente() {
		return dependente;
	}
	
	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}
	
	public Medico getMedico() {
		return medico;
	}
	
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
}
