package modelo;

public class Especialidade {
	
	//Atributos
	private int 	idEspecialidade;
	private String 	nome;
	private String 	descricao;
	private PrecoEspecialidade precoEspecialidade;

	//construtor vazio da classe especialidade
	public Especialidade() {
		this.idEspecialidade = 0;
		this.nome = null;
		this.descricao = null;
		this.precoEspecialidade = null;
	}
	
	//Métodos set's e get's
	public int getIdEspecialidade() {
		return idEspecialidade;
	}

	public void setIdEspecialidade(int idEspecialidade) {
		this.idEspecialidade = idEspecialidade;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public PrecoEspecialidade getPrecoEspecialidade() {
		return precoEspecialidade;
	}

	public void setPrecoEspecialidade(PrecoEspecialidade precoEspecialidade) {
		this.precoEspecialidade = precoEspecialidade;
	}
}
