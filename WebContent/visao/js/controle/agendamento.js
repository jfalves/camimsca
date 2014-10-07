$(document).ready(function() {
	
	var coluna;
	var conteudo;
	
	/* ********************
	 Controle de validação
	********************* */
	
	//Valida o formulario do agendamento
	$("#agendamento").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {	
		
			//retorna os valores dos campos
	    	$matricula = $('input[name=matricula]').val();
	    	$medico = $('#listaMedico').find('option').filter(':selected').text();
	    	$semana = $('#listaSemana').find('option').filter(':selected').text();

			//retorna os dados a serem carregados no grid
	    	$.ajax({
	 			type: "POST",
	 			url: 'http://localhost:8080/CamimSCA/servlet/ControleHorarioAtendimento?acao=listaHorario&listaMedico='+$medico+'&listaSemana='+$semana+'&matricula='+$matricula,
	 			dataType: "html/text",
	 			cache: false,
	 			async: false,
	 			
	 			//Em caso de sucesso direciona a página ou exibe uma mensagem
	 			success: function(data, textStatus, XMLHttpRequest) {
	    		
	 				//Caso algo seja retornado, adiciona no textfield
	 				if(data.indexOf("mensagem") != -1) {
	 					
	 					//Transforma a string em ojeto JSON
	 					var obj = $.parseJSON(data);
	 					
	 					//Adiciona a mensagem no dialogo e exibe
						$('#dialog1').html(obj.mensagem);
						$('#dialog1').dialog('open');
						
						//retorna os objetos ao estado inicial
				    	$("#list").GridUnload();
				    	
				    	$('input[name=matricula]').removeAttr('disabled');
				    	$('#listaMedico').removeAttr('disabled');
				    	$('#listaEspecialidade').removeAttr('disabled');
				    	$('#listaSemana').removeAttr('disabled');
	 				} else {
	
	 					//retorna a referencia do grid e adiciona o resultado
	 					var grid = $("#list")[0];
	 					var result = eval("("+data+")");
	 					grid.addJSONData(result);
	 					
	 					result = null;
	 					data =null;
	 				}
	 			}
	 		});
		
		},
    	
		//Define as regras a serem aplicadas nos campos
		rules: {
			matricula: {
				required  : true,
				number	  : true,
				minlength : 6
			},
			
			listaEspecialidade: {
				required  : true
			},
			
			listaMedico: {
				required  : true
			}			
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matricula: {
				required: "Digite a matricula.",
				number: "Digite somente números.",
				minlength: "A matricula deve conter 6 dígitos."
			},
			
			listaEspecialidade: {
				required  : "Selecione uma especialidade"
			},
			
			listaMedico: {
				required  : "Selecione um Médico"
			}		
		}
	});
	
	/* ********************
	 Controle dos diálogos
	********************* */	
	
	$('#dialog').dialog({ 
		autoOpen: false,
        buttons: {
			"Nao": function() {
				$(this).dialog('close');
			},
			
			"Sim": function() {
				
				//Pega os atributos do formulário e coloca em forma de URL
		    	$matricula = $('input[name=matricula]').val();
		    	$medico = $('#listaMedico').find('option').filter(':selected').text();
		    	$semana = $('#listaSemana').find('option').filter(':selected').text();
		    	$hora = conteudo;
		    	$coluna = coluna;
	
				$.ajax({
		 			type: "POST",
		 			url: "http://localhost:8080/CamimSCA/servlet/ControleHorarioAtendimento?acao=agendar&listaMedico="+$medico+"&listaSemana="+$semana+"&matricula="+$matricula+"&hora="+$hora+"&diaSemana="+$coluna,
		 			dataType: "json",
		 			cache: false,
		 			async: false,
		 			//Em caso de sucesso direciona a página ou exibe uma mensagem
		 			success: function(data, textStatus, XMLHttpRequest) {
		 				
		 				if(data){
		 					
		 					$('#dialog1').html(data.mensagem);

		 				}
		 			},
		 			
		 			//Mostra um alerta em caso de erro
		 		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
		 				alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
		 			} 
		 		});
				
				$('#dialog').dialog('close');
 				
 				$('#dialog1').dialog('open');
			}
        },
        draggable: false,
        modal: true,
        resizable: false
	});		
	
	$('#dialog1').dialog({ 
		autoOpen: false,
       buttons: {
			"Ok": function() {
				$(this).dialog('close');
			}
       },
       draggable: false,
       modal: true,
       resizable: false
	});	
	
	/* ********************
	 Controle dos Botões
	********************* */
    
    $("input[value='listaHorario']").live("click", function() {
    	
    	//se o formulario estiver válido
    	if($("#agendamento").valid()) {
	    	
	    	//Carrega o grid
			 $("#list").jqGrid({
				// Configuração da requisição
				datatype : 'local',
	
				// Configuração do leitor de JSON
				jsonReader : { 
					root		: "rows", 
					page		: "page", 
					total		: "total", 
					records		: "records", 
					repeatitems : false, 
					id			: "id",
					cell		: ""
				},
				
				// Configuração do nome das colunas
				colNames : ['Segunda', 'Terca', 'Quarta', 'Quinta', 'Sexta', 'Sabado'],  
				
				// Configuração das colunas
		        colModel : [ 
		            { name: 'segunda', index: 'segunda', width: 100, align: 'center', sortable: false }, 
		            { name: 'terca'  , index: 'terca'  , width: 100, align: 'center', sortable: false },
		            { name: 'quarta' , index: 'quarta' , width: 100, align: 'center', sortable: false },
		            { name: 'quinta' , index: 'quinta' , width: 100, align: 'center', sortable: false },
		            { name: 'sexta'  , index: 'sexta'  , width: 100, align: 'center', sortable: false },
		            { name: 'sabado' , index: 'sabado' , width: 100, align: 'center', sortable: false }
		        ],
					
		        //Acao ao clicar sobre o registro
		        onCellSelect : function(rowid, iCol, cellcontent) {
					
					if(cellcontent.indexOf('X') != -1) {
						$("#dialog").html("Horario nao disponivel!");
						
						$("#dialog").dialog("open");
						
					} else {
						
						$matricula = $('input[name=matricula]').val();
						
						$.ajax({
				 			type: "POST",
				 			url: "http://localhost:8080/CamimSCA/servlet/ControleCliente?acao=Consultar&matricula="+$matricula,
				 			dataType: "json",
				 			cache: false,
				 			async: false,
				 			//Em caso de sucesso direciona a página ou exibe uma mensagem
				 			success: function(data, textStatus, XMLHttpRequest) {
				 				
				 				//Caso algo seja retornado, adiciona na combobox
				 				if(data){
				 					$("#dialog").html("Deseja realmente agendar o horario do "+data.nome+" no horario de "+cellcontent+" horas ?");
				 				}
				 			},
				 			
				 			//Mostra um alerta em caso de erro
				 		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
				 				alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
				 			} 
				 		});  	

						coluna = iCol;
						conteudo = cellcontent;
						
						$("#dialog").dialog("open");
					}
				
			},

	        //Acao ao completar o grid
			loadComplete: function(){
				$('#pager').find('#first, #prev, #selbox, #sp_1, #sp_2, #next, #last').hide();
				
		    	$('input[name=matricula]').attr('disabled','disabled');
		    	$('#listaMedico').attr('disabled','disabled');
		    	$('#listaEspecialidade').attr('disabled','disabled');
		    	$('#listaSemana').attr('disabled','disabled');
			},
	        
			// Configuração da exibição
	        pager		: $('#pager'),
	        altRows		: true, 
	        caption		: 'Grade de Horario',
	        height		: "100%",
	        hidegrid	: false,
	        rowNum		: 50,
	        sortname	: 'id',    
	        sortorder	: 'asc',
	        viewrecords : true
	      }).jqGrid('navGrid', '#pager', { edit: false, add: false, del: false });
    	}
    });
    
    
    $("input[value='Limpar']").live("click", function() {
    	
    	$("#list").GridUnload();
    	
    	$('input[name=matricula]').removeAttr('disabled');
    	$('#listaMedico').removeAttr('disabled');
    	$('#listaEspecialidade').removeAttr('disabled');
    	$('#listaSemana').removeAttr('disabled');
 	
    });
    	
	/* ********************
	 Controle dos combobox
	********************* */

    //preeche o combobox referente a especialidade
	$("#listaEspecialidade").load('../../agendamento.html', function() {
     	$.ajax({
 			type: "POST",
 			url: "http://localhost:8080/CamimSCA/servlet/ControleEspecialidade?acao=listaEspecialidade",
 			dataType: "text/html",
 			cache: false,
 			async: false,
 			//Em caso de sucesso direciona a página ou exibe uma mensagem
 			success: function(data, textStatus, XMLHttpRequest) {
 				
 				//Caso algo seja retornado, adiciona na combobox
 				if(data){
 					$("#listaEspecialidade").html(data);	
 				}
 			},
 			
 			//Mostra um alerta em caso de erro
 		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
 				alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
 			} 
 		});  	
	});
	
	//preeche o combobox referente ao médico
	$("#listaEspecialidade").live("focus", function(){
		
		$especialidade = $('#listaEspecialidade').find('option').filter(':selected').text();
        
        //Caso o indice seja 0 procura todos os registros
        if($especialidade != "Buscar") {
        	
        	$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/CamimSCA/servlet/ControleMedico",
    			dataType: "text/html",
    			data: {acao:"listaMedico", especialidade: $especialidade},
    			cache: false,
    			async: false,
    			//Em caso de sucesso direciona a página ou exibe uma mensagem
    			success: function(data, textStatus, XMLHttpRequest) {
    				
    				//Caso algo seja retornado, adiciona na combobox
    				if(data){
    					$("#listaMedico").html(data);	
    				} else {
    					$("#listaMedico").html(" ");
    				}
    			},
    			//Mostra um alerta em caso de erro
    		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
    				alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
    			} 
    		});  	
        } 
    });
	
	$("#listaSemana").load('../../agendamento.html', function() {
		
     	$.ajax({
 			type: "POST",
 			url: "http://localhost:8080/CamimSCA/servlet/ControleHorarioAtendimento?acao=listaSemana",
 			dataType: "text/html",
 			cache: false,
 			async: false,
 			//Em caso de sucesso direciona a página ou exibe uma mensagem
 			success: function(data, textStatus, XMLHttpRequest) {
 				
 				//Caso algo seja retornado, adiciona na combobox
 				if(data){
 					$("#listaSemana").html(data);	
 				}
 			},
 			
 			//Mostra um alerta em caso de erro
 		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
 				alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
 			} 
 		});  	
	});

});