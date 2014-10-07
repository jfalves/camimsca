package controle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.*;
import dao.*;

public class ControlePagamentoConsulta extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Recupera o campo acao
		String acao = request.getParameter("acao");

		//Cria o objeto DAOPagamentoConsulta
		DAOPagamentoConsulta daoPagamentoConsulta = new DAOPagamentoConsulta();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//Executa o procedimento de acordo com a acao		
		if(acao.equals("listaFormaPagamento")) {
			
			//Cria uma lista com as formas de pagamento
			ArrayList<PagamentoConsulta> listaFormaPagamento = daoPagamentoConsulta.listaFormaPagamento();
			
			//Verifica se a lista NÃO está vazia
			if (!listaFormaPagamento.isEmpty()){
				
				//Cria o objeto de contagem
				Iterator<PagamentoConsulta> i = listaFormaPagamento.iterator();
				
				//Cria o objeto PlanoSaude
				PagamentoConsulta pagamentoConsulta = new PagamentoConsulta();
				
				//Adiciona os objetos a string que servirá como option
				while (i.hasNext()) {
					pagamentoConsulta = (PagamentoConsulta) i.next(); 
					
					out.print("<option value="+pagamentoConsulta.getFormaPagamento()+" selected='selected'>"+pagamentoConsulta.getFormaPagamento()+"</option>");
				}
			}
		} else if(acao.equals("listaPagamentoConsulta")) {
			
			//Recupera os campos restantes
			String matriculaCliente = request.getParameter("matriculaCliente");
			String matriculaFuncionario = request.getParameter("matriculaFuncionario");
			
			//Cria o objeto Cliente e define a matricula
			Cliente cliente = new Cliente();
			cliente.setMatricula(matriculaCliente);
			
			//Cria o objeto DAOCliente
			DAOCliente daoCliente = new DAOCliente();
			cliente = daoCliente.consultaMatricula(cliente);
			
			if(cliente == null) {
				
				String resposta = "{\"mensagem\":\"Cliente inexistente!\"}";
					
				out.print(resposta);
			} else {
				
				//Cria o objeto Funcionario e define a matricula
				Funcionario funcionario = new Funcionario();
				funcionario.setMatricula(matriculaFuncionario);
				
				//Cria o objeto DAOFuncionario
				DAOFuncionario daoFuncionario = new DAOFuncionario();
				funcionario = daoFuncionario.consultaMatricula(funcionario);
				
				if(funcionario == null) {
					
					String resposta = "{\"mensagem\":\"Funcionario inexistente!\"}";
					
					out.print(resposta);
				} else {
					
					//Cria o objeto DAOAtendimento
					DAOAtendimento daoAtendimento = new DAOAtendimento();
					
					//Cria uma lista com todos os atendimento finalizados
					ArrayList<Atendimento> listaAtendimentoConsultaFinalizado = daoAtendimento.listaAtendimentoConsultaFinalizado(cliente);
					
					//Verifica se a lista NÃO está vazia
					if (!listaAtendimentoConsultaFinalizado.isEmpty()){
						
						//Cria uma lista para armazenar os pagamentos
						ArrayList<PagamentoConsulta> listaPagamentoConsulta = new ArrayList<PagamentoConsulta>();
						
						//Cria o objeto de contagem
						Iterator<Atendimento> i = listaAtendimentoConsultaFinalizado.iterator();
						
						//Cria o objeto Atendimento
						Atendimento atendimento = new Atendimento();
						
						while (i.hasNext()) {
							
							//Define o objeto Atendimento atraves da lista retornada
							atendimento = i.next();
							
							//Cria o objeto PagamentoConsulta(auxiliar) e define o cliente e atendimento
							PagamentoConsulta auxiliar = new PagamentoConsulta();
							auxiliar.setCliente(cliente);
							auxiliar.setAtendimento(atendimento);
							
							//retorna o valor de um pagamento
							auxiliar = daoPagamentoConsulta.consultaValor(auxiliar);
							
							//Adiciona o objeto ConsultaPagamento a lista
							listaPagamentoConsulta.add(auxiliar);
						}

						//Cria o objeto de contagem
						Iterator<PagamentoConsulta> j = listaPagamentoConsulta.iterator();
						
						//Cria o objeto PagamentoConsulta
						PagamentoConsulta pagamentoConsulta = new PagamentoConsulta();
						
						//cria a estrutura do objeto necessário para popular jqgrid
						String resposta = "{\"total\":\""+1+"\",\"page\":\""+0+"\",\"records\":\""+listaPagamentoConsulta.size()+"\",\"rows\":[";
						
						String dados = "";
						
						while (j.hasNext()) {
							
							//Define o objeto PagamentoConsulta atraves da lista retornada
							pagamentoConsulta = j.next();
							
							dados += "{\"id\":\""+pagamentoConsulta.getIdPagamentoConsulta()+"\",\"cliente\":\""+pagamentoConsulta.getCliente().getNome()+"\",\"medico\":\""+pagamentoConsulta.getAtendimento().getMedico().getNome()+"\",\"especialidade\":\""+pagamentoConsulta.getAtendimento().getMedico().getEspecialidade().getNome()+"\",\"valor\":\""+pagamentoConsulta.getValor()+"\"},";
						}
						
						resposta += dados;
						
						String fim = "]}";
								
						resposta += fim;
									
						resposta = resposta.replace(",]","]");
						
						out.print(resposta);
					}
				}
			}
		} else if(acao.equals("pagar")){
			
			//Recupera os campos restantes
			String matriculaFuncionario = request.getParameter("matriculaFuncionario");
			String formaPagamento = request.getParameter("formaPagamento");
			int idPagamentoConsulta = Integer.parseInt(request.getParameter("idPagamento"));
			
			//Cria o objeto Funcionario e define a matricula
			Funcionario funcionario = new Funcionario();
			funcionario.setMatricula(matriculaFuncionario);
			
			//Cria o objeto DAOFuncionario
			DAOFuncionario daoFuncionario = new DAOFuncionario();
			funcionario = daoFuncionario.consultaMatricula(funcionario);
			
			//Cria o objeto PagamentoConsulta e define o cliente e atendimento
			PagamentoConsulta pagamentoConsulta = new PagamentoConsulta();
			pagamentoConsulta.setFuncionario(funcionario);
			pagamentoConsulta.setFormaPagamento(formaPagamento);
			pagamentoConsulta.setIdPagamentoConsulta(idPagamentoConsulta);
			
			daoPagamentoConsulta.realizaPagamento(pagamentoConsulta);
			
			String resposta = "{\"mensagem\":\"Pagamento realizado com sucesso!\"}";
			
			out.print(resposta);
		}
		
		out.flush();
		out.close();
	}
}