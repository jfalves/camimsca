$(document).ready(function() {
	
	var coluna;
	var conteudo;
	var intervalo;

	/* ********************
	 Controle de validação
	********************* */
	
	//Valida o formulario do atendimento
	$("#atendimentoMedico").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {	
		
			$medico = $('input[name=matriculaMedico]').val();
			$dataInicio = $('input[name=dataInicio]').val();
			$dataFim = $('input[name=dataFim]').val();
			
        	$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/CamimSCA/servlet/ControleRelatorio?acao=atendimentoMedico&matriculaMedico="+$medico+"&dataInicio="+$dataInicio+"&dataFim="+$dataFim,
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
			matriculaMedico: {
				required  : true,
				number    : true,
				minlength : 6
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
			matriculaMedico: {
				required: "Escolha a especialidade.",
				number  : "Digite apenas numeros.",
				minlength : "A matricula deve conter 6 dígitos."
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
	    	if($("#atendimentoMedico").valid()) {
		    	
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
					colNames : ['Medico', 'Quantidade de Atendimentos'],  
					
					// Configuração das colunas
			        colModel : [ 
			            { name: 'medico', index: 'medico', width:50, align: 'center', sortable: false }, 
			            { name: 'atendimento'  , index: 'atendimento'  , width: 50, align: 'center', sortable: false }
			        ],

		        //Acao ao completar o grid
				loadComplete: function(){
					$('#pager').find('#first, #prev, #selbox, #sp_1, #sp_2, #next, #last').hide();
				},
		        
				// Configuração da exibição
		        pager		: $('#pager'),
		        altRows		: true, 
		        caption		: 'medico por atendimento',
		        height		: "100%",
		        hidegrid	: false,
		        rowNum		: 50,
		        sortname	: 'id',    
		        sortorder	: 'asc',
		        viewrecords : true
		      }).jqGrid('navGrid', '#pager', { edit: false, add: false, del: false });
	    	}
	});
});