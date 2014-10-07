package controle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Pessoa;
import dao.DAOMedico;

public class ControleMedico extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Recupera a acao e a matricula
		String acao = request.getParameter("acao");
		String especialidade = request.getParameter("especialidade");
		
		//Cria o objeto DAOMedico
		DAOMedico daoMedico = new DAOMedico();		
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if(acao.equals("listaMedico")) {
			//Cria uma lista com todos os tipos de plano de saúde
			ArrayList<Pessoa> listaMedico = daoMedico.listaMedicoCombo(especialidade);
		
			//Verifica se a lista NÃO está vazia
			if (!listaMedico.isEmpty()){
				
				//Cria o objeto de contagem
				Iterator<Pessoa> i = listaMedico.iterator();
				int indice = 1;
				
				//Adiciona os objetos a string que servirá como option
				while (i.hasNext()) {
					Pessoa pessoa = i.next();
					out.print("<option value="+indice+++">"+pessoa.getNome()+"</option>");
				}
			}
		}
	}

}
