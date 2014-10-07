package controle;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Cliente;
import modelo.Dependente;
import dao.DAOCliente;
import dao.DAODependente;

public class ControleDependente extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Recupera a acao e a matricula
		String acao = request.getParameter("acao");
		
		//Cria o objeto DAODependente
		DAODependente daoDependente = new DAODependente();
		
		//Cria o objeto DAOCliente
		DAOCliente daoCliente = new DAOCliente();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		//Executa o procedimento de acordo com a acao
		if (acao.equals("Cadastrar")) {
			
			//Recupera todos os campos de dependente
			String matricula		   = request.getParameter("matricula");
			String nome 	  	       = request.getParameter("nome");
			String cpf 		 	  	   = request.getParameter("cpf");
			String rg	 		  	   = request.getParameter("rg");
			String telefoneResidencia  = request.getParameter("telefoneResidencia");
			String telefoneCelular 	   = request.getParameter("telefoneCelular");
			String matriculaTitular    = request.getParameter("matriculaTitular");
			String dataNascimento 	   = request.getParameter("dataNascimento");
			
			//Cria o objeto Calendar
			Calendar dataNasc = new GregorianCalendar();
			
			//Transforma para a String para Date
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			//Transforma a data para Calendar
			try {
				dataNasc.setTime((Date) formatter.parse(dataNascimento));
			} catch (Exception e) {
				System.out.println(e);
			}
			
			//Verifica a idade do Dependente
			Calendar hoje = Calendar.getInstance();
			Calendar aux = new GregorianCalendar();
			
			//adiciona a data de nascimento ao objeto auxiliar
			aux.setTime(dataNasc.getTime());
			
			//subtrai os anos das duas datas.
			int idade = hoje.get(Calendar.YEAR) - aux.get(Calendar.YEAR);
			aux.add(Calendar.YEAR, idade);
			
			if(hoje.before(aux)) {
				idade--;
			}
			
			if (idade >= 18){
				String resposta = "{\"mensagem\":\"Dependente maior de idade!\"}";
				
				out.print(resposta);
			} else {
				//Cria o objeto especialidade(auxiliar) e define o campo (matricula)
				Dependente auxiliar = new Dependente();
				auxiliar.setMatricula(matricula);

				//Retorna o objeto preenchido pela DAO
				auxiliar = daoDependente.consultar(auxiliar);
				
				//verifica se o dependente não existe
				if(auxiliar == null) {
					
					//Cria o objeto Cliente e define a matricula
					Cliente cliente = new Cliente();
					cliente.setMatricula(matriculaTitular);
					
					//Consulta o cliente
					cliente = daoCliente.consultaMatricula(cliente);
						
					if(cliente == null) {
							
						String resposta = "{\"mensagem\":\"Cliente inexistente!\"}";
							
						out.print(resposta);
					} else {
							
						int qtdDependente = daoCliente.consultaQtdDependente(cliente);
						
						if(qtdDependente == -1) {
							String resposta = "{\"mensagem\":\"Erro, por favor tente novamente!\"}";
							
							out.print(resposta);
						} else if(qtdDependente < 6){
							
							//Cria o objeto dependente e define todos os campos
							Dependente dependente = new Dependente();
								
							dependente.setMatricula(matricula);
							dependente.setNome(nome);
							dependente.setCpf(cpf);
							dependente.setRg(rg);
							dependente.setDataNascimento(dataNasc);
							dependente.setTelefoneResidencia(telefoneResidencia);
							dependente.setTelefoneCelular(telefoneCelular);
							dependente.setEndereco(cliente.getEndereco());
							dependente.setCliente(cliente);
								
							daoDependente.cadastrar(dependente);
							
							String resposta = "{\"mensagem\":\"Dependente cadastrado com sucesso!\"}";
								
							out.print(resposta);	
						} else {
							
							String resposta = "{\"mensagem\":\"Cliente já possui 6 dependentes!\"}";
							
							out.print(resposta);
						}
					}			
				} else {
					String resposta = "{\"mensagem\":\"Dependente já existe!\"}";
						
					out.print(resposta);
				}			
			}
		} else if(acao.equals("Consultar")) {
				
			//Recupera o campo matricula
			String matricula = request.getParameter("matricula");
				
			//Cria o objeto Dependente e atribui a matricula
			Dependente dependente = new Dependente();
			dependente.setMatricula(matricula);
			
			//COnsulta o dependente
			Dependente auxiliar = daoDependente.consultar(dependente);
			
			//Cria a String de resposta
			String resposta = "";
			
			//Verifica se a consulta retornou algum resultado
			if (auxiliar != null) {
					
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					
				String dataNasc = sdf.format(auxiliar.getDataNascimento().getTime()); 
					
				//Adiciona os objetos a string
				resposta = "{\"matricula\":\""+auxiliar.getMatricula()+"\",\"nome\":\""+auxiliar.getNome()+"\",\"cpf\":\""+auxiliar.getCpf()+"\",\"senha\":\""+auxiliar.getSenha()+
							"\",\"rg\":\""+auxiliar.getRg()+"\",\"dataNascimento\":\""+dataNasc+"\",\"telefoneResidencia\":\""+auxiliar.getTelefoneResidencia() +
							"\",\"telefoneCelular\":\""+auxiliar.getTelefoneCelular()+"\",\"matriculaTitular\":\""+auxiliar.getCliente().getMatricula()+"\"}";
							
				//Envia a resposta
				out.print(resposta);
			} else {
				
				resposta = "{\"mensagem\":\"Dependente não encontrado!\"}";

				//Envia a resposta
				out.print(resposta);
			}
		}else if(acao.equals("Alterar")) {
			
			//Recupera todos os campos de dependente
			String matricula		   = request.getParameter("matricula");
			String nome 	  	       = request.getParameter("nome");
			String cpf 		 	  	   = request.getParameter("cpf");
			String rg	 		  	   = request.getParameter("rg");
			String telefoneResidencia  = request.getParameter("telefoneResidencia");
			String telefoneCelular 	   = request.getParameter("telefoneCelular");
			String dataNascimento 	   = request.getParameter("dataNascimento");
			
			//Cria o objeto Calendar
			Calendar dataNasc = new GregorianCalendar();
			
			//Transforma para a String para Date
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			//Transforma a data para Calendar
			try {
				dataNasc.setTime((Date) formatter.parse(dataNascimento));
			} catch (Exception e) {
				System.out.println(e);
			}
			
			//Cria o objeto especialidade e define o campo matricula
			Dependente dependente = new Dependente();
			dependente.setMatricula(matricula);
			
			//Retorna o objeto preenchido pela DAO
			dependente = daoDependente.consultar(dependente);	
			
			//Cria a String de resposta
			String resposta = "";
			
			//Verifica se o objeto NÃO está nulo
			if(dependente != null) {
				
				dependente.setNome(nome);
				dependente.setCpf(cpf);
				dependente.setRg(rg);
				dependente.setDataNascimento(dataNasc);
				dependente.setTelefoneResidencia(telefoneResidencia);
				dependente.setTelefoneCelular(telefoneCelular);
				
				daoDependente.alterar(dependente);
				
				resposta = "{\"mensagem\":\"Dependente Alterado com sucesso!\"}";
				
				out.print(resposta);
			} else {
				
				resposta = "{\"mensagem\":\"Dependente não existe!\"}";
			}
		} else if(acao.equals("Excluir")) {
			
			//Recupera o campo matricula
			String matricula = request.getParameter("matricula");
			
			//Cria o objeto Dependente e atribui a matricula
			Dependente dependente = new Dependente();
			dependente.setMatricula(matricula);
			
			//Cria uma lista com todos os dependentes
			Dependente auxiliar = daoDependente.consultar(dependente);
			
			//Cria a String de resposta
			String resposta = "";
			
			//verifica se o dependente já existe
			if(auxiliar != null) {
				
				daoDependente.excluir(auxiliar);
				
				resposta = "{\"mensagem\":\"Dependente Excluído com sucesso!\"}";
				
				out.print(resposta);
			} else {
				
				resposta = "{\"mensagem\":\"Dependente não existe!\"}";

				out.print(resposta);
			}
		}

		out.flush();
		out.close();
	}
}
