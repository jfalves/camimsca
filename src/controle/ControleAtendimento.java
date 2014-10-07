package	controle;

import implementacao.Prontuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Atendimento;
import modelo.Medico;

import dao.DAOAtendimento;
import dao.DAOMedico;
import dao.DAOPagamentoConsulta;

public class ControleAtendimento extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String status[] = {"Agendado", "Em Andamento", 
			 "Atrasado", "Cancelado", "Finalizado"};

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Recupera o campo acao
		String acao = request.getParameter("acao");

		//Cria o objeto DAOHorarioAtendimento
		DAOAtendimento daoAtendimento = new DAOAtendimento();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		//Executa o procedimento de acordo com a acao
		//Executa o procedimento de acordo com a acao
		if(acao.equals("listaStatus")) {
			
			String stat = request.getParameter("status");
			
			//Cria o objeto Atendimento
			Atendimento atendimento = new Atendimento();
			
			if(stat.equals("")) {
				//Cria uma lista com todos os dias de atendimento de um determinado medico
				ArrayList<Atendimento> listaStatus = daoAtendimento.listaStatus();
				
				//Verifica se a lista NÃO está vazia
				if (!listaStatus.isEmpty()){
					
					//Cria o objeto de contagem
					Iterator<Atendimento> i = listaStatus.iterator();
					
					//Adiciona os objetos a string que servirá como option
					while (i.hasNext()) {
						atendimento = (Atendimento) i.next(); 
						
						out.print("<option value="+atendimento.getStatus()+" selected='selected'>"+atendimento.getStatus()+"</option>");
					}
				}
			} else if(stat.equals(status[1])) {
				
				//altera o status
				atendimento.setStatus(status[1]);
				
				//Atualiza no banco de dados
				daoAtendimento.alteraStatus(atendimento);
				
				out.print("<option value="+atendimento.getStatus()+" selected='selected'>"+atendimento.getStatus()+"</option>");
				
			} else if(stat.equals(status[4])) {
				
				//altera o status
				atendimento.setStatus(status[4]);
				
				//Atualiza no banco de dados
				daoAtendimento.alteraStatus(atendimento);
				
				out.print("<option value="+atendimento.getStatus()+" selected='selected'>"+atendimento.getStatus()+"</option>");
			}
			
		} else if(acao.equals("alteraStatus")) {
			
			int idAtendimento = Integer.parseInt(request.getParameter("idAtendimento"));
			String stat = request.getParameter("status");
			
			//Cria o objeto Atendimento
			Atendimento atendimento = new Atendimento();
			
			//altera o idAtendimento e status
			atendimento.setIdAtendimento(idAtendimento);
			atendimento.setStatus(stat);
			
			//Cria o objeto auxiliar para consulta de status
			Atendimento aux = new Atendimento();
			aux = daoAtendimento.consultaStatus(atendimento);
			
			//Verifica o status corrente
			if(atendimento.getStatus().equals(aux.getStatus())) {
				
				String resposta = "{\"mensagem\":\"Status já ativo!\"}";
				
				out.print(resposta);
			} else {
			
				//Atualiza no banco de dados
				daoAtendimento.alteraStatus(atendimento);
			}
	
		} else if(acao.equals("listaAgenda")) {
			
			//Recupera os campos restantes
			String matriculaMedico = request.getParameter("matriculaMedico");
			
			//Cria o objeto Medico e define a matricula
			Medico medico = new Medico();
			medico.setMatricula(matriculaMedico);
			
			//Cria o objeto DAOMedico
			DAOMedico daoMedico = new DAOMedico();
			medico = daoMedico.consultaMatricula(medico);
			
			if(medico == null) {
					
				String resposta = "{\"mensagem\":\"Medico inexistente!\"}";
					
				out.print(resposta);
			} else {
				
				//Cria o objeto Atendimento
				Atendimento auxiliar = new Atendimento();
				
				//Define o campo medico
				auxiliar.setMedico(medico);

				//Cria uma lista com todos os dias de atendimento de um determinado medico
				ArrayList<Atendimento> listaAtendimento = daoAtendimento.listaAtendimento(auxiliar);
				
				//Verifica se a lista NÃO está vazia
				if (!listaAtendimento.isEmpty()){
					
					//Cria o objeto de contagem
					Iterator<Atendimento> i = listaAtendimento.iterator();
					
					//Cria o objeto Atendimento
					Atendimento atendimento = new Atendimento();
					
					//Cria o objeto SimpleDateFormat para mascarar o campo string
					Format formatador = new SimpleDateFormat("HH:mm");
					
					//cria a estrutura do objeto necessário para popular jqgrid
					String resposta = "{\"total\":\"1\",\"page\":\"1\",\"records\":\""+listaAtendimento.size()+"\",\"rows\":[";
					String dados = "";
					
					while (i.hasNext()) {
						
						//Define o objeto Atendimento atra´ves da lista retornada
						atendimento = i.next();
						
						//retorna a data e hora corrente (aux) e cria o ojbeto que recebe a referencia da hora corrente
						Calendar aux = Calendar.getInstance();   
						Calendar hoje = new GregorianCalendar();
						
						//Reseta todas as informações referente a data
						hoje.setTimeInMillis(atendimento.getHora().getTimeInMillis());
						hoje.set(Calendar.HOUR, aux.get(Calendar.HOUR));
						hoje.set(Calendar.HOUR_OF_DAY, aux.get(Calendar.HOUR_OF_DAY));
						hoje.set(Calendar.MINUTE, aux.get(Calendar.MINUTE));
						
						//Cria as horas de atendimento com acrescimo
						Calendar atendimentoT = new GregorianCalendar(); //tres minutos de acrescimo
						Calendar atendimentoU = new GregorianCalendar(); //uma hora de acrescimo 

						//Acrescenta 3 minutos a hora de atendimento
						atendimentoT.setTimeInMillis(atendimento.getHora().getTimeInMillis());
						
						atendimentoT.add(Calendar.MINUTE, 3);

						//Acrescenta 1 hora a hora de atendimento
						atendimentoU.setTimeInMillis(atendimento.getHora().getTimeInMillis());
						
						atendimentoU.add(Calendar.HOUR, 1);
						
						//verifica se o cliente está atrasado
						if(hoje.getTimeInMillis() > atendimentoT.getTimeInMillis() && hoje.getTimeInMillis() < atendimentoU.getTimeInMillis()) {
							
							//altera o status
							atendimento.setStatus(status[2]);
							
							//Atualiza no banco de dados
							daoAtendimento.alteraStatus(atendimento);
							
						} else if ( hoje.getTimeInMillis() > atendimentoU.getTimeInMillis()) {
							
							//altera o status
							atendimento.setStatus(status[3]);
							
							//Atualiza no banco de dados
							daoAtendimento.alteraStatus(atendimento);
						}

						//Transforma a hora do atendimento em String através do formatador
						String hora = formatador.format(atendimento.getHora().getTime());
						
						dados += "{\"id\":\""+atendimento.getIdAtendimento()+"\",\"cliente\":\""+atendimento.getCliente().getNome()+"\",\"horario\":\""+hora+"\",\"status\":\""+atendimento.getStatus()+"\"},";
						
					}
					
					resposta += dados;
					
					String fim = "]}";
							
					resposta += fim;
								
					resposta = resposta.replace(",]","]");
					
					out.print(resposta);
				}
			}
		} else if(acao.equals("finalizaAtendimento")) {
			
			//Recupera os campos restantes
			int idAtendimento = Integer.parseInt(request.getParameter("idAtendimento"));
			String matriculaMedico = request.getParameter("matriculaMedico");
			String stat = request.getParameter("status");
			String pront = request.getParameter("prontuario");
			String receituario = request.getParameter("receituario");
			
			//Cria o objeto Medico e define a matricula
			Medico medico = new Medico();
			medico.setMatricula(matriculaMedico);
			
			//Cria o objeto DAOMedico
			DAOMedico daoMedico = new DAOMedico();
			medico = daoMedico.consultaMatricula(medico);
			
			//Cria o objeto atendimento e prontuario
			Atendimento atendimento = new Atendimento();	
			Prontuario prontuario = new Prontuario();
			
			//Define os atributos
			atendimento.setIdAtendimento(idAtendimento);
			atendimento.setStatus(stat);
			atendimento.setMedico(medico);
			
			prontuario.setProntuario(pront);
			prontuario.setReceituario(receituario);
			
			atendimento.setProntuario(prontuario);
			
			boolean resultado = daoAtendimento.registraAtendimento(atendimento);
			
			String resposta = "";
			
			if(resultado) {
				
				DAOPagamentoConsulta daoPagamentoConsulta = new DAOPagamentoConsulta();
				
				daoPagamentoConsulta.geraPagamentoConsulta(atendimento);
				
				resposta = "{\"mensagem\":\"Atendimento Finalizado com Sucesso!\"}";
			} else {
				
				resposta = "{\"mensagem\":\"Houve uma falha por favor tente novamente!\"}";
			}

			out.print(resposta);
		}
	}
}
