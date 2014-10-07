package modelo;

import java.util.Calendar;

public class PagamentoPlanoSaude {
	
	//Atributos
	private int			idPagamentoPlanoSaude;
	private Calendar 	dataVencimento;
	private Calendar 	dataPagamento;
	private double 		valorPago;
	private String 		mesReferencia;
	private String		status;
	
	//construtor vazio da classe pagamentoPlanoSaude
	public PagamentoPlanoSaude() {
		this.idPagamentoPlanoSaude = 0;
		this.dataVencimento = null;
		this.dataPagamento = null;
		this.valorPago = 0;
		this.mesReferencia = null;
		this.status = null;
	}
	
	//Métodos set's e get's		
	public int getIdPagamentoPlanoSaude() {
		return idPagamentoPlanoSaude;
	}
	
	public void setIdPagamentoPlanoSaude(int idPagamentoPlanoSaude) {
		this.idPagamentoPlanoSaude = idPagamentoPlanoSaude;
	}
	
	public Calendar getDataVencimento() {
		return dataVencimento;
	}
	
	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public Calendar getDataPagamento() {
		return dataPagamento;
	}
	
	public void setDataPagamento(Calendar dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	public double getValorPago() {
		return valorPago;
	}
	
	public void setValorPago(double valorPago) {
		this.valorPago = valorPago;
	}
	
	public String getMesReferencia() {
		return mesReferencia;
	}
	
	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
