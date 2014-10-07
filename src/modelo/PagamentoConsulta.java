package modelo;

import java.util.Calendar;

public class PagamentoConsulta {
	
	//Atributos
	private int 		idPagamentoConsulta;
	private Calendar 	data;
	private Calendar 	hora;
	private double 		valor;
	private String 		formaPagamento;
	private String 		status;
	private Atendimento atendimento;
	private Cliente 	cliente;
	private Funcionario funcionario;
	
	//construtor vazio da classe pagamentoConsulta
	public PagamentoConsulta() {
		this.idPagamentoConsulta = 0;
		this.data = null;
		this.hora = null;
		this.valor = 0;
		this.formaPagamento = null;
		this.status = null;
		this.atendimento = null;
		this.cliente = null;
		this.funcionario = null;
	}
	
	//Métodos set's e get's	
	public int getIdPagamentoConsulta() {
		return idPagamentoConsulta;
	}
	
	public void setIdPagamentoConsulta(int idPagamentoConsulta) {
		this.idPagamentoConsulta = idPagamentoConsulta;
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
	
	public double getValor() {
		return valor;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public String getFormaPagamento() {
		return formaPagamento;
	}
	
	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Atendimento getAtendimento() {
		return atendimento;
	}
	
	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}
	
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}
