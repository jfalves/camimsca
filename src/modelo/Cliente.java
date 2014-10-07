package modelo;

public class Cliente extends Pessoa {
	
	//Atributos
	private int idCliente;
	
	//construtor vazio da classe cliente
	public Cliente() {
		this.idCliente = 0;
	}
	
	//construtor sobrecarregado da classe cliente
	public Cliente(Pessoa pessoa) {
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
		this.idCliente = pessoa.getIdPessoa();
	}
	
	//Métodos set's e get's
	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
}
