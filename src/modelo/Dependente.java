package modelo;

public class Dependente extends Pessoa {
	
	//Atributos
	private int idDependente;
	private Cliente cliente;
	
	//construtor vazio da classe dependente
	public Dependente() {
		this.idDependente = 0;
		this.cliente = null;
	}
	
	//construtor sobrecarregado da classe dependente	
	public Dependente(Pessoa pessoa) {
		this.setIdPessoa(pessoa.getIdPessoa());
		this.setMatricula(pessoa.getMatricula());		
		this.setSenha(pessoa.getSenha());
		this.setNome(pessoa.getNome());
		this.setCpf(pessoa.getCpf());
		this.setRg(pessoa.getRg());
		this.setTelefoneResidencia(pessoa.getTelefoneResidencia());
		this.setTelefoneCelular(pessoa.getTelefoneCelular());
		this.setDataNascimento(pessoa.getDataNascimento());
		this.setTipo(pessoa.getTipo());
		this.idDependente =	pessoa.getIdPessoa();
	}

	//Métodos set's e get's
	public int getIdDependente() {
		return idDependente;
	}

	public void setIdDependente(int idDependente) {
		this.idDependente = idDependente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}