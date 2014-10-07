$(function() {

	$("#login").validate({
 
 	   	errorContainer: "#error",
 	   	errorLabelContainer: "#error ul",
 	   	wrapper: "li", 
	
   	   	//Executa quando o formul�rio estiver v�lido
		submitHandler: function(form) {
			$dados = $("#login").serialize();
				
			$.ajax({
				type	 : "POST",
				url		 : "http://localhost:8080/CamimSCA/servlet/ControlePrincipal",
				data	 : $dados,
				dataType : "text/html",
				cache	 : false,
				async	 : false,
				//Em caso de sucesso direciona a p�gina ou exibe uma mensagem
				success	 : function(data, textStatus, XMLHttpRequest) {
				
					//Caso retorne dados exibe a mensagem, caso contr�rio redireciona a p�gina
					if(data){
						$("#dialog").html(data);
						
						$("#dialog").dialog("open");
						
					} else {
						window.location.href = data;
					}
				},
				//Mostra um alerta em caso de erro
			    error	 : function(XMLHttpRequest, textStatus, errorThrown) {  
					jAlert('Erro!  Status = ' + errorThrown,'ERRO');  
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
		
			senha: {
				required  : true
			}
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matricula: {
				required  : "Digite uma matricula.",
				minlength : "A matricula deve conter 6 digitos.",
				number	  : "A matricula deve conter somente numeros."
			},
			
			senha: {
				required  : "Digite uma senha."
			}
		}
	});
    
    $("#error").hide();
    
    //Configura��o dos alertas
    $("#dialog").dialog({
        modal	 : true, 
        position : ['center',500], 
        autoOpen : false, 
        title	 : 'Erro', 
        overlay	 : { 
			opacity	   : 0.5,
			background : 'black'
		} 
	}); 
	 
    //Configura��o dos bot�es
    $("input:submit, input:button, input:reset").button();
    
    //Configura��o das datas
    $.datepicker.setDefaults($.datepicker.regional['pt-BR']);
    
    $(".data").datepicker();
		
    $(".dataN").datepicker({maxDate: '0d'});
    
	//Configura��o do AJAX
	$("a").live("click", function(){
			
		var path = $(this).attr('href');
		
		path = path.replace('#','');
		
		$('#col2_content').load(path);

	});
});