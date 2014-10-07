package modelo;

import java.util.Calendar;

public class PrecoEspecialidade {
	
	//Atributos
	private int 		idPrecoEspecialidade;
	private Calendar 	dataInicio;
	private Calendar 	dataFim;
	private double 		valor;
	
	//construtor vazio da classe precoEspecialidade
	public PrecoEspecialidade() {
		this.idPrecoEspecialidade = 0;
		this.dataInicio = null;
		this.dataFim = null;
		this.valor = 0;
	}
	
	//Métodos set's e get's	
	public int getIdPrecoEspecialidade() {
		return idPrecoEspecialidade;
	}

	public void setIdPrecoEspecialidade(int idPrecoEspecialidade) {
		this.idPrecoEspecialidade = idPrecoEspecialidade;
	}

	public Calendar getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Calendar getDataFim() {
		return dataFim;
	}
	
	public void setDataFim(Calendar dataFim) {
		this.dataFim = dataFim;
	}
	
	public double getValor() {
		return valor;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}

}
