package controle;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOPessoa;

public class ControlePessoa extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Recupera a acao
		String acao = request.getParameter("acao");
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//Executa o procedimento de acordo com a acao
		if (acao.equals("geraMatricula")) {
			
			//Cria o objeto DAOPessoa
			DAOPessoa daoPessoa = new DAOPessoa();
			
			//Cria a string da matricula
			String matricula = "";
			
			do {
				
				//Gera um numero aleatório
				int auxiliar = (int)(Math.random() * 1000000);
				
				//converte para string o numero gerado
				matricula = String.valueOf(auxiliar);
				
			  //Verifica se a matricula já existe e se contem 6 digitos
			} while(daoPessoa.verificaMatricula(matricula) && matricula.length() == 6);
			
			//retorna a resposta
			out.print(matricula);

		}
		/*if(acao.equals("pesquisar")) {
			//Recupera o nome
			String nome = request.getParameter("nome");
			
			System.out.print(nome);
			
			//Cria o objeto DAO
			DAOPessoa daoPessoa = new DAOPessoa();
			
			//Cria uma lista com todos os clientes
			ArrayList<Pessoa> listaPessoa = daoPessoa.pesquisar(nome);
			
			//Verifica se a lista NÃO está vazia
			if (listaPessoa != null){

				//Cria o objeto de contagem
				Iterator<Pessoa> i = listaPessoa.iterator();
				
				//Cria o objeto para auxiliar na exibição do conteudo
				Pessoa pessoa = new Pessoa();

				//cria a estrutura do objeto necessário para popular jqgrid
				String resposta = "{\"total\":\"1\",\"page\":\"1\",\"records\":\""+listaPessoa.size()+"\",\"rows\":[";
				String dados = ""; 
				
				//Adiciona os objetos a string
				if (i.hasNext()) {
					pessoa = (Pessoa) i.next();
					dados += "{\"nome\":"+pessoa.getNome()+"\"matricula\":"+pessoa.getMatricula()+"},";
				}
				
				resposta += dados;
				
				String fim = "]}";
				
				resposta += fim;
					
				resposta = resposta.replace(",]","]");
				
				//Envia a resposta
				out.print(resposta);
			}
		}*/
		
	}

}
