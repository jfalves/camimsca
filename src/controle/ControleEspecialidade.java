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

import com.google.gson.Gson;

import modelo.Especialidade;
import modelo.PrecoEspecialidade;
import dao.DAOEspecialidade;

public class ControleEspecialidade extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Recupera a acao
		String acao = request.getParameter("acao");
		
		//Cria o objeto DAOEspecialidade
		DAOEspecialidade daoEspecialidade = new DAOEspecialidade();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//Executa o procedimento de acordo com a acao
		if(acao.equals("listaEspecialidade")) {
			
			//Cria uma lista com todas as especialidades
			ArrayList<Especialidade> listaEspecialidadeCombo = daoEspecialidade.listaEspecialidadeCombo();
		
			//Verifica se a lista NÃO está vazia
			if (!listaEspecialidadeCombo.isEmpty()){
				
				//Cria o objeto de contagem
				Iterator<Especialidade> i = listaEspecialidadeCombo.iterator();
				
				//Cria o objeto Especialidade
				Especialidade especialidade = new Especialidade();
				
				//Adiciona os objetos a string que servirá como option
				while (i.hasNext()) {
					especialidade = (Especialidade) i.next();
				
					out.print("<option value="+especialidade.getNome()+">"+especialidade.getNome()+"</option>");
				}
			}
		 } else if (acao.equals("Cadastrar")) {
				
				//Recupera todos os campos da especialidade
				String nome = request.getParameter("nome");			
				String descricao = request.getParameter("descricao");
				double valor = Double.parseDouble(request.getParameter("valor"));
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
				
				//Cria o objeto especialidade(auxiliar) e define o campo (nome)
				Especialidade auxiliar = new Especialidade();
				auxiliar.setNome(nome);
				
				//Retorna o objeto preenchido pela DAO
				auxiliar = daoEspecialidade.consultar(auxiliar);			
				
				//verifica se a especialidade não existe
				if(auxiliar == null) {
					
					//Cria o objeto especialidade e define todos os campos
					Especialidade especialidade = new Especialidade();
					especialidade.setNome(nome);
					especialidade.setDescricao(descricao);
						
					PrecoEspecialidade precoEspecialidade = new PrecoEspecialidade();
					precoEspecialidade.setDataInicio(dataInicio);
					precoEspecialidade.setDataFim(dataFim);
					precoEspecialidade.setValor(valor);
					
					especialidade.setPrecoEspecialidade(precoEspecialidade);
					
					daoEspecialidade.cadastrar(especialidade);
						
					String resposta = "{\"mensagem\":\"Especialidade Cadastrada com sucesso!\"}";
					
					out.print(resposta);
				} else {
					String resposta = "{\"mensagem\":\"Especialidade já existe!\"}";
					
					out.print(resposta);
				}
		} else if(acao.equals("Consultar")) {
			
			//Recupera a pagina requerida, define o limite de registros do jqgrid e conta a quantidade de registros
			int page = Integer.parseInt(request.getParameter("page"));
			int limit = 10;
			int count = daoEspecialidade.qtdRegistro();
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
			ArrayList<Especialidade> especialidades = daoEspecialidade.consultarTodos(start,limit);	
			
			//Verifica se a lista NÃO está vazia
			if (!especialidades.isEmpty()){
				
				//Cria o objeto de contagem
				Iterator<Especialidade> i = especialidades.iterator();
				
				//Cria o objeto Especialidade
				Especialidade especialidade = new Especialidade();
				
				//Cria o objeto que trata as conversões de objetos JAVA em JSON
				Gson gson = new Gson();
				
				//cria a estrutura do objeto necessário para popular jqgrid
				String resposta = "{\"total\":\""+total+"\",\"page\":\""+page+"\",\"records\":\""+especialidades.size()+"\",\"rows\":[";				
				
				//Adiciona os objetos a string que servirá como option
				while (i.hasNext()) {
					
					especialidade = (Especialidade) i.next();

					resposta += gson.toJson(especialidade)+",";
				}
				
				String fim = "]}";
				
				resposta += fim;
					
				resposta = resposta.replace(",]","]");

				System.out.print(resposta);
				
				out.print(resposta);
			} else {
				String resposta = "{\"mensagem\":\"Especialidade não existe!\"}";
				
				out.print(resposta);
			}
		} else if(acao.equals("Alterar")) {
			
			//Recupera todos os campos da especialidade
			String nome = request.getParameter("nome");			
			String descricao = request.getParameter("descricao");
			double valor = Double.parseDouble(request.getParameter("valor"));
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
			
			//Cria o objeto especialidade e define o campo nome
			Especialidade especialidade = new Especialidade();
			especialidade.setNome(nome);
			
			//Retorna o objeto preenchido pela DAO
			especialidade = daoEspecialidade.consultar(especialidade);			
			
			//Verifica se o objeto NÃO está nulo
			if(especialidade != null) {
				especialidade.setDescricao(descricao);

				PrecoEspecialidade precoEspecialidade = new PrecoEspecialidade();
				precoEspecialidade.setDataInicio(dataInicio);
				precoEspecialidade.setDataFim(dataFim);
				precoEspecialidade.setValor(valor);
				
				especialidade.setPrecoEspecialidade(precoEspecialidade);
				
				daoEspecialidade.alterar(especialidade);
					
				String resposta = "{\"mensagem\":\"Especialidade Alterada com sucesso!\"}";
				
				out.print(resposta);
			} else {
				String resposta = "{\"mensagem\":\"Especialidade não existe!\"}";
				
				out.print(resposta);
			}
		} else if(acao.equals("Excluir")) {
			
			//Recupera o nome da especialidade
			String nome = request.getParameter("nome");
			
			//Cria o objeto especialidade e define o campo nome
			Especialidade especialidade = new Especialidade();
			especialidade.setNome(nome);
			
			//Retorna o objeto preenchido pela DAO
			especialidade = daoEspecialidade.consultar(especialidade);
			
			//verifica se a especialidade já existe
			if(especialidade != null) {
				daoEspecialidade.excluir(especialidade);
				
				String resposta = "{\"mensagem\":\"Especialidade Excluída com sucesso!\"}";
				
				out.print(resposta);
			} else {
				String resposta = "{\"mensagem\":\"Especialidade não existe!\"}";
				
				out.print(resposta);
			}
		}  
		
		out.flush();
		out.close();
	}
}
