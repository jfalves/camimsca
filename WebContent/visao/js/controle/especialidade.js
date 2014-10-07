$(document).ready(function() {

	/* ********************
	 Controle de validação
	********************* */
	
	//valida o formulario especialidade
	$("#especialidade").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {
		
			//Pega os atributos do formulário e coloca em forma de URL
			$dados = $("#especialidade").serialize();
				
			//Faz a requisição via AJAX
			$.ajax({
				type	 : "POST",
		   		url		 : "http://localhost:8080/CamimSCA/servlet/ControleEspecialidade",
		   		data	 : $dados,
		   		dataType : "json",
		   		cache	 : false,
		   		async	 : false,
		   		
		   		//Em caso de sucesso direciona a página ou exibe uma mensagem
		   		success	 : function(data, textStatus, XMLHttpRequest) {
				
					$('#dialog').html(data.mensagem);
					
					$('#dialog').dialog('open');
				}
			});
		},
    	
		//Define as regras a serem aplicadas nos campos
		rules: {
			nome	  : { required: true, maxlength: 45 },
			descricao : { maxlength: 1024 },
			valor	  : { required: true, number:true },
			dataInicio: { required: true },
			dataFim	  : { required: true }
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			nome	  : {
				required  : "Digite o nome da especialidade.",
				maxlength : "O campo deve conter até 45 caracteres."
			},
			descricao : {
				maxlength : "O campo deve conter até 1024 caracteres."
			},
			valor: {
				required  : "Digite um valor.",
				number    : "Digite apenas numeros"
			},
			dataInicio: {
				required  : "Digite uma data de inicio."
			},
			dataFim: {
				required  : "Digite uma data de fim."
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
       			url:'http://localhost:8080/CamimSCA/servlet/ControleEspecialidade?acao=Consultar',
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
       			colNames: ['Nome', 'Descricao'],    
       				
       			// Configuração das colunas
       		    colModel: [ 
       		           { name: 'nome'	  , index: 'nome'	  , align:'center', sortable:false }, 
       		           { name: 'descricao', index: 'descricao', align:'center', sortable:false }
       		    ],
       			
       		    // Configurações do clique duplo
       		    ondblClickRow: function(rowid) {
       				
       				$nome = $("#search-list").getCell(rowid,0);
       				$descricao = $("#search-list").getCell(rowid,1);
       				
					$("input[name='nome']").val($nome);
					$("textarea[name='descricao']").val($descricao);
					
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
       		    sortname: 'idEspecialidade', 
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
	$("input[value='Cadastrar']").live('click', function(event){
		
		//Remove a classe que ignora os elementos
		$("input[name='nome']").removeClass("ignore");
	});
	
	//Define o comportamento da tela ao Consultar
	$("input[value='Consultar']").live('click', function(event){
		
		//Adiciona a classe que ignora os elementos
		$("input[name='nome']").addClass("ignore");
		
		//Abre o campo de pesquisa
		$('#search-dialog').dialog('open');
	});

	//Define o comportamento da tela ao Alterar
	$("input[value='Alterar']").live('click', function(event){
		
		//Adiciona a classe que ignora os elementos
		$("input[name='nome']").removeClass("ignore");
	});
	
	//Define o comportamento da tela ao Excluir
	$("input[value='Excluir']").live('click', function(event){
		
		//Adiciona a classe que ignora os elementos
		$("input[name='nome']").removeClass("ignore");
	});	
});