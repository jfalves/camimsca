package modelo;

import java.util.Calendar;

public class Funcionario extends Pessoa {
	
	//Atributos
	private int 		idFuncionario;
	private String 		funcao;
	private String 		ctps;
	private Calendar 	dataAdmissao;
	private Calendar 	dataDemissao;
	private String 		status;

	//construtor vazio da classe funcionario
	public Funcionario() {
		this.idFuncionario = 0;
		this.funcao = null;
		this.ctps = null;
		this.dataAdmissao = null;
		this.dataDemissao = null;
		this.status = null;
	}
	
	//construtor sobrecarregado da classe funcionario
	public Funcionario(Pessoa pessoa) {
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
		this.idFuncionario = pessoa.getIdPessoa();
	}
	
	//Métodos set's e get's
	public int getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(int idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public String getFuncao() {
		return funcao;
	}
	
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	
	public String getCtps() {
		return ctps;
	}
	
	public void setCtps(String ctps) {
		this.ctps = ctps;
	}
	
	public Calendar getDataAdmissao() {
		return dataAdmissao;
	}
	
	public void setDataAdmissao(Calendar dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	
	public Calendar getDataDemissao() {
		return dataDemissao;
	}
	
	public void setDataDemissao(Calendar dataDemissao) {
		this.dataDemissao = dataDemissao;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
