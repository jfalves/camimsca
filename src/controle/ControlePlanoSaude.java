package controle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import modelo.PlanoSaude;
import dao.DAOPlanoSaude;

public class ControlePlanoSaude extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Recupera a acao da tela
		String acao = request.getParameter("acao");
		
		//Cria o objeto DAOPlanoSaude
		DAOPlanoSaude daoPlanoSaude = new DAOPlanoSaude();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//Executa o procedimento de acordo com a acao		
		if(acao.equals("listaTipoPlanoSaude")) {// Consulta todos os objetos
			
			//Cria uma lista com todos os tipos de plano de saúde
			ArrayList<PlanoSaude> listaTipoPlanoSaude = daoPlanoSaude.listaTipoPlanoSaude();
			
			//Verifica se a lista NÃO está vazia
			if (!listaTipoPlanoSaude.isEmpty()){
				
				//Cria o objeto de contagem
				Iterator<PlanoSaude> i = listaTipoPlanoSaude.iterator();
				
				//Cria o objeto PlanoSaude
				PlanoSaude planoSaude = new PlanoSaude();
				
				//Adiciona os objetos a string que servirá como option
				while (i.hasNext()) {
					planoSaude = (PlanoSaude) i.next(); 
					
					out.print("<option value="+planoSaude.getTipo()+" selected='selected'>"+planoSaude.getTipo()+"</option>");
				}
			}
		} else if(acao.equals("Cadastrar")) {
			
			//Recupera todos os campos do Plano de Saude
			int idadeMinima   = Integer.parseInt(request.getParameter("idadeMinima"));
			int idadeMaxima   = Integer.parseInt(request.getParameter("idadeMaxima"));
			double valorFaixa = Double.parseDouble(request.getParameter("valorFaixa"));
			String tipo		  = request.getParameter("listaTipoPlanoSaude");
			
			//Cria o objeto PlanoSaude(auxiliar) e define os campos (IdadeMinima, idadeMaxima, tipo)
			PlanoSaude auxiliar = new PlanoSaude();			
			auxiliar.setIdadeMinima(idadeMinima);
			auxiliar.setIdadeMaxima(idadeMaxima);
			auxiliar.setTipo(tipo);
			
			//Retorna o objeto preenchido pela DAO
			auxiliar = daoPlanoSaude.consultar(auxiliar);	
			
			//verifica se o plano de saude não existe
			if(auxiliar == null) {
				
				//Cria o objeto PlanoSaude e define todos os campos
				PlanoSaude planoSaude = new PlanoSaude();
				planoSaude.setIdadeMinima(idadeMinima);
				planoSaude.setIdadeMaxima(idadeMaxima);
				planoSaude.setValorFaixa(valorFaixa);
				planoSaude.setTipo(tipo);
				
				//Verifica se a faixa de idades é válida
				if(daoPlanoSaude.verificaFaixa(planoSaude) == true){
					
					daoPlanoSaude.cadastrar(planoSaude);
					
					String resposta = "{\"mensagem\":\"Especialidade Cadastrada com sucesso!\"}";
					
					out.print(resposta);
				} else {
					String resposta = "{\"mensagem\":\"Faixa de idade já existe!\"}";
					
					out.print(resposta);
				}
			} else {
				String resposta = "{\"mensagem\":\"Plano de Saúde já existe!\"}";
				
				out.print(resposta);
			}
		} else if(acao.equals("Consultar")) {
			
			//Recupera a pagina requerida, define o limite de registros do jqgrid e conta a quantidade de registros
			int page = Integer.parseInt(request.getParameter("page"));
			int limit = 10;
			int count = daoPlanoSaude.qtdRegistro();
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
			ArrayList<PlanoSaude> planoSaudes = daoPlanoSaude.consultarTodos(start,limit);	
			
			//Verifica se a lista NÃO está vazia
			if (!planoSaudes.isEmpty()){
				
				//Cria o objeto de contagem
				Iterator<PlanoSaude> i = planoSaudes.iterator();
				
				//Cria o objeto Especialidade
				PlanoSaude planoSaude = new PlanoSaude();
				
				//Cria o objeto que trata as conversões de objetos JAVA em JSON
				Gson gson = new Gson();
				
				//cria a estrutura do objeto necessário para popular jqgrid
				String resposta = "{\"total\":\""+total+"\",\"page\":\""+page+"\",\"records\":\""+planoSaudes.size()+"\",\"rows\":[";				
				
				//Adiciona os objetos a string que servirá como option
				while (i.hasNext()) {
					
					planoSaude = (PlanoSaude) i.next();

					resposta += gson.toJson(planoSaude)+",";
				}
				
				String fim = "]}";
				
				resposta += fim;
					
				resposta = resposta.replace(",]","]");

				out.print(resposta);
			} else {
				String resposta = "{\"mensagem\":\"Plano de Saude não existe!\"}";
				
				out.print(resposta);
			}
		} else if(acao.equals("Alterar")) {
			
			//Recupera todos os campos do Plano de Saude
			int idadeMinima   = Integer.parseInt(request.getParameter("idadeMinima"));
			int idadeMaxima   = Integer.parseInt(request.getParameter("idadeMaxima"));
			double valorFaixa = Double.parseDouble(request.getParameter("valorFaixa"));
			String tipo		  = request.getParameter("listaTipoPlanoSaude");
			
			//Cria o objeto PlanoSaude(auxiliar) e define os campos (IdadeMinima, idadeMaxima, tipo)
			PlanoSaude planoSaude = new PlanoSaude();			
			planoSaude.setIdadeMinima(idadeMinima);
			planoSaude.setIdadeMaxima(idadeMaxima);
			planoSaude.setTipo(tipo);
			
			//Retorna o objeto preenchido pela DAO
			planoSaude = daoPlanoSaude.consultar(planoSaude);
			
			//Verifica se o objeto NÃO está nulo
			if (planoSaude != null) {
				
				planoSaude.setValorFaixa(valorFaixa);
				
				daoPlanoSaude.alterar(planoSaude);
				
				String resposta = "{\"mensagem\":\"Plano de saúde Alterado com sucesso!\"}";
				
				out.print(resposta);
			} else {
				String resposta = "{\"mensagem\":\"Plano de saúde não existe!\"}";
				
				out.print(resposta);
			} 
		} else if(acao.equals("Excluir")) {
			
			//Recupera os campos do Plano de Saude (idadeMinima, idadeMaxima e tipo)
			int idadeMinima   = Integer.parseInt(request.getParameter("idadeMinima"));
			int idadeMaxima   = Integer.parseInt(request.getParameter("idadeMaxima"));
			String tipo		  = request.getParameter("listaTipoPlanoSaude");
			
			//Cria o objeto PlanoSaude(auxiliar) e define os campos (IdadeMinima, idadeMaxima, tipo)
			PlanoSaude planoSaude = new PlanoSaude();			
			planoSaude.setIdadeMinima(idadeMinima);
			planoSaude.setIdadeMaxima(idadeMaxima);
			planoSaude.setTipo(tipo);
			
			//Retorna o objeto preenchido pela DAO
			planoSaude = daoPlanoSaude.consultar(planoSaude);
			
			//verifica se o plano de saúde já existe
			if(planoSaude != null) {
				daoPlanoSaude.excluir(planoSaude);
				
				String resposta = "{\"mensagem\":\"Plano de saúde Excluído com sucesso!\"}";
				
				out.print(resposta);
			} else {
				String resposta = "{\"mensagem\":\"Plano de saúde não existe!\"}";
				
				out.print(resposta);
			}
		}  
		
		out.flush();
		out.close();
	}
}