package controle;

import implementacao.Endereco;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.DAOCliente;

import modelo.Cliente;

public class ControleCliente extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Recupera a acao e a matricula
		String acao = request.getParameter("acao");
		String tipoPlano = request.getParameter("tipoPlano");
		
		//Cria o objeto DAOCliente
		DAOCliente daoCliente = new DAOCliente();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if(tipoPlano == null) {
			tipoPlano = "";
		}
		
		if(tipoPlano.equals("semPlano") || tipoPlano.equals("")) { //procedimentos para um cliente avulso
			
			//Executa o procedimento de acordo com a acao
			if (acao.equals("Cadastrar")) {
				
				//Recupera todos os campos do cliente
				String matricula		   = request.getParameter("matricula");
				String senha 	  	       = request.getParameter("senha");
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
				
				String logradouro 	   = request.getParameter("logradouro");
				String complemento 	   = request.getParameter("complemento");
				String bairro	 	   = request.getParameter("bairro");
				String cep		 	   = request.getParameter("cep");
				String cidade 	  	   = request.getParameter("cidade");
				
				//Cria o objeto cliente(auxiliar) e define o campo (matricula)
				Cliente auxiliar = new Cliente();
				auxiliar.setMatricula(matricula);

				//Retorna o objeto preenchido pela DAO
				auxiliar = daoCliente.consultar(auxiliar);	
				
				//cia a string de resposta
				String resposta = "";
				
				//verifica se o cliente não existe
				if(auxiliar == null) {
					
					//Cria o objeto cliente e define todos os campos
					Cliente cliente = new Cliente();
					
					cliente.setMatricula(matricula);
					cliente.setNome(nome);
					cliente.setSenha(senha);
					cliente.setCpf(cpf);
					cliente.setRg(rg);
					cliente.setDataNascimento(dataNasc);
					cliente.setTelefoneResidencia(telefoneResidencia);
					cliente.setTelefoneCelular(telefoneCelular);
					
					Endereco endereco = new Endereco();
					
					endereco.setLogradouro(logradouro);
					endereco.setComplemento(complemento);
					endereco.setBairro(bairro);
					endereco.setCep(cep);
					endereco.setCidade(cidade);
					
					cliente.setEndereco(endereco);
					
					daoCliente.cadastrar(cliente);
					
					resposta = "{\"mensagem\":\"Cliente cadastrado com sucesso!\"}";
					
					out.print(resposta);			
				} else {
					resposta = "{\"mensagem\":\"Cliente já existe!\"}";
					
					out.print(resposta);
				}
			} else if(acao.equals("Consultar")) {
				
				//Recupera o campo matricula
				String matricula = request.getParameter("matricula");
					
				//Cria o objeto Dependente e atribui a matricula
				Cliente cliente = new Cliente();
				cliente.setMatricula(matricula);
				
				//Consulta o dependente
				Cliente auxiliar = daoCliente.consultar(cliente);
				
				//Cria a String de resposta
				String resposta = "";

				//Verifica se a consulta retornou algum resultado
				if (auxiliar != null) {
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					
					String dataNasc = sdf.format(auxiliar.getDataNascimento().getTime()); 
						
					//Adiciona os objetos a string
					resposta = "{\"matricula\":\""+auxiliar.getMatricula()+"\",\"nome\":\""+auxiliar.getNome()+"\",\"cpf\":\""+auxiliar.getCpf()+"\",\"senha\":\""+auxiliar.getSenha()+
								"\",\"rg\":\""+auxiliar.getRg()+"\",\"dataNascimento\":\""+dataNasc+"\",\"telefoneResidencia\":\""+auxiliar.getTelefoneResidencia() +
								"\",\"telefoneCelular\":\""+auxiliar.getTelefoneCelular()+"\",\"logradouro\":\""+auxiliar.getEndereco().getLogradouro()+"\",\"complemento\":\""+auxiliar.getEndereco().getComplemento() +
								"\",\"bairro\":\""+auxiliar.getEndereco().getBairro()+"\",\"cep\":\""+auxiliar.getEndereco().getCep()+"\",\"cidade\":\""+auxiliar.getEndereco().getCidade()+"\"}";
								
					//Envia a resposta
					out.print(resposta);
				} else {
					
					resposta = "{\"mensagem\":\"Cliente não existe!\"}";

					//Envia a resposta
					out.print(resposta);
				}
			} else if(acao.equals("ConsultarTodos")) {
				
				//Recupera a pagina requerida, define o limite de registros do jqgrid e conta a quantidade de registros
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = 10;
				int count = daoCliente.qtdRegistro();
				int total = 0;
				
				// calcula o total de páginas para a query
				if( count > 0 ) {  
					total = (int) Math.ceil(count/limit); 
				}
				 
				// verifica se a página requerida é maior que o total
				if (page > total) {
					page = total;
				}
				
				// calcula a posição inicial da query
				int start = limit*page - limit; 
				 
				// verifica se o valor é negativo
				if(start <0){ 
					start = 0;
				}
				
				//Retorna o objeto preenchido pela DAO
				ArrayList<Cliente> clientes = daoCliente.consultarTodos(start,limit);	
				
				//Verifica se a lista NÃO está vazia
				if (!clientes.isEmpty()){
					
					//Cria o objeto de contagem
					Iterator<Cliente> i = clientes.iterator();
					
					//Cria o objeto Especialidade
					Cliente cliente = new Cliente();
					
					//Cria o objeto que trata as conversões de objetos JAVA em JSON
					Gson gson = new Gson();
					
					//cria a estrutura do objeto necessário para popular jqgrid
					String resposta = "{\"total\":\""+total+"\",\"page\":\""+page+"\",\"records\":\""+clientes.size()+"\",\"rows\":[";				
					
					//Adiciona os objetos a string que servirá como option
					while (i.hasNext()) {
						
						cliente = (Cliente) i.next();

						resposta += gson.toJson(cliente)+",";
					}
					
					String fim = "]}";
					
					resposta += fim;
						
					resposta = resposta.replace(",]","]");

					out.print(resposta);
				} else {
					String resposta = "{\"mensagem\":\"Não há clientes!\"}";
					
					out.print(resposta);
				}
			} else if(acao.equals("Alterar")) {
				
				//Recupera todos os campos do cliente
				String matricula		   = request.getParameter("matricula");
				String senha 	  	       = request.getParameter("senha");
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
				
				String logradouro 	   = request.getParameter("logradouro");
				String complemento 	   = request.getParameter("complemento");
				String bairro	 	   = request.getParameter("bairro");
				String cep		 	   = request.getParameter("cep");
				String cidade 	  	   = request.getParameter("cidade");
				
				//Cria o objeto especialidade e define o campo matricula
				Cliente cliente = new Cliente();
				cliente.setMatricula(matricula);
				
				//Retorna o objeto preenchido pela DAO
				cliente = daoCliente.consultar(cliente);	
				
				//Cria a String de resposta
				String resposta = "";
				
				//Verifica se o objeto NÃO está nulo
				if(cliente != null) {
					
					cliente.setNome(nome);
					cliente.setSenha(senha);
					cliente.setCpf(cpf);
					cliente.setRg(rg);
					cliente.setDataNascimento(dataNasc);
					cliente.setTelefoneResidencia(telefoneResidencia);
					cliente.setTelefoneCelular(telefoneCelular);
					
					Endereco endereco = new Endereco();
					
					endereco.setIdEndereco(cliente.getEndereco().getIdEndereco());
					endereco.setBairro(bairro);
					endereco.setCep(cep);
					endereco.setCidade(cidade);
					endereco.setComplemento(complemento);
					endereco.setLogradouro(logradouro);
					
					cliente.setEndereco(endereco);
					
					daoCliente.alterar(cliente);
					
					resposta = "{\"mensagem\":\"Cliente Alterado com sucesso!\"}";
					
					out.print(resposta);
				} else {
					
					resposta = "{\"mensagem\":\"Cliente não existe!\"}";
				}
			} else if(acao.equals("Excluir")) {
				
				//Recupera o campo matricula
				String matricula = request.getParameter("matricula");
				
				//Cria o objeto Dependente e atribui a matricula
				Cliente cliente = new Cliente();
				cliente.setMatricula(matricula);
				
				//Cria uma lista com todos os dependentes
				Cliente auxiliar = daoCliente.consultar(cliente);
				
				//Cria a String de resposta
				String resposta = "";
				
				//verifica se o dependente já existe
				if(auxiliar != null) {
					
					daoCliente.excluir(auxiliar);
					
					resposta = "{\"mensagem\":\"Cliente Excluído com sucesso!\"}";
					
					out.print(resposta);
				} else {
					
					resposta = "{\"mensagem\":\"Cliente não existe!\"}";

					out.print(resposta);
				}
			}
		} else {//procedimentos para um cliente com plano de saude
			
		}	
	}
}
