$(document).ready(function() {
	
	var linha;
	var conteudo;
	var intervalo;
	
	/* ********************
	 Controle de validação
	********************* */
	
	//Valida o formulario do atendimento
	$("#atendimento").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {	
		
			//retorna os valores dos campos
	    	$matriculaMedico = $('input[name=matriculaMedico]').val();

			//retorna os dados a serem carregados no grid
	    	carregaGrid();
		
		},
    	
		//Define as regras a serem aplicadas nos campos
		rules: {
			matriculaMedico: {
				required  : true,
				number	  : true,
				minlength : 6
			}			
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matriculaMedico: {
				required: "Digite a matricula.",
				number: "Digite somente números.",
				minlength: "A matricula deve conter 6 dígitos."
			}
		}
	});

	//Valida o formulario do prontuario
	$("#prontuario").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {	
		
			$status = $('#listaStatus').find('option').filter(':selected').text();
			
			if($status.indexOf("Em Andamento") != -1) {
	    		
	        	$.ajax({
	    			type: "POST",
	    			url: "http://localhost:8080/CamimSCA/servlet/ControleAtendimento?acao=alteraStatus&status="+$status+"&idAtendimento="+linha,
	    			dataType: "json",
	    			cache: false,
	    			async: false,
	    			//Em caso de sucesso direciona a página ou exibe uma mensagem
	    			success: function(data, textStatus, XMLHttpRequest) {
	        			
	        			$('#listaStatus').find('option').filter(':selected').remove();		
	        			
	 	 				$("#listaStatus").append("<option value='2' selected='selected'>Finalizado</option>");
	 	 				
		 				if(data){
		 					
		 					$('#dialog1').html(data.mensagem);
		 	 				
		 	 				$('#dialog1').dialog('open');
		 				}
	    			},
	    			
	    			//Mostra um alerta em caso de erro
	    		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
	    				alert('Erro! 1 Status = ' + XMLHttpRequest.status,'ERRO');  
	    			} 
	    		});
	        	
	    	} else {
	    		
		    	$medico = $('input[name=matriculaMedico]').val();
	    		$prontuario = $('textarea[name=prontuario]').val();
	    		$receituario = $('textarea[name=receituario]').val();
	    		
	    		alert($medico);
	    		
	        	$.ajax({
	    			type: "POST",
	    			url: "http://localhost:8080/CamimSCA/servlet/ControleAtendimento?acao=finalizaAtendimento&matriculaMedico="+$medico+"&status="+$status+"&idAtendimento="+linha+"&prontuario="+$prontuario+"&receituario="+$receituario,
	    			dataType: "json",
	    			cache: false,
	    			async: false,
	    			//Em caso de sucesso direciona a página ou exibe uma mensagem
	    			success: function(data, textStatus, XMLHttpRequest) {
	        			
	        			$('#listaStatus').find('option').filter(':selected').remove();
	        			
		 				if(data){
		 					
		 					$('#dialog1').html(data.mensagem);
		 	 				
		 	 				$('#dialog1').dialog('open');
		 	 				
		 	 				carregaGrid();
		 				}
	
	    			},
	    			
	    			//Mostra um alerta em caso de erro
	    		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
	    				alert('Erro! 1 Status = ' + XMLHttpRequest.status,'ERRO');  
	    			} 
	    		});
	    	}
			
		},
    	
		//Define as regras a serem aplicadas nos campos
		rules: {
			prontuario: {
				required  : true
			}			
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			prontuario: {
				required: "Campo Obrigatorio!"
			}
		}
	});
	
	/* ********************
	 Controle das tabs
	********************* */
	
	$("#tabs").tabs( );
	
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
    
    $("input[value='listaAgenda']").live("click", function() {
    	
    	//se o formulario estiver válido
    	if($("#atendimento").valid()) {
	    	
    		//Carrega o grid
			$("#list").jqGrid({
				// Configuração da requisição
				datatype : 'json',
	
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
				colNames : ['Cliente', 'Horario', 'Status'],  
				
				// Configuração das colunas
		        colModel : [ 
		            { name: 'cliente', index: 'cliente', width: 100, align: 'center', sortable: false }, 
		            { name: 'horario', index: 'horario', width: 100, align: 'center', sortable: false },
		            { name: 'status' , index: 'status' , width: 100, align: 'center', sortable: false }
		        ],

		        //Acao ao clicar sobre o registro
		        onCellSelect : function(rowid, iCol, cellcontent) {

					$('#listaStatus').append('<option value="1" selected="selected">Em Andamento</option>');
					
					$('input[value=avancaConsulta]').removeAttr('disabled');
				    $('#listaStatus').removeAttr('disabled');
					$('textarea[name=prontuario]').removeAttr('disabled');
					$('textarea[name=receituario]').removeAttr('disabled');
					
					linha = rowid;
				},

				//Acao ao completar o grid
				loadComplete: function(){
					$('#pager').find('#first, #prev, #selbox, #sp_1, #sp_2, #next, #last').hide();

				},
	        
				//Configuração da exibição
				pager		: $('#pager'),
				altRows		: true, 
				caption		: 'Horario de Atendimento',
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
    	
    	clearInterval(intervalo);
    	
    	$('input[name=matriculaMedico]').removeAttr('disabled');
    	
    	$('#listaMedico').removeAttr('disabled');
    	$('textarea[name=prontuario]').attr('disabled','disabled');
    	$('textarea[name=receituario]').attr('disabled','disabled');
 	
    });
	
    $("input[value='avancaConsulta']").live("click", function() {
    	
    	$status = $('#listaStatus').find('option').filter(':selected').text();
		
		if($status.indexOf("Em Andamento") != -1) {

    		$('textarea[name=prontuario]').addClass("ignore");
		} else {
			
    		$('textarea[name=prontuario]').removeClass("ignore");
		}
    	
    });
	
	/* ********************
	 Funções
	********************* */
	
	//preeche o combobox
	function carregaCombo() {
		
		$status = $('#listaStatus').find('option').filter(':selected').text();
		
    	$.ajax({
			type: "POST",
			url: "http://localhost:8080/CamimSCA/servlet/ControleAtendimento?acao=listaStatus&status="+$status,
			dataType: "text/html",
			cache: false,
			async: false,
			//Em caso de sucesso direciona a página ou exibe uma mensagem
			success: function(data, textStatus, XMLHttpRequest) {
    			
    			$('#listaStatus').find('option').remove();
    		
				//Caso algo seja retornado, adiciona na combobox
				if(data){
					$("#listaStatus").html(data);	
				}
			},
			
			//Mostra um alerta em caso de erro
		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
				alert('Erro! 1 Status = ' + XMLHttpRequest.status,'ERRO');  
			} 
		});
    	
	};
    
	function carregaGrid() {
		
		//retorna os dados a serem carregados no grid
		$.ajax({
			type: "POST",
			url: 'http://localhost:8080/CamimSCA/servlet/ControleAtendimento?acao=listaAgenda&matriculaMedico='+$matriculaMedico,
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
			    	
		    		$('input[name=matriculaMedico]').removeAttr('disabled');
			    	
		    		$('#listaMedico').removeAttr('disabled');
		    		$('textarea[name=prontuario]').attr('disabled','disabled');
		    		$('textarea[name=receituario]').attr('disabled','disabled');
				} else {

					$("input[name='matriculaMedico']").attr('disabled','disabled');
					$('input[value=listaAgenda]').attr('disabled','disabled');
					
					//retorna a referencia do grid e adiciona o resultado
					var grid = $("#list")[0];
					var result = eval("("+data+")");
					grid.addJSONData(result);
					
					grid = $("#list");
					grid.trigger("reloadGrid");
					
					result = null;
					data =null;
				}
			}
		});
		
		// Atualiza o grid em intervalos de 5 minutos
		if(intervalo == null) {	
			intervalo = self.setInterval(carregaGrid, 300000);	
		}
	}
	
	//Desabilita elementos do formulario prontuario
    $('input[value=avancaConsulta]').attr('disabled','disabled');
    $("#listaStatus").attr('disabled','disabled');
	$('textarea[name=prontuario]').attr('disabled','disabled');
	$('textarea[name=receituario]').attr('disabled','disabled');
});