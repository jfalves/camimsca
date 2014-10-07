$(document).ready(function() {
	
	/* ********************
	 Controle de validação
	********************* */
	
	//Adiciona o método de comparação entre idades
	jQuery.validator.addMethod("idadeMinima", function(value, element, params) {
		return this.optional(element) || value < $("input[name='idadeMaxima']").val();
		}, "A idade minima deve ser menor que a maxima.");
	
	jQuery.validator.addMethod("idadeMaxima", function(value, element, params) {
		return this.optional(element) || value > $("input[name='idadeMinima']").val();
		}, "A idade maxima deve ser maior que a minima.");
	
	//valida o formulario planoSaude
	$("#planoSaude").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {
		
			//Pega os atributos do formulário e coloca em forma de URL
			$dados = $("#planoSaude").serialize();
				
			//Faz a requisição via AJAX
			$.ajax({
				type: "POST",
		   		url: "http://localhost:8080/CamimSCA/servlet/ControlePlanoSaude",
		   		data: $dados,
		   		dataType: "json",
		   		cache: false,
		   		async: false,
		   		
		   		//Em caso de sucesso direciona a página ou exibe uma mensagem
		   		success: function(data, textStatus, XMLHttpRequest) {
						$('#dialog').html(data.mensagem);
						$('#dialog').dialog('open');
				},
				
				//Mostra um alerta em caso de erro
				error: function(XMLHttpRequest, textStatus, errorThrown) {  
					jAlert('Erro!  Status = ' + errorThrown,'ERRO');  
				} 
			});
		},
    	
		//Define as regras a serem aplicadas nos campos
		rules: {
			idadeMinima			: { required: true, number: true, range: [0, 120], idadeMinima: true },
			idadeMaxima			: { required: true, number: true, range: [0, 120], idadeMaxima: true },
			valorFaixa			: { required: true, number: true },
			listaTipoPlanoSaude : { required: true }
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			idadeMinima: {
				required : "Digite uma idade mínima.",
				number   : "Digite um número válido",
				range    : "Digite uma idade entre 0 e 120"
			},
			idadeMaxima: {
				required : "Digite uma idade máxima.",
				number   : "Digite um número válido",
				range    : "Digite uma idade entre 0 e 120"
			},
			valorFaixa: {
				required : "Digite o valor da faixa etária.",
				number   : "Digite um número válido"
			},
			
			listaTipoPlanoSaude: {
				required : "Selecione um tipo de plano de saude"
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
      			url:'http://localhost:8080/CamimSCA/servlet/ControlePlanoSaude?acao=Consultar',
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
      			colNames: ['Idade Minima', 'Idade Maxima', 'Valor Faixa', 'Tipo Plano'],    
      				
      			// Configuração das colunas
      		    colModel: [ 
      		           { name: 'idadeMinima', index: 'idadeMinima', align:'center', sortable:false }, 
      		           { name: 'idadeMaxima', index: 'idadeMaxima', align:'center', sortable:false },
      		           { name: 'valorFaixa' , index: 'valorFaixa' , align:'center', sortable:false },
      		           { name: 'tipo'       , index: 'tipo'       , align:'center', sortable:false }
      		    ],
      			
      		    // Configurações do clique duplo
      		    ondblClickRow: function(rowid) {
      				
      				$idadeMinima = $("#search-list").getCell(rowid,0);
      				$idadeMaxima = $("#search-list").getCell(rowid,1);
      				$valorFaixa  = $("#search-list").getCell(rowid,2);
      				$tipoPlano   = $("#search-list").getCell(rowid,3);
      				
					$("input[name='idadeMinima']").val($idadeMinima);
					$("input[name='idadeMaxima']").val($idadeMaxima);
					$("input[name='valorFaixa']").val($valorFaixa);
					
					//$("input[name='idadeMinima']").val($idadeMinima);
					
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
      		    sortname: 'idPlanoSaude', 
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

	//Define o comportamento da tela ao Cadastrar
	$("input[value='Cadastrar']").live("click", function(event){
		
		//Remove a classe que ignora os elementos
		$("input[name='idadeMinima']").removeClass("ignore");
		$("input[name='idadeMaxima']").removeClass("ignore");
		$("input[name='valorFaixa']").removeClass("ignore");
		$("#listaTipoPlanoSaude").removeClass("ignore");
	});
	
	//Define o comportamento da tela ao Consultar
	$("input[value='Consultar']").live("click", function(event){
		
		//Adiciona a classe que ignora os elementos
		$("#listaTipoPlanoSaude").removeClass("ignore");
	   	$("input[name='idadeMinima']").removeClass("ignore");
	   	$("input[name='idadeMaxima']").removeClass("ignore");
		$("input[name='valorFaixa']").addClass("ignore");
		
		//Abre o campo de pesquisa
		$('#search-dialog').dialog('open');
	});
	
	//Define o comportamento da tela ao Alterar
	$("input[value='Alterar']").live("click", function(event){
		
		//Remove a classe que ignora os elementos
		$("input[name='idadeMinima']").removeClass("ignore");
		$("input[name='idadeMaxima']").removeClass("ignore");
		$("input[name='valorFaixa']").removeClass("ignore");
		$("#listaTipoPlanoSaude").removeClass("ignore");
	});
	
	//Define o comportamento da tela ao Excluir
	$("input[value='Excluir']").live("click", function(event){
		
		//Adiciona a classe que ignora os elementos
		$("input[name='valorFaixa']").addClass("ignore");
	});
	
	/* ********************
	 Controle dos combobox's
	********************* */
	
	//Lista o tipo de plano de saude
	$("#listaTipoPlanoSaude").load('../../planosaude.html', function() {
        $.ajax({
    		type: "POST",
    		url: "http://localhost:8080/CamimSCA/servlet/ControlePlanoSaude?acao=listaTipoPlanoSaude",
    		dataType: "text/html",
    		cache: false,
    		async: false,
    		
    		//Em caso de sucesso direciona a página ou exibe uma mensagem
    		success: function(data, textStatus, XMLHttpRequest) {
    			
    			//Caso algo seja retornado, adiciona na combobox
    			if(data){
    				$("#listaTipoPlanoSaude").html(data);	
    			}
    		},
    		
    		//Mostra um alerta em caso de erro
    	    error: function(XMLHttpRequest, textStatus, errorThrown) {  
    			alert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
    		} 
    	});
	});	
});