package modelo;

public class Medico extends Funcionario {
	
	private int		idMedico;
	private String 	crm;
	private Especialidade especialidade;

	//construtor vazio da classe medico
	public Medico() {
		this.idMedico = 0;
		this.crm = null;
		this.especialidade = null;	
	}
	
	//construtor sobrecarregado da classe medico
	public Medico(Funcionario funcionario) {
		this.setIdFuncionario(funcionario.getIdFuncionario());
		this.setMatricula(funcionario.getMatricula());		
		this.setSenha(funcionario.getSenha());
		this.setNome(funcionario.getNome());
		this.setCpf(funcionario.getCpf());
		this.setRg(funcionario.getRg());
		this.setTelefoneResidencia(funcionario.getTelefoneResidencia());
		this.setTelefoneCelular(funcionario.getTelefoneCelular());
		this.setDataNascimento(funcionario.getDataNascimento());
		this.setTipo(funcionario.getTipo());
		this.setFuncao(funcionario.getFuncao());
		this.setCtps(funcionario.getCtps());
		this.setDataAdmissao(funcionario.getDataAdmissao());
		this.setDataDemissao(funcionario.getDataDemissao());
		this.idMedico = funcionario.getIdFuncionario();
	}
	
	//Métodos set's e get's
	public int getIdMedico() {
		return idMedico;
	}

	public void setIdMedico(int idMedico) {
		this.idMedico = idMedico;
	}

	public String getCrm() {
		return crm;
	}
	
	public void setCrm(String crm) {
		this.crm = crm;
	}
	
	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}
}
