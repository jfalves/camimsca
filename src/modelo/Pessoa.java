	package modelo;

import implementacao.Endereco;

import java.util.Calendar;

public class Pessoa {
	
	private int 	 idPessoa;
	private String	 matricula;
	private String	 senha;
	private String	 nome;
	private String	 cpf;
	private String	 rg;
	private String	 telefoneResidencia;
	private String	 telefoneCelular;
	private Calendar dataNascimento;
	private String	 tipo;
	private Endereco endereco;

	//construtor vazio da classe pessoa
	public Pessoa() {
		this.idPessoa = 0;
		this.matricula = null;
		this.senha = null;
		this.nome = null;
		this.cpf = null;
		this.rg = null;
		this.telefoneResidencia = null;
		this.telefoneCelular = null;
		this.dataNascimento = null;
		this.tipo = null;
		this.endereco = null;
	}
	
	//Métodos set's e get's
	public int getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(int idPessoa) {
		this.idPessoa = idPessoa;
	}

	public String getMatricula() {
		return matricula;
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getRg() {
		return rg;
	}
	
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public String getTelefoneResidencia() {
		return telefoneResidencia;
	}
	
	public void setTelefoneResidencia(String telefoneResidencia) {
		this.telefoneResidencia = telefoneResidencia;
	}
	
	public String getTelefoneCelular() {
		return telefoneCelular;
	}
	
	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}
	
	public Calendar getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(Calendar dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
}

