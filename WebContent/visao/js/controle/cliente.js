$(document).ready(function() {
	
	var today = new Date();
	var hoje = 
	
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
	
	jQuery.validator.addMethod(“data”, function(value, element) {
//contando chars
if(value.length!=10) return false;
// verificando data
var data = value;
var dia = data.substr(0,2);
var barra1 = data.substr(2,1);
var mes = data.substr(3,2);
var barra2 = data.substr(5,1);
var ano = data.substr(6,4);
if(data.length!=10||barra1!=”/”||barra2!=”/”||isNaN(dia)||isNaN(mes)||isNaN(ano)||dia>31||mes>12)return false;
if((mes==4||mes==6||mes==9||mes==11)&&dia==31)return false;
if(mes==2 && (dia>29||(dia==29 && ano % 4 != 0 || ano % 100 == 0 && ano % 400 != 0)))return false;
if(ano < 1900)return false;
return true;
}, "Informe uma data válida"); // Mensagem padrão


	
	//Valida o formulario do cliente
	$("#cliente").validate({
    	
		//Classe que ignora os elementos
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {
		
			//Pega os atributos do formulário e coloca em forma de URL
			$dados = $("#cliente").serialize();
		
			//Faz a requisição via AJAX
			$.ajax({
				type: "POST",
		   		url: "http://localhost:8080/CamimSCA/servlet/ControleCliente",
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
							
						$("input[name='logradouro']").val(data.logradouro);
						$("input[name='complemento']").val(data.complemento);
						$("input[name='bairro']").val(data.bairro);
						$("input[name='cep']").val(data.cep);
						$("input[name='cidade']").val(data.cidade);
						
						$("input[name='tipoPlano']").text().select();

					}
	   			
					$("input[name='matricula']").removeClass("ignore");
					$("input[name='senha']").removeClass("ignore");
					$("input[name='csenha']").removeClass("ignore");
					
					$("input[name='nome']").removeClass("ignore");
					$("input[name='cpf']").removeClass("ignore");
					$("input[name='rg']").removeClass("ignore");
					$("input[name='dataNascimento']").removeClass("ignore");
					$("input[name='telefoneResidencia']").removeClass("ignore");
					$("input[name='telefoneCelular']").removeClass("ignore");
					
					$("input[name='logradouro']").removeClass(data.logradouro);
					$("input[name='complemento']").removeClass(data.complemento);
					$("input[name='bairro']").removeClass(data.bairro);
					$("input[name='cep']").removeClass(data.cep);
					$("input[name='cidade']").removeClass(data.cidade);			
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
			},
			
			csenha: {
				required  : true,
				equalTo   : "#senha"
			},
			
			nome: {
				required  : true
			},
					
			cpf: {
				required  : true,
				number    : true,
				cpf       : true,
				minlength : 11
			},
					
			rg: {
				required : true,
				number   : true,
				minlength: 9
			},
			
			dataNascimento: {
				required: true,
				date: true,
				data : true
			},
			
			telefoneResidencial: {
				number: true
			},
				
			telefoneCelular: {
				number: true
			},
			
			logradouro: {
				required: true
			},
		
			bairro: {
				required: true
			},
			
			cep: {
				required: true,
				number: true,
				minlength: 8				
			},

			cidade: {
				required: true
			},
			
			tipoPlano: {
				required: true
			}
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matricula: {
				required: "Digite a matricula.",
				number: "Digite somente números.",
				minlength: "A matricula deve conter 6 dígitos."
			},
				
			senha: { required: "Digite a Senha." },
					
			csenha: { required: "Campo obrigatório", equalTo: "As senhas devem estar iguais" },
					
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
						
			logradouro: {
				required : "Digite o Logradouro."
			
			},

			bairro: {
				required : "Digite o Bairro."
			},	

			cep: {
				required: "Digite o CEP.",
				minlength: "O CEP deve conter 8 dígitos.",
				number: "Digite somente números."
			},

			cidade: {
				required: "Digite uma cidade"
			},
			
			tipoPlano: {
				required: "Selecione a opcao desejada"
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
	
	//Define o comportamento da tela ao Cadastrar
	$("input[value='Cadastrar']").live('click', function(event){

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
		$("input[name='logradouro']").removeClass("ignore");
		$("input[name='bairro']").removeClass("ignore");
		$("input[name='cep']").removeClass("ignore");
		$("input[name='cidade']").removeClass("ignore");
		$("input[name='tipoPlano']").removeClass("ignore");
	});
	
	//Define o comportamento da tela ao Consultar
	$("input[value='Consultar']").live('click', function(event){

		//Abre o campo de pesquisa
		$('#search-dialog').dialog('open');
		
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
		$("input[name='logradouro']").addClass("ignore");
		$("input[name='bairro']").addClass("ignore");
		$("input[name='cep']").addClass("ignore");
		$("input[name='cidade']").addClass("ignore");
		$("input[name='tipoPlano']").addClass("ignore");
	});
	
	//Define o comportamento da tela ao Alterar
	$("input[value='Alterar']").live('click', function(event){
		
		//Remove a classe que ignora os elementos
		$("input[name='matricula']").removeClass("ignore");

		$("input[name='senha']").addClass("ignore");
		$("input[name='csenha']").addClass("ignore");
		$("input[name='nome']").addClass("ignore");
		$("input[name='cpf']").addClass("ignore");
		$("input[name='rg']").addClass("ignore");
		$("input[name='dataNascimento']").addClass("ignore");		
		$("input[name='telefoneResidencia']").addClass("ignore");
		$("input[name='telefoneCelular']").addClass("ignore");
		$("input[name='logradouro']").addClass("ignore");
		$("input[name='bairro']").addClass("ignore");
		$("input[name='cep']").addClass("ignore");
		$("input[name='cidade']").addClass("ignore");
		$("input[name='tipoPlano']").addClass("ignore");
	});	
	
	//Define o comportamento da tela ao Excluir
	$("input[value='Excluir']").live('click', function(event){
		
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
		$("input[name='logradouro']").addClass("ignore");
		$("input[name='bairro']").addClass("ignore");
		$("input[name='cep']").addClass("ignore");
		$("input[name='cidade']").addClass("ignore");
		$("input[name='tipoPlano']").addClass("ignore");
	});
	
	/* ********************
	 Controle dos textfields
	********************* */
	
	$("input[name='matricula']").load('../../cliente.html', function() {

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