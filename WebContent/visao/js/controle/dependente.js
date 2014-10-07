$(document).ready(function() {

	/* ********************
	 Controle de validação
	********************* */
	
	//Adiciona o método de validação de CPF
	jQuery.validator.addMethod("cpf", function(value, element) {
		value = value.replace('.','');
		value = value.replace('.','');
		cpf = value.replace('-','');
		while(cpf.length < 11) cpf = "0"+ cpf;
		var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
		var a = [];
		var b = new Number;
		var c = 11;
		for (i=0; i<11; i++){
		a[i] = cpf.charAt(i);
		if (i < 9) b += (a[i] * --c);
		}
		if ((x = b % 11) < 2) { a[9] = 0 } else { a[9] = 11-x }
		b = 0;
		c = 11;
		for (y=0; y<10; y++) b += (a[y] * c--);
		if ((x = b % 11) < 2) { a[10] = 0; } else { a[10] = 11-x; }
		if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]) || cpf.match(expReg)) return false;
		return true;
		}, "Informe um CPF válido.");
	
	//valida o formulario dependente
	$("#dependente").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {
		
			//Pega os atributos do formulário e coloca em forma de URL
			$dados = $("#dependente").serialize();
				
			//Faz a requisição via AJAX
			$.ajax({
				type: "POST",
		   		url: "http://localhost:8080/CamimSCA/servlet/ControleDependente",
		   		data: $dados,
		   		dataType: "json",
		   		cache: false,
		   		async: false,
		   		//Em caso de sucesso direciona a página ou exibe uma mensagem
		   		success: function(data, textStatus, XMLHttpRequest) {
				
					if(data.mensagem) {
						
						$('#dialog').html(data.mensagem);
						
						$('#dialog').dialog('open');
					} else {
						
						$("input[name='matricula']").val(data.matricula);
						$("input[name='senha']").val(data.senha);
						$("input[name='csenha']").val(data.senha);
						$("input[name='nome']").val(data.nome);
						$("input[name='cpf']").val(data.cpf);
						$("input[name='rg']").val(data.rg);
						$("input[name='dataNascimento']").val(data.dataNascimento);
						$("input[name='telefoneResidencia']").val(data.telefoneResidencia);
						$("input[name='telefoneCelular']").val(data.telefoneCelular);
						$("input[name='matriculaTitular']").val(data.matriculaTitular);
					}
	   			
					$("input[name='matricula']").removeClass("ignore");
					$("input[name='senha']").removeClass("ignore");
					$("input[name='csenha']").removeClass("ignore");
					$("input[name='nome']").removeClass("ignore");
					$("input[name='cpf']").removeClass("ignore");
					$("input[name='rg']").removeClass("ignore");
					$("input[name='dataNascimento']").removeClass("ignore");
					$("input[name='telefoneResidencia']").removeClass("ignore");
					("input[name='telefoneCelular']").removeClass("ignore");
					$("input[name='matriculaTitular']").removeClass("ignore");
				}
			});
		},
    	
		//Define as regras a serem aplicadas nos campos
		rules: {
			matricula: {
				required: true,
				number: true,
				minlength: 6
			},
			
			nome: {
				required: true
			},
					
			cpf: {
				required: true,
				number: true,
				cpf: true,
				minlength: 11
			},
					
			rg: {
				required: true,
				number: true,
				minlength: 9
			},
			
			dataNascimento: {
				required: true,
				date: true
			},
			
			telefoneResidencial: {
				number: true
			},
				
			telefoneCelular: {
				number: true
			},
					
			matriculaTitular: {
				required: true,
				number: true,
				minlength: 6
			} 			
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
					
			matricula: {
				required: "Digite a matricula.",
				number: "Digite somente números.",
				minlength: "A matricula deve conter 6 dígitos."
			},
					
			nome: { required: "Digite o Nome." },
					
			cpf: {
				required: "Digite o CPF.",
				number: "Digite somente números.",
				minlength: "O CPF deve conter 11 dígitos."
			},
					
			rg: {
				required: "Digite o RG.",
				minlength: "O RG deve conter 9 dígitos.",
				number: "Digite somente números."
			},
			
			dataNascimento: {
				required: "Digite uma data",
				date: "Digite uma data válida"
			},
			
			telefoneResidencial: {
				number: "Digite apenas numeros"
			},
				
			telefoneCelular: {
				number: "Digite apenas numeros"
			},
				
			matriculaTitular: {
				required: "Digite a matricula do titular.",
				minlength: "A matricula deve conter 6 dígitos.",
				number: "Digite somente números."
			}
		}
	});

	/* ********************
	 Controle dos diálogos
	********************* */
	
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
	
	$("input[value='Cadastrar']").live('click', function(event){
		
		//Esconde o container do resultado da pesquisa
		$("#resultado").hide();
		
		//Remove a classe que ignora os elementos
		$("input[name='matricula']").removeClass("ignore");
		$("input[name='senha']").removeClass("ignore");
		$("input[name='csenha']").removeClass("ignore");
		$("input[name='nome']").removeClass("ignore");
		$("input[name='cpf']").removeClass("ignore");
		$("input[name='rg']").removeClass("ignore");
		$("input[name='dataNascimento']").removeClass("ignore");
		$("input[name='telefoneResidencia']").removeClass("ignore");
		$("input[name='telefoneCelular']").removeClass("ignore");
		$("input[name='matriculaTitular']").removeClass("ignore");
	});
	
	$("input[value='Consultar']").live('click', function(event){
		
		//Mostra o container do resultado da pesquisa
	   	$("#resultado").show();
		
		//Adiciona a classe que ignora os elementos
		$("input[name='matricula']").addClass("ignore");
		$("input[name='senha']").addClass("ignore");
		$("input[name='csenha']").addClass("ignore");
		$("input[name='nome']").addClass("ignore");
		$("input[name='cpf']").addClass("ignore");
		$("input[name='rg']").addClass("ignore");
		$("input[name='dataNascimento']").addClass("ignore");
		$("input[name='telefoneResidencia']").addClass("ignore");
		$("input[name='telefoneCelular']").addClass("ignore");
		$("input[name='matriculaTitular']").addClass("ignore");
	});
	
	$("input[value='Alterar']").live('click', function(event){
		
		//Esconde o container do resultado da pesquisa
		$("#resultado").hide();
		
		//Adiciona a classe que ignora os elementos
		$("input[name='matricula']").removeClass("ignore");
		$("input[name='dataNascimento']").removeClass("ignore");		
		
		$("input[name='senha']").addClass("ignore");
		$("input[name='csenha']").addClass("ignore");
		$("input[name='nome']").addClass("ignore");
		$("input[name='cpf']").addClass("ignore");
		$("input[name='rg']").addClass("ignore");
		$("input[name='telefoneResidencia']").addClass("ignore");
		$("input[name='telefoneCelular']").addClass("ignore");
		$("input[name='matriculaTitular']").addClass("ignore"); 
	});
	
	$("input[value='Excluir']").live('click', function(event){
	
		//Esconde o container do resultado da pesquisa
		$("#resultado").hide();
		
		//Adiciona a classe que ignora os elementos
		$("input[name='matricula']").removeClass("ignore");

		$("input[name='senha']").addClass("ignore");
		$("input[name='csenha']").addClass("ignore");
		$("input[name='nome']").addClass("ignore");
		$("input[name='cpf']").addClass("ignore");
		$("input[name='rg']").addClass("ignore");
		$("input[name='dataNascimento']").addClass("ignore");		
		$("input[name='telefoneResidencia']").addClass("ignore");
		$("input[name='telefoneCelular']").addClass("ignore");
		$("input[name='matriculaTitular']").addClass("ignore"); 
	});
	
	/* ********************
	 Controle dos textfields
	********************* */
	
	$("input[name='matricula']").load('../../dependente.html', function() {

     	$.ajax({
 			type: "POST",
 			url: "http://localhost:8080/CamimSCA/servlet/ControlePessoa?acao=geraMatricula",
 			dataType: "text/html",
 			cache: false,
 			async: false,
 			//Em caso de sucesso direciona a página ou exibe uma mensagem
 			success: function(data, textStatus, XMLHttpRequest) {
     		
 				//Caso algo seja retornado, adiciona no textfield
 				if(data) {
 					$("input[name='matricula']").val(data);	
 				}
 			}
 		});  	
	});	
});