$(document).ready(function() {
	
	var coluna;
	var conteudo;
	var intervalo;

	/* ********************
	 Controle de validação
	********************* */
	
	//Valida o formulario do atendimento
	$("#clienteEspecialidade").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {	

			$especialidade = $('#listaEspecialidade').find('option').filter(':selected').text();
			$dataInicio = $('input[name=dataInicio]').val();
			$dataFim = $('input[name=dataFim]').val();
		
        	$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/CamimSCA/servlet/ControleRelatorio?acao=clienteEspecialidade&especialidade="+$especialidade+"&dataInicio="+$dataInicio+"&dataFim="+$dataFim,
				dataType : "text/html",
				cache	 : false,
				async	 : false,
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
	 				} else {
	
	 					//retorna a referencia do grid e adiciona o resultado
	 					var grid = $("#list")[0];
	 					var result = eval("("+data+")");
	 					grid.addJSONData(result);
	 					
	 					result = null;
	 					data =null;
	 				}
    			},
    			
    			//Mostra um alerta em caso de erro
    		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
    				alert('Erro! Status = ' + XMLHttpRequest.status,'ERRO');  
    			} 
    		});
        	
        	
		
		},
    	
		//Define as regras a serem aplicadas nos campos
		rules: {
			listaEspecialidade: {
				required  : true
			},

			dataInicio: {
				required  : true
			},
			
			dataFim: {
				required  : true
			}			
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			listaEspecialidade: {
				required: "Escolha a especialidade."
			},
			
			dataInicio: {
				required  : "Digite uma data de inicio"
			},
			
			dataFim: {
				required  : "Digite uma data de fim"
			}
		}
	});
	
	/* ********************
	 Controle dos diálogos
	********************* */	
	
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

	$("input[value='Exibir']").live('click', function(event){
	    	
	    	//se o formulario estiver válido
	    	if($("#clienteEspecialidade").valid()) {
		    	
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
					colNames : ['Especialidade', 'Idade Media'],  
					
					// Configuração das colunas
			        colModel : [ 
			            { name: 'especialidade', index: 'especialidade', width:50, align: 'center', sortable: false }, 
			            { name: 'mediaIdade'  , index: 'mediaIdade'  , width: 50, align: 'center', sortable: false }
			        ],

		        //Acao ao completar o grid
				loadComplete: function(){
					$('#pager').find('#first, #prev, #selbox, #sp_1, #sp_2, #next, #last').hide();
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

	/* ********************
	 Controle dos combobox
	********************* */

   //preeche o combobox referente a especialidade
	$("#listaEspecialidade").load('../../relatorioIdadeClientePorEspecialidade.html', function() {
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
});