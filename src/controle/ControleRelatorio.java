package controle;

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

import dao.DAOMedico;
import dao.DAORelatorio;

import modelo.Cliente;
import modelo.Especialidade;
import modelo.Medico;


public class ControleRelatorio extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Recupera a acao
		String acao = request.getParameter("acao");
		
		//Cria o DAORelatorio
		DAORelatorio daoRelatorio = new DAORelatorio();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if(acao.equals("clienteEspecialidade")) {
			
			//Recupera a acao
			String nome = request.getParameter("especialidade");
			String dataI = request.getParameter("dataInicio");
			String dataF = request.getParameter("dataFim");
			
			//Cria o objeto Calendar
			Calendar dataInicio = new GregorianCalendar();
			Calendar dataFim = new GregorianCalendar();
				
			//Transforma para a String para Date
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			//Transforma a data para Calendar
			try {
				dataInicio.setTime((Date) formatter.parse(dataI));
				dataFim.setTime((Date) formatter.parse(dataF));
			} catch (Exception e) {
				System.out.println(e);
			}
			
			//Cira o objeto especialidade
			Especialidade especialidade = new Especialidade();
			especialidade.setNome(nome);
			
			//Cria uma lista com todos os clientes de uma especialidade
			ArrayList<Cliente> listaCliente = daoRelatorio.listaCliente(especialidade, dataInicio, dataFim);

			double mediaIdade = 0;
			
			//Verifica se a lista NÃO está vazia
			if (!listaCliente.isEmpty()){

				//Cria o objeto de contagem
				Iterator<Cliente> i = listaCliente.iterator();
				
				while (i.hasNext()) {
					Cliente cliente = new Cliente();
					
					cliente  = i.next();
				
					//Verifica a idade do Dependente
					Calendar hoje = Calendar.getInstance();
					Calendar aux = new GregorianCalendar();
					
					//adiciona a data de nascimento ao objeto auxiliar
					aux.setTime(cliente.getDataNascimento().getTime());
					
					//subtrai os anos das duas datas.
					int idade = hoje.get(Calendar.YEAR) - aux.get(Calendar.YEAR);
					aux.add(Calendar.YEAR, idade);
					
					if(hoje.before(aux)) {
						mediaIdade += --idade;
					} else {
						mediaIdade += idade;
					}
				}
				
				mediaIdade /= listaCliente.size();
			}
			
			//cria a estrutura do objeto necessário para popular jqgrid
			String resposta = "{\"total\":\""+1+"\",\"page\":\""+0+"\",\"records\":\"1\",\"rows\":[{\"especialidade\":\""+nome+"\",\"mediaIdade\":\""+mediaIdade+"\"}]}";
			
			out.print(resposta);
			
			
		} else if(acao.equals("especialidadeAtendimento")) {
			
			//Recupera a acao
			String dataI = request.getParameter("dataInicio");
			String dataF = request.getParameter("dataFim");
			
			//Cria o objeto Calendar
			Calendar dataInicio = new GregorianCalendar();
			Calendar dataFim = new GregorianCalendar();
				
			//Transforma para a String para Date
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			//Transforma a data para Calendar
			try {
				dataInicio.setTime((Date) formatter.parse(dataI));
				dataFim.setTime((Date) formatter.parse(dataF));
			} catch (Exception e) {
				System.out.println(e);
			}
			
			//Cria uma lista com todas as especialidades e quantidades de atendimento
			ArrayList<String> listaEspecialidade = daoRelatorio.listaEspecialidade(dataInicio, dataFim);
			
			//Verifica se a lista NÃO está vazia
			if (!listaEspecialidade.isEmpty()){

				//Cria o objeto de contagem
				Iterator<String> i = listaEspecialidade.iterator();
				
				String resposta = "{\"total\":\"1\",\"page\":\"1\",\"records\":\""+listaEspecialidade.size()/2+"\",\"rows\":[";
				String dados = "";
				
				while (i.hasNext()) {
					String especialidade = i.next();
					String qtdAtendimento = i.next();
					
					dados += "{\"especialidade\":\""+especialidade+"\",\"qtdAtendimento\":\""+qtdAtendimento+"\"},";
				}
				
				resposta += dados;
				
				String fim = "]}";
						
				resposta += fim;
							
				resposta = resposta.replace(",]","]");
				
				out.print(resposta);
			}
		} else if(acao.equals("atendimentoMedico")) {
			
			//Recupera os campos
			String matricula = request.getParameter("matriculaMedico");
			String dataI = request.getParameter("dataInicio");
			String dataF = request.getParameter("dataFim");
			
			//Cria o objeto Calendar
			Calendar dataInicio = new GregorianCalendar();
			Calendar dataFim = new GregorianCalendar();
				
			//Transforma para a String para Date
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			//Transforma a data para Calendar
			try {
				dataInicio.setTime((Date) formatter.parse(dataI));
				dataFim.setTime((Date) formatter.parse(dataF));
			} catch (Exception e) {
				System.out.println(e);
			}
			
			//Cria o objeto Medico e define a matricula
			Medico medico = new Medico();
			medico.setMatricula(matricula);
			
			//Cria o objeto DAOMedico
			DAOMedico daoMedico = new DAOMedico();
			medico = daoMedico.consultaMatricula(medico);
			
			if(medico == null) {
					
				String resposta = "{\"mensagem\":\"Medico inexistente!\"}";
					
				out.print(resposta);
			} else {
				
				int qtdAtendimento = daoRelatorio.listaAtendimento(medico, dataInicio, dataFim);
				
				//cria a estrutura do objeto necessário para popular jqgrid
				String resposta = "{\"total\":\""+1+"\",\"page\":\""+0+"\",\"records\":\"1\",\"rows\":[{\"medico\":\""+medico.getNome()+"\",\"atendimento\":\""+qtdAtendimento+"\"}]}";
				
				out.print(resposta);
				
			}
		}
	}

}
