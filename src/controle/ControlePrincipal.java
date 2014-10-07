package controle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControlePrincipal extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ControlePrincipal() {
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		//Recupera campos da tela
		String matricula = request.getParameter("matricula");
		String senha     = request.getParameter("senha");
		
		//Cria o objeto Pessoa e define os atributos
		Pessoa pessoa = new Pessoa();
		pessoa.setMatricula(matricula);
		pessoa.setSenha(senha);

		//Cria o objeto DAOPessoa e carrega o objeto
		DAOPessoa daoPessoa = new DAOPessoa();
		pessoa = daoPessoa.carregaPessoa(pessoa);
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//Valida o Acesso
		if (pessoa != null) {
			verificaAcesso(request, response, pessoa);
		} else {
			out.print("Matricula ou Senha incorreta!");
			out.flush();
			out.close();	
		}
		
	}
	
	private void verificaAcesso(Request request,Response response, Pessoa pessoa) {
	
		//Inicia a sessão
		HttpSession session = request.getSession();
		session.setAttribute("pessoa", pessoa);
		
		if(pessoa.getTipo().equals("C")) {
			pessoa = new Cliente(pessoa);
				
			//Direciona os objetos Request e Response para o controle Cliente
			RequestDispatcher rd = request.getRequestDispatcher("ControleCliente");
			rd.forward(request, response);
			
			//Direciona para o index.html
			getServletContext().getRequestDispatcher("/WebContent/visao/index.html");
		} else if(pessoa.getTipo().equals("F")) {
			pessoa = new Funcionario(pessoa);
			
			//cria a DAOFuncionario e carrega o resto do objeto
			DAOFuncionario daoFuncionario = new DAOFuncionario();
			pessoa = (Funcionario) daoFuncionario.carregaFuncionario((Funcionario) pessoa);
			
			//Direciona os objetos Request e Response para o controle Funcionario
			RequestDispatcher rd = request.getRequestDispatcher("ControleFuncionario");
			rd.forward(request, response);
			
			//Direciona para o index.html
			getServletContext().getRequestDispatcher("/WebContent/visao/index.html");				
		}*/
		
	}
}
