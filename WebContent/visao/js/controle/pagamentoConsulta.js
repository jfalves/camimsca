$(document).ready(function() {

	var conteudo;
	
	/* ********************
	 Controle de validação
	********************* */
	
	//valida o formulario planoSaude
	$("#pagamento").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {
		
			//Pega os atributos do formulário e coloca em forma de URL
			$cliente = $('input[name=matriculaCliente]').val();
			$funcionario = $('input[name=matriculaFuncionario]').val();
			$formaPagamento = $('#listaFormaPagamento').find('option').filter(':selected').text();
			
			//Faz a requisição via AJAX
			$.ajax({
				type: "POST",
		   		url: "http://localhost:8080/CamimSCA/servlet/ControlePagamentoConsulta?acao=listaPagamentoConsulta&matriculaCliente="+$cliente+"&matriculaFuncionario="+$funcionario+"&listaFormaPagamento="+$formaPagamento,
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
				    	
				    	$('input[name=matriculaCliente]').removeAttr('disabled');
				    	$('input[name=matriculaFuncionario]').removeAttr('disabled');
				    	$('#listaFormaPagamento').removeAttr('disabled');
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
			matriculaCliente			: { required: true, number: true },
			matriculaFuncionario		: { required: true, number: true },
			listaFormaPagamento         : { required: true }
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matriculaCliente: {
				required : "Digite a matricula.",
				number   : "Digite um numero valido"
			},
			
			matriculaFuncionario: {
				required : "Digite a matricula.",
				number   : "Digite um numero valido"
			},
			
			listaFormaPagamento: {
				required : "Selecione uma forma de pagamento."
			}
		}
	});
	
	/* ********************
	 Controle dos diálogos
	********************* */
	
	$('#search-dialog').dialog({ 
	   autoOpen: false,
       buttons: {
			"Cancelar": function() {$(this).dialog('close');}
         },
       draggable: false,
       modal: true,

       open: function(event, ui) {

      	 $grid = $("#search-list").jqGrid({
      		 
      			// Configuração da requisição
      			url:'http://localhost:8080/CamimSCA/servlet/ControleCliente?acao=ConsultarTodos',
      			datatype: 'json',
      			mtype: 'POST',
      			   		        
      			// Configuração do leitor de JSON
      			jsonReader: {   
      				root: "rows", 
      				page: "page", 
      				total: "total", 
      				records: "records", 
      				cell: "cell", 
      				repeatitems: false
      			},
      					
      			// Configuração do nome das colunas
      			colNames: ['Matricula', 'Nome', 'Cpf'],    
      				
      			// Configuração das colunas
      		    colModel: [ 
      		           { name: 'matricula', index: 'matricula', align:'center', sortable:false }, 
      		           { name: 'nome', index: 'nome', align:'center', sortable:false },
      		           { name: 'cpf' , index: 'cpf' , align:'center', sortable:false }
      		    ],
      			
      		    // Configurações do clique duplo
      		    ondblClickRow: function(rowid) {
      				
      				$matricula = $("#search-list").getCell(rowid,0);
					
					$("input[name='matriculaCliente']").val($matricula);
					
					$("#search-list").GridUnload();
					
					//Fecha o campo de pesquisa
					$('#search-dialog').dialog('close');
      			},
      		    
      		    // Configuração da exibição
      			pager: $("#search-pager"),
      			altRows		: true, 
      			height		: "100%",
      			hidegrid	: false,
      		    rowNum: 10,
      		    sortname: 'idCliente', 
      		    sortorder: 'desc', 
      			viewrecords: true
      	 	}).jqGrid('navGrid', '#search-pager', { edit: false, add: false, del: false });

       },
       resizable: false,
       width: 760
	});	
	
	$('#dialog').dialog({ 
		autoOpen: false,
        buttons: {
			"Nao": function() {
				$(this).dialog('close');
			},
			
			"Sim": function() {
				
				//Pega os atributos do formulário e coloca em forma de URL
		    	$matriculaF = $('input[name=matriculaFuncionario]').val();
		    	$formaPagamento = $('#listaFormaPagamento').find('option').filter(':selected').text();

		    	$id = conteudo;
	
				$.ajax({
		 			type: "POST",
		 			url: "http://localhost:8080/CamimSCA/servlet/ControlePagamentoConsulta?acao=pagar&matriculaFuncionario="+$matriculaF+"&formaPagamento="+$formaPagamento+"&idPagamento="+$id,
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

    $("input[value='Consultar']").live("click", function() {
    	
    	//se o formulario estiver válido
    	if($("#pagamento").valid()) {
	    	
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
      			colNames: ['Cliente', 'Medico', 'Especialidade', 'Valor (R$)'],    
      				
      			// Configuração das colunas
      		    colModel: [ 
      		           {name: 'cliente'	      ,index: 'cliente'	      ,align:'center', sortable:false },
      		           {name: 'medico' 		  ,index: 'medico'	      ,align:'center', sortable:false },
      		           {name: 'especialidade' ,index: 'especialidade' ,align:'center', sortable:false },
      		           {name: 'valor' 		  ,index: 'valor' 	      ,align:'center', sortable:false }
      		           
      		    ],
      			
		        //Acao ao clicar sobre o registro
		        onCellSelect : function(rowid, iCol, cellcontent) {
					
					$matricula = $('input[name=matriculaCliente]').val();
						
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
				 				$("#dialog").html("Deseja realizar o pagamento do "+data.nome+" ?");
				 			}
				 		},
				 			
				 		//Mostra um alerta em caso de erro
				 		error: function(XMLHttpRequest, textStatus, errorThrown) {  
				 			alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
				 		} 
					});  	

					conteudo = rowid;
						
					$("#dialog").dialog("open");
		
				},
      			
      			//Acao ao completar o grid
    			loadComplete: function(){
    				$('#pager').find('#first, #prev, #selbox, #sp_1, #sp_2, #next, #last').hide();
    				
    		    	$('input[name=matriculaCliente]').attr('disabled','disabled');
    		    	$('input[name=matriculaFuncionario]').attr('disabled','disabled');
    		    	$('#listaFormaPagamento').attr('disabled','disabled');
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
    	
    	$('input[name=matriculaCliente]').removeAttr('disabled');
    	$('input[name=matriculaFuncionario]').removeAttr('disabled');
    	$('#listaFormaPagamento').removeAttr('disabled');
 	
    });    
    
	// Dialog Link
	$("#dialog_link").live("click", function(){
		$('#search-dialog').dialog('open');
	});

	
	//hover states on the static widgets
	$("#dialog_link").hover(
		function() { $(this).addClass('ui-state-hover'); }, 
		function() { $(this).removeClass('ui-state-hover'); }
	);
	
	/* ********************
	 Controle dos combobox's
	********************* */
	
	//Lista a forma de pagamento
	$("#listaFormaPagamento").load('../../pagamento.html', function() {
        $.ajax({
    		type: "POST",
    		url: "http://localhost:8080/CamimSCA/servlet/ControlePagamentoConsulta?acao=listaFormaPagamento",
    		dataType: "text/html",
    		cache: false,
    		async: false,
    		
    		//Em caso de sucesso direciona a página ou exibe uma mensagem
    		success: function(data, textStatus, XMLHttpRequest) {
    			
    			//Caso algo seja retornado, adiciona na combobox
    			if(data){
    				$("#listaFormaPagamento").html(data);	
    			}
    		},
    		
    		//Mostra um alerta em caso de erro
    	    error: function(XMLHttpRequest, textStatus, errorThrown) {  
    			alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
    		} 
    	});
	});	
});