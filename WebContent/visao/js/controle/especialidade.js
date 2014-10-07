$(document).ready(function() {

	/* ********************
	 Controle de valida��o
	********************* */
	
	//valida o formulario especialidade
	$("#especialidade").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formul�rio estiver v�lido
		submitHandler: function(form) {
		
			//Pega os atributos do formul�rio e coloca em forma de URL
			$dados = $("#especialidade").serialize();
				
			//Faz a requisi��o via AJAX
			$.ajax({
				type	 : "POST",
		   		url		 : "http://localhost:8080/CamimSCA/servlet/ControleEspecialidade",
		   		data	 : $dados,
		   		dataType : "json",
		   		cache	 : false,
		   		async	 : false,
		   		
		   		//Em caso de sucesso direciona a p�gina ou exibe uma mensagem
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
				maxlength : "O campo deve conter at� 45 caracteres."
			},
			descricao : {
				maxlength : "O campo deve conter at� 1024 caracteres."
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
	 Controle dos di�logos
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
       		 
       			// Configura��o da requisi��o
       			url:'http://localhost:8080/CamimSCA/servlet/ControleEspecialidade?acao=Consultar',
       			datatype: 'json',
       			mtype: 'POST',
       			   		        
       			// Configura��o do leitor de JSON
       			jsonReader: {   
       				root: "rows", 
       				page: "page", 
       				total: "total", 
       				records: "records", 
       				cell: "cell", 
       				repeatitems: false
       			},
       					
       			// Configura��o do nome das colunas
       			colNames: ['Nome', 'Descricao'],    
       				
       			// Configura��o das colunas
       		    colModel: [ 
       		           { name: 'nome'	  , index: 'nome'	  , align:'center', sortable:false }, 
       		           { name: 'descricao', index: 'descricao', align:'center', sortable:false }
       		    ],
       			
       		    // Configura��es do clique duplo
       		    ondblClickRow: function(rowid) {
       				
       				$nome = $("#search-list").getCell(rowid,0);
       				$descricao = $("#search-list").getCell(rowid,1);
       				
					$("input[name='nome']").val($nome);
					$("textarea[name='descricao']").val($descricao);
					
					$("#search-list").GridUnload();
					
					//Fecha o campo de pesquisa
					$('#search-dialog').dialog('close');
       			},
       		    
       		    // Configura��o da exibi��o
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
	 Controle dos Bot�es
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