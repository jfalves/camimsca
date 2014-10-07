$(document).ready(function() {
	
	var coluna;
	var conteudo;
	var intervalo;
	
	/* ********************
	 Controle de valida��o
	********************* */
	
	//Valida o formulario do atendimento
	$("#especialidadeAtendimento").validate({
   	
		//Classe que ignora os elementos
   	ignore: ".ignore",
   	
   	//Executa quando o formul�rio estiver v�lido
		submitHandler: function(form) {	
		
			$dataInicio = $('input[name=dataInicio]').val();
			$dataFim = $('input[name=dataFim]').val();
			
			$.ajax({
				type: "POST",
				url: "http://localhost:8080/CamimSCA/servlet/ControleRelatorio?acao=especialidadeAtendimento&dataInicio="+$dataInicio+"&dataFim="+$dataFim,
				dataType : "text/html",
				cache	 : false,
				async	 : false,
				//Em caso de sucesso direciona a p�gina ou exibe uma mensagem
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
	 					data = null;
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
			dataInicio: {
				required  : true
			},
			
			dataFim: {
				required  : true
			}
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			dataInicio: {
				required  : "Digite uma data de inicio"
			},
			
			dataFim: {
				required  : "Digite uma data de fim"
			}
		
		}
	});
	
	/* ********************
	 Controle dos di�logos
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
	 Controle dos Bot�es
	********************* */

	$("input[value='Listar']").live('click', function(event){

    	//se o formulario estiver v�lido
    	if($("#especialidadeAtendimento").valid()) {		    	
		    	//Carrega o grid
				 $("#list").jqGrid({
					// Configura��o da requisi��o
					datatype : 'local',
		
					// Configura��o do leitor de JSON
					jsonReader : { 
						root		: "rows", 
						page		: "page", 
						total		: "total", 
						records		: "records", 
						repeatitems : false, 
						id			: "id",
						cell		: ""
					},
					
					// Configura��o do nome das colunas
					colNames : ['Especialidade', 'Quantidade Atendimento'],  
					
					// Configura��o das colunas
			        colModel : [ 
			            { name: 'especialidade', index: 'especialidade', width:50, align: 'center', sortable: false }, 
			            { name: 'qtdAtendimento'  , index: 'qtdAtendimento'  , width: 50, align: 'center', sortable: false }
			        ],

		        //Acao ao completar o grid
				loadComplete: function(){
					$('#pager').find('#first, #prev, #selbox, #sp_1, #sp_2, #next, #last').hide();
				},
		        
				// Configura��o da exibi��o
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
});