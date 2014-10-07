package modelo;

public class PlanoSaude {
	
	//Atributos
	private int		idPlanoSaude;
	private int 	idadeMinima;
	private int 	idadeMaxima;
	private String	tipo;
	private double 	valorFaixa;
	
	//construtor vazio da classe planoSaude
	public PlanoSaude() {
		this.idPlanoSaude = 0;
		this.idadeMinima = 0;
		this.idadeMaxima = 0;
		this.tipo = null;
		this.valorFaixa = 0;
	}
	
	//Métodos set's e get's
	public int getIdPlanoSaude() {
		return idPlanoSaude;
	}
	public void setIdPlanoSaude(int idPlanoSaude) {
		this.idPlanoSaude = idPlanoSaude;
	}
	public int getIdadeMinima() {
		return idadeMinima;
	}
	public void setIdadeMinima(int idadeMinima) {
		this.idadeMinima = idadeMinima;
	}
	public int getIdadeMaxima() {
		return idadeMaxima;
	}
	public void setIdadeMaxima(int idadeMaxima) {
		this.idadeMaxima = idadeMaxima;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getValorFaixa() {
		return valorFaixa;
	}
	public void setValorFaixa(double valorFaixa) {
		this.valorFaixa = valorFaixa;
	}


	
}
