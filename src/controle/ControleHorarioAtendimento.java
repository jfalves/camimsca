package controle;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Atendimento;
import modelo.Cliente;
import modelo.Dependente;
import modelo.HorarioAtendimento;
import modelo.Medico;

import dao.DAOAtendimento;
import dao.DAOCliente;
import dao.DAODependente;
import dao.DAOHorarioAtendimento;
import dao.DAOMedico;

public class ControleHorarioAtendimento extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String meses[] = {"Janeiro", "Fevereiro", 
							 "Março", "Abril", "Maio", "Junho", 
							 "Julho", "Agosto", "Setembro", "Outubro",
							 "Novembro", "Dezembro"};
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Recupera o campo acao
		String acao = request.getParameter("acao");

		//Cria o objeto DAOHorarioAtendimento
		DAOHorarioAtendimento daoHorarioAtendimento = new DAOHorarioAtendimento();
		
		//Cria o objeto de resposta
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		//Executa o procedimento de acordo com a acao
		if(acao.equals("listaSemana")) {
			
			String semana = "";
			
			Calendar hoje = Calendar.getInstance();   
			
			hoje.setFirstDayOfWeek(Calendar.SUNDAY);
			
			Calendar data = new GregorianCalendar();
			
			data.set(Calendar.YEAR, hoje.get(Calendar.YEAR));   
			data.setFirstDayOfWeek(Calendar.SUNDAY);   
			data.set(Calendar.WEEK_OF_YEAR, hoje.get(Calendar.WEEK_OF_YEAR));
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			int j = hoje.getMaximum(Calendar.WEEK_OF_MONTH) - hoje.get(Calendar.WEEK_OF_MONTH);
			
			
			for(int i = 1; i <= j; ++i) {
				
				data.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
					
				if(data.get(Calendar.MONTH) < hoje.get(Calendar.MONTH)) {
					data.set(Calendar.MONTH, hoje.get(Calendar.MONTH));
					data.set(Calendar.DAY_OF_MONTH, hoje.getActualMinimum(Calendar.DAY_OF_MONTH));
				}
				
				semana = sdf.format(data.getTime())+" a ";   
					  
				data.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);   
					
				if(data.get(Calendar.MONTH) > hoje.get(Calendar.MONTH)) {
					data.set(Calendar.MONTH, hoje.get(Calendar.MONTH));
					data.set(Calendar.DAY_OF_MONTH, hoje.getActualMaximum(Calendar.DAY_OF_MONTH));
				}
					
				semana += sdf.format(data.getTime());
					
				data.set(Calendar.WEEK_OF_YEAR, hoje.get(Calendar.WEEK_OF_YEAR)+i);
				
				out.print("<option value="+i+">"+semana+"</option>");
			}
		
		} else if(acao.equals("listaHorario")) {
			
			//Recupera os campos restantes
			String matricula = request.getParameter("matricula");
			String nomeMedico = request.getParameter("listaMedico");
			String semana = request.getParameter("listaSemana");
			
			//Cria o objeto Cliente e define a matricula
			Cliente cliente = new Cliente();
			cliente.setMatricula(matricula);
			
			//Cria o objeto DAOCliente
			DAOCliente daoCliente = new DAOCliente();
			cliente = daoCliente.consultaMatricula(cliente);
			
			//Cria o objeto Dependente e define a matricula
			Dependente dependente = new Dependente();
			dependente.setMatricula(matricula);
			
			//Cria o objeto DAODependente
			DAODependente daoDependente = new DAODependente();
			dependente = daoDependente.consultaMatricula(dependente);
			
			if(cliente == null && dependente == null ) {

				String resposta = "{\"mensagem\":\"Cliente ou Dependente inexistente!\"}";
						
				out.print(resposta);
			} 
			
			if(cliente != null || dependente != null) {
				
				//Cria o objeto Medico
				Medico medico = new Medico();
				medico.setNome(nomeMedico);
				
				//Cria o objeto DAOMedico
				DAOMedico daoMedico = new DAOMedico();
				medico = daoMedico.consultaMedicoNome(medico);
				
				//Separa as datas de inicio e fim da semana
				String semanas[] = semana.split(" a ");  
				
				//Cria o objeto Calendar
				Calendar inicioSemana = new GregorianCalendar();
				Calendar fimSemana = new GregorianCalendar();
					
				//Transforma para a String para Date
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				
				//Transforma a data para Calendar
				try {
					inicioSemana.setTime((Date) formatter.parse(semanas[0]));
					fimSemana.setTime((Date) formatter.parse(semanas[1]));
				} catch (Exception e) {
					System.out.println(e);
				}

				//Cria o objeto HorarioAtendimento(auxiliar)
				HorarioAtendimento auxiliar = new HorarioAtendimento();
				auxiliar.setMedico(medico);
					
				//Cria o objeto DAOAtendimento
				DAOAtendimento daoAtendimento = new DAOAtendimento();		
					
				//Cria o objeto Calendar e define o mes corrente
				Calendar hoje = Calendar.getInstance(); 
				auxiliar.setMesAtendimento(meses[hoje.get(Calendar.MONTH)]);
				
				//Cria uma lista com todos os dias de atendimento de um determinado medico
				ArrayList<HorarioAtendimento> listaHorarioAtendimento = daoHorarioAtendimento.listaHorarioAtendimento(auxiliar);
				
				//Verifica se a lista NÃO está vazia
				if (!listaHorarioAtendimento.isEmpty()){
	
					//Cria o objeto de contagem
					Iterator<HorarioAtendimento> i = listaHorarioAtendimento.iterator();
					
					//Cria o objeto HorarioAtendimento
					HorarioAtendimento horarioAtendimento = new HorarioAtendimento();
					
					//Cria o array para montar a grade de horario
					String []grade = new String[0];
					
					while (i.hasNext()) {
						
						//Define o objeto HorarioAtendimento atra´ves da lista retornada
						horarioAtendimento = i.next();
						horarioAtendimento.setMedico(auxiliar.getMedico());
				
						//Aloca espaço no vetor para armazenar os dias de atendimento
						grade = alocaEspaco(horarioAtendimento, grade);
						
						//Cria o objeto Calendar para abrigar as horas de Inicio
						Calendar horaInicio = new GregorianCalendar();
						 
						//Define os campos horaInicio
						horaInicio = horarioAtendimento.getHoraInicio();
						
						//Cria o objeto SimpleDateFormat para mascarar o campo string
						Format formatador = new SimpleDateFormat("HH:mm");
						
						//insere as hors nos respectivos dias de consulta de acordo com o horario do médico além de verificar a disponibilidade de horario
						for(int j = 0; j < grade.length; j++ ) {
							
							//Transforma a hora do atendimento em String através do formatador
							String hora = formatador.format(horaInicio.getTime());
								
							//Verifica os Dias da semana e insere os horarios
							if(horarioAtendimento.getDiaAtendimento().equals("Segunda")) {
								if(daoAtendimento.verificaDisponibilidade(horarioAtendimento) == true) {
									if(fimSemana.get(Calendar.DAY_OF_WEEK) > 1 && hoje.get(Calendar.DAY_OF_WEEK) > 1) {
										grade[j] = grade[j].replace(",\"segunda\":\"X\"",",\"segunda\":\""+hora+"\"");
									}
								}
							}
							
							if(horarioAtendimento.getDiaAtendimento().equals("Terça")) {
								if(daoAtendimento.verificaDisponibilidade(horarioAtendimento) == true) {
									if((inicioSemana.get(Calendar.DAY_OF_WEEK) < 2 && fimSemana.get(Calendar.DAY_OF_WEEK) > 2) && hoje.get(Calendar.DAY_OF_WEEK) > 2) {
										grade[j] = grade[j].replace(",\"terca\":\"X\"",",\"terca\":\""+hora+"\"");
									}
								}
							}
								
							if(horarioAtendimento.getDiaAtendimento().equals("Quarta")) {
								if(daoAtendimento.verificaDisponibilidade(horarioAtendimento) == true) {
									if((inicioSemana.get(Calendar.DAY_OF_WEEK) < 3 && fimSemana.get(Calendar.DAY_OF_WEEK) > 3) && hoje.get(Calendar.DAY_OF_WEEK) > 3) {
										grade[j] = grade[j].replace(",\"quarta\":\"X\"",",\"quarta\":\""+hora+"\"");
									}
								}
							}
								
							if(horarioAtendimento.getDiaAtendimento().equals("Quinta")) {
								if(daoAtendimento.verificaDisponibilidade(horarioAtendimento) == true) {
									if((inicioSemana.get(Calendar.DAY_OF_WEEK) < 4 && fimSemana.get(Calendar.DAY_OF_WEEK) > 4) && hoje.get(Calendar.DAY_OF_WEEK) > 4) {
										grade[j] = grade[j].replace(",\"quinta\":\"X\"",",\"quinta\":\""+hora+"\"");
									}
								}
							}
								
							if(horarioAtendimento.getDiaAtendimento().equals("Sexta")) {
								if(daoAtendimento.verificaDisponibilidade(horarioAtendimento) == true) {
									if((inicioSemana.get(Calendar.DAY_OF_WEEK) < 5 && fimSemana.get(Calendar.DAY_OF_WEEK) > 5) && hoje.get(Calendar.DAY_OF_WEEK) > 5) {
										grade[j] = grade[j].replace(",\"sexta\":\"X\"",",\"sexta\":\""+hora+"\"");
									}
								}
							}
								
							if(horarioAtendimento.getDiaAtendimento().equals("Sábado")) {
								if(daoAtendimento.verificaDisponibilidade(horarioAtendimento) == true) {
									if(inicioSemana.get(Calendar.DAY_OF_WEEK) < 6 && hoje.get(Calendar.DAY_OF_WEEK) > 6) {
										grade[j] = grade[j].replace(",\"sabado\":\"X\"",",\"sabado\":\""+hora+"\"");
									}
								}
							}
										
							//Adiciona o intervalo da consulta
							horaInicio.add(Calendar.MINUTE, horarioAtendimento.getDuracaoConsulta());
							horarioAtendimento.setHoraInicio(horaInicio);
						}	
					}
						 
					//cria a estrutura do objeto necessário para popular jqgrid
					String resposta = "{\"total\":\"1\",\"page\":\"1\",\"records\":\""+grade.length+"\",\"rows\":[";
					String dados = "";  
					
					for (int k = 0; k < grade.length; k++) {
						grade[k] = grade[k].replace("null","");
							dados += "{\"id\":\""+k+"\""+grade[k]+"";		
					}
						 
					resposta += dados;
						
					String fim = "]}";
						
					resposta += fim;
							
					resposta = resposta.replace(",]","]");
					
					out.print(resposta);
				}
			}	
		} else if(acao.equals("agendar")) {
		
			//Recupera os campos restantes
			String matricula = request.getParameter("matricula");
			String nomeMedico = request.getParameter("listaMedico");
			String semana = request.getParameter("listaSemana");
			String hora = request.getParameter("hora");
			int diaSemana = Integer.parseInt(request.getParameter("diaSemana"));
			
			//Separa as datas de inicio e fim da semana
			String semanas[] = semana.split(" a ");  
			String horas[] = hora.split(":"); 
			
			//Cria o objeto Calendar
			Calendar data = new GregorianCalendar();
			
			//Transforma para a String para Date
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			//Transforma a data para Calendar
			try {
				data.setTime((Date) formatter.parse(semanas[0]));
			} catch (Exception e) {
				System.out.println(e);
			}
			
			//Adiciona a data e a hora da consulta
			data.add(Calendar.DAY_OF_WEEK, diaSemana+1);
			data.add(Calendar.HOUR, Integer.parseInt(horas[0]));
			data.add(Calendar.MINUTE, Integer.parseInt(horas[1]));
			
			//Cria o objeto Cliente e define a matricula
			Cliente cliente = new Cliente();
			cliente.setMatricula(matricula);
			
			//Cria o objeto DAOCliente
			DAOCliente daoCliente = new DAOCliente();
			cliente = daoCliente.consultaMatricula(cliente);
		
			//Cria o objeto Dependente e define a matricula
			Dependente dependente = new Dependente();
			dependente.setMatricula(matricula);
				
			//Cria o objeto DAODependente
			DAODependente daoDependente = new DAODependente();
			dependente = daoDependente.consultaMatricula(dependente);
			
			//Cria o objeto Medico
			Medico medico = new Medico();
			medico.setNome(nomeMedico);
			
			//Cria o objeto DAOMedico
			DAOMedico daoMedico = new DAOMedico();
			medico = daoMedico.consultaMedicoNome(medico);
			
			//Cria o objeto Atendimento e define os campos
			Atendimento atendimento = new Atendimento();
			atendimento.setData(data);
			atendimento.setHora(data);
			
			//Cria o objeto HorarioAtendimento
			HorarioAtendimento horarioAtendimento = new HorarioAtendimento();
			horarioAtendimento.setHoraInicio(atendimento.getHora());
			horarioAtendimento.setMedico(medico);
			
			//Verifica para qual tipo de cliente foi realizado o atendimento
			if(cliente == null) {		
				atendimento.setDependente(dependente);
			} else {
				atendimento.setCliente(cliente);
			}
			
			atendimento.setMedico(medico);
			
			//Cria o objeto DAOAtendimento
			DAOAtendimento daoAtendimento = new DAOAtendimento();
			
			boolean duplicidade = daoAtendimento.verificaDuplicidade(atendimento);
			
			boolean disponibilidade = daoAtendimento.verificaDisponibilidade(horarioAtendimento);
			
			String resposta = null;
			
			if(disponibilidade) {
				if(duplicidade) {
					
					boolean flag = daoAtendimento.registraAgendamento(atendimento);

					if(flag) {
						resposta = "{\"mensagem\":\"Horario marcado com sucesso!!\"}";
						
						out.print(resposta);
					} else {
						resposta = "{\"mensagem\":\"Infelizmente não foi possível marcar o horário!\"}";
						
						out.print(resposta);
					}
				} else {
					resposta = "{\"mensagem\":\"O cliente já foi agendado para a especialidade!\"}";
					
					out.print(resposta);
				}
			} else {
				resposta = "{\"mensagem\":\"Horario já ocupado!\"}";
				
				out.print(resposta);
			}
		}
	}
	
	private String []alocaEspaco(HorarioAtendimento horarioAtendimento, String []grade) {
		//Variável que armazena a quantidade de atendimentos em um determinado intervado
		int qtdAtendimento = 0;

		//Cria o objeto Calendar para abrigar as horas de Inicio e Fim de um atendimento
		Calendar horaInicio = new GregorianCalendar();
		Calendar horaFim = new GregorianCalendar();
		 
		//Define os campos horaInicio e HoraFim
		horaInicio = horarioAtendimento.getHoraInicio();
		horaFim = horarioAtendimento.getHoraFim();
		
		//Define a quantidade de atendimentos para alocar o espaço necessário no array
		while(horaInicio.getTimeInMillis() <= horaFim.getTimeInMillis()) {
			horaInicio.add(Calendar.MINUTE, horarioAtendimento.getDuracaoConsulta());
			
			++qtdAtendimento;
		}
		
		//verifica se a quantidade de atendimento é maior do que o tamanho que o vetor possui
		if(grade.length < qtdAtendimento) {
			
			int i = grade.length;
			
			grade = Arrays.copyOf(grade, qtdAtendimento);
			
			while (i < grade.length) {
				grade[i] = ",\"segunda\":\"X\",\"terca\":\"X\",\"quarta\":\"X\",\"quinta\":\"X\",\"sexta\":\"X\",\"sabado\":\"X\"},";
				
				i++;
			}
			
		}
		
		//Reseta a horaInicio
		horaInicio.add(Calendar.MINUTE, -(qtdAtendimento*horarioAtendimento.getDuracaoConsulta()));
		
		return grade;
	}
}