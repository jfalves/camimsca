$(document).ready(function() {
	
	var coluna;
	var conteudo;
	var intervalo;

	/* ********************
	 Controle de valida��o
	********************* */
	
	//Valida o formulario do atendimento
	$("#relatorio").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formul�rio estiver v�lido
		submitHandler: function(form) {	

        	$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/CamimSCA/servlet/ControleRelatorio?acao=clienteEspecialidade",
				dataType : "text/html",
				cache	 : false,
				async	 : false,
    			//Em caso de sucesso direciona a p�gina ou exibe uma mensagem
    			success: function(data, textStatus, XMLHttpRequest) {
        		
	        		if (data) {
	        			
	                } else {
	                	window.location.href = data;
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
				number	  : true,
				minlength : 6
			}			
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matriculaMedico: {
				required: "Digite a matricula.",
				number: "Digite somente n�meros.",
				minlength: "A matricula deve conter 6 d�gitos."
			}
		}
	});
	
	/* ********************
	 Controle dos di�logos
	********************* */	
	
	$('#dialog').dialog({ 
		autoOpen: false,
        buttons: {
			"Nao": function() {
				$(this).dialog('close');
			},
			
			"Sim": function() {
				
				//Pega os atributos do formul�rio e coloca em forma de URL
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
		 			//Em caso de sucesso direciona a p�gina ou exibe uma mensagem
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
	 Controle dos Bot�es
	********************* */
    
});