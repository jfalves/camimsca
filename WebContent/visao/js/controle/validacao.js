$(document).ready(function() {
    $("#login").validate({
		//Executa quando o formulário estiver válido
		submitHandler: function(form) {
			$dados = $('#login').serialize();
				
			$.ajax({
				type: "POST",
				url: "http://localhost:8080/CamimSCA/servlet/ControlePrincipal",
				data: $dados,
				dataType: "text/html",
				cache: false,
				async: false,
				//Em caso de sucesso direciona a página ou exibe uma mensagem
				success: function(data, textStatus, XMLHttpRequest) {
				
					//Caso retorne dados exibe a mensagem, caso contrário redireciona a página
					if(data){
						jAlert(data,"Erro");
					} else {
						window.location.href = data;
					}
				},
				//Mostra um alerta em caso de erro
			    error: function(XMLHttpRequest, textStatus, errorThrown) {  
					jAlert('Erro!  Status = ' + errorThrown,'ERRO');  
				} 
			});
		},
		
		//Define as regras a serem aplicadas nos campos
		rules: {
			matricula: {
				required  : true,
				digits	  : true,
				minlength : 6
			},
		
			senha: {
				required: true
			}
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matricula: {
				required: "Digite a matricula.",
				minlength: "A matricula deve conter 6 digitos.",
				digits: "Digite somente numeros."
			},
			senha: "Digite a Senha."
		}
	});
    
    $("#especialidade").validate({
		
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {
			$dados = $('#especialidade').serialize();
		
			$.ajax({
				type: "POST",
				url: "http://localhost:8080/CamimSCA/servlet/ControleEspecialidade",
				data: $dados,
				dataType: "text/html",
				cache: false,
				async: false,
				//Em caso de sucesso direciona a página ou exibe uma mensagem
				success: function(data, textStatus, XMLHttpRequest) {
					jAlert(data);
					
					// Em qualquer ação tomada, a combobox de pesquisa é resetada
					$("#listaEspecialidade").html("<option value=0 selected=\"selected\">Buscar</option>");	
				},
				//Mostra um alerta em caso de erro
			    error: function(XMLHttpRequest, textStatus, errorThrown) {  
					jAlert('Erro!  Status = ' + errorThrown,'ERRO');  
				} 
			});	
		},
		
		//Define as regras a serem aplicadas nos campos
		rules: {
			nome: { required: true, maxlength: 45 },
			descricao: { maxlength: 1024 }
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			matricula: {
				required: "Digite o nome da especialidade.",
				maxlength: "O campo deve conter até 45 caracteres."
			},
			descricao: {
				maxlength: "O campo deve conter até 1024 caracteres."
			}
		}
	});
	
    $("#listaEspecialidade").blur(function(){
        $indice = $("option:selected").val(); 
    
        //Caso o indice seja 0 procura todos os registros
        if($indice == 0) {
        	$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/CamimSCA/servlet/ControleEspecialidade",
    			data: {acao : "listaEspecialidade"},
    			dataType: "text/html",
    			cache: false,
    			async: false,
    			//Em caso de sucesso direciona a página ou exibe uma mensagem
    			success: function(data, textStatus, XMLHttpRequest) {
    				
    				//Caso algo seja retornado, adiciona na combobox
    				if(data){
    					$("#listaEspecialidade").html(data);	
    				}
    			},
    			//Mostra um alerta em caso de erro
    		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
    				jAlert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
    			} 
    		});  	
        } else {
        	$nome = $("option[value="+$indice+"]").text();
        	
        	alert($nome);
        	
        	$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/CamimSCA/servlet/ControleEspecialidade",
    			data: {acao: "Consultar", nome: $nome },
    			dataType: "json",
    			cache: false,
    			async: false,
    			//Em caso de sucesso direciona a página ou exibe uma mensagem
    			success: function(data, textStatus, XMLHttpRequest) {
    				
    				//Caso algo seja retornado, adiciona nos respectivos campos
    				if (data) {
    					$("input[name='nome']").val(data.nome);
    					$("textarea[name='descricao']").val(data.descricao);
    				}
    			},
    			//Mostra um alerta em caso de erro
    		    error: function(XMLHttpRequest, textStatus, errorThrown) {  
    				jAlert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
    			} 
    		});
        }
    });
	
	jQuery.validator.addMethod("idadeMinima", function(value, element, params) {
		return this.optional(element) || value < $("input[name='idadeMaxima']").val();
		}, "A idade minima deve ser menor que a maxima.");
	
	jQuery.validator.addMethod("idadeMaxima", function(value, element, params) {
		return this.optional(element) || value > $("input[name='idadeMinima']").val();
		}, "A idade maxima deve ser maior que a minima.");

	$("#planoSaude").validate({
		
    	//Executa quando o formulário estiver válido
		submitHandler: function(form) {
			$dados	   = $('#planoSaude').serialize();
			
			//Obtem o nome do option a partir do selecionado
			$nomePlano = $("option[value="+$("option:selected").val()+"]").text();
			
			//Monta a requisição no formato URL
			$tipoPlano = "&tipoPlano="+$nomePlano;
			
			//Concatena com os outros dados
			$dados 	  += $tipoPlano;
			
			if($dados.indexOf("Consultar") != -1) {
			    
				$("#resultado").show();
			    
			    $("#list").jqGrid({
			        url:'http://localhost:8080/CamimSCA/servlet/ControlePlanoSaude?acao=Consultar',
			        datatype: 'json',
			        mtype: 'POST',
			        
			        // this is what jqGrid is looking for in json callback 
			        jsonReader: {   
			            root: "rows", 
			            page: "page", 
			            total: "total", 
			            records: "records", 
			            cell: "cell", 
			            id: "id",
			            repeatitems: false
			        }, 
			        colNames: ['Id', 'Idade Minima', 'Idade Maxima', 'Valor Faixa', 'Tipo Plano'],    
			        colModel: [ 
			            { name: 'id', index: 'id', width: 55, search: false }, 
			            { name: 'idadeMinima', index: 'idadeMinima', width: 50 }, 
			            { name: 'idadeMaxima', index: 'idadeMaxima', width: 150 },
			            { name: 'valorFaixa', index: 'valorFaixa', width: 150 },
			            { name: 'tipoPlano', index: 'tipoPlano', width: 200 }
			        ],   
			        pager: $("#pager"), 
			        sortname: "id",    
			        sortorder: "asc", 
			        viewrecords: true, 
			        caption: "Resultado" 
			      }).jqGrid('navGrid', '#pager', { edit: false, add: false, del: false }); 
			} else {
				$.ajax({
					type: "POST",
					url: "http://localhost:8080/CamimSCA/servlet/ControlePlanoSaude",
					data: $dados,
					dataType: "text/html",
					cache: false,
					async: false,
					//Em caso de sucesso direciona a página ou exibe uma mensagem
					success: function(data, textStatus, XMLHttpRequest) {
						jAlert(data);
					
						// Em qualquer ação tomada, a combobox de pesquisa é resetada
						$("#listaTipoPlanoSaude").html("<option value=0 selected=\"selected\">Buscar</option>");
						$("#resultado").hide();
					},
					//Mostra um alerta em caso de erro
				    error: function(XMLHttpRequest, textStatus, errorThrown) {  
						jAlert('Erro!  Status = ' + errorThrown,'ERRO');  
					} 
				});				
			}
		},
		
		//Define as regras a serem aplicadas nos campos
		rules: {
			listaTipoPlanoSaude : { required: true },
			idadeMinima     	: { required: true, number: true, range: [0, 100], idadeMinima: true },
			idadeMaxima			: { required: true, number: true, range: [0, 100], idadeMaxima: true },
			valorFaixa			: { required: true, number: true }
		},

		//Define as mensagens a serem exibidas para cada campo
		messages: {
			listaTipoPlanoSaude: {
				required: "Selecione um tipo de plano."
			},
			idadeMinima: {
				required: "Digite uma idade mínima.",
				number  : "Digite um número válido",
				range   : "Digite uma idade entre 0 e 100"
			},
			idadeMaxima: {
				required: "Digite uma idade máxima.",
				number  : "Digite um número válido",
				range   : "Digite uma idade entre 0 e 100"
			},
			valorFaixa: {
				required: "Digite o valor da faixa etária.",
				number  : "Digite um número válido"
			}
		}
	});
	
    $("#listaTipoPlanoSaude").blur(function(){
        $indice = $("option:selected").val(); 
        
        //Caso o indice seja 0 procura todos os registros
        if($indice == 0) {
        	$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/CamimSCA/servlet/ControlePlanoSaude",
    			data: {acao : "listaTipoPlanoSaude"},
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
    				jAlert('Erro!  Status = ' + XMLHttpRequest.status,'ERRO');  
    			} 
    		});  	
        } 
    });
    
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
    
    $("#dependente").validate({
    		
    	ignore: ".ignore",
    	
    	//Executa quando o formulário estiver válido
    	submitHandler: function(form) {

			$dados = $("#dependente").serialize();
				
			alert($dados);
				
					
   			if($dados.indexOf("Consultar") != -1) {		
        		
   				$("#resultado").show();
    			    
   			    $("#list").jqGrid({
   			        url:'http://localhost:8080/CamimSCA/servlet/ControleDependente?acao=Consultar',
   			        datatype: 'json',
   			        mtype: 'POST',
   			        
   			        // this is what jqGrid is looking for in json callback 
   			        jsonReader: {   
   			            root: "rows", 
   			            page: "page", 
   			            total: "total", 
   			            records: "records", 
   			            cell: "cell", 
   			            id: "id",
   			            repeatitems: false
   			        }, 
   			        colNames: ['Id', 'Matricula', 'Nome', 'Cpf', 'Rg', 'Data Nascimento', 'Telefone Residencia', 'Telefone Celular', 'Matricula Titular'],    
   			        colModel: [ 
   			            { name: 'id', index: 'id', width: 55, search: false }, 
   			            { name: 'matricula', index: 'matricula', width: 50 }, 
   			            { name: 'nome', index: 'nome', width: 150 },
   			            { name: 'cpf', index: 'cpf', width: 150 },
   			            { name: 'rg', index: 'rg', width: 150 },
   			            { name: 'dataNascimento', index: 'dataNascimento', width: 150 },
   			            { name: 'telefoneResidencia', index: 'telefoneResidencia', width: 150 },
   			            { name: 'telefoneCelular', index: 'telefoneCelular', width: 150 },
   			            { name: 'matriculaTitular', index: 'matriculaTitular', width: 150 }
   			        ],   
   			        pager: $("#pager"), 
   			        sortname: "id",    
   			        sortorder: "asc", 
   			        viewrecords: true, 
   			        caption: "Resultado" 
   			      }).jqGrid('navGrid', '#pager', { edit: false, add: false, del: false });
   			    
				//Remove a classe que ignora os elementos
			    $("input[name='senha']").removeClass("ignore");
			    $("input[name='csenha']").removeClass("ignore");
			    $("input[name='nome']").removeClass("ignore");
			    $("input[name='cpf']").removeClass("ignore");
			    $("input[name='rg']").removeClass("ignore");
			    $("input[name='dataNascimento']").removeClass("ignore");
			    $("input[name='telefoneResidencia']").removeClass("ignore");
			    $("input[name='telefoneCelular']").removeClass("ignore");
			    $("input[name='matriculaTitular']").removeClass("ignore"); 
   			} else {
   				$("#resultado").hide();
   							
   				$.ajax({
   					type: "POST",
   					url: "http://localhost:8080/CamimSCA/servlet/ControleDependente",
   					data: $dados,
   					dataType: "text/html",
   					cache: false,
   					async: false,
   					//Em caso de sucesso direciona a página ou exibe uma mensagem
   					success: function(data, textStatus, XMLHttpRequest) {
   						jAlert(data);
   						
   		   		    	$("input[name='senha']").removeClass("ignore");
   		   		    	$("input[name='csenha']").removeClass("ignore");
   		   		    	$("input[name='nome']").removeClass("ignore");
   		   		    	$("input[name='cpf']").removeClass("ignore");
   		   		    	$("input[name='rg']").removeClass("ignore");
   		   		    	$("input[name='telefoneResidencia']").removeClass("ignore");
   		   		    	$("input[name='telefoneCelular']").removeClass("ignore");
   		   		    	$("input[name='matriculaTitular']").removeClass("ignore"); 
   					},
   					//Mostra um alerta em caso de erro
   				    error: function(XMLHttpRequest, textStatus, errorThrown) {  
   						jAlert('Erro!  Status = ' + errorThrown,'ERRO');  
   					} 
   				});	
   			}
   },
    		
    //Define as regras a serem aplicadas nos campos
    rules: {
    	matricula: {
    		required: true,
    		number: true,
    		minlength: 6
    	},
    
    	senha: {
    		required: true
    	},
    	
    	csenha: {
		    required: true,
    		equalTo: "#senha"
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
    		
    	matriculaTitular: {
    		required: "Digite a matricula do titular.",
    		minlength: "A matricula deve conter 6 dígitos.",
    		number: "Digite somente números."
    	}
    }
});

    
 $("#desabilitaDependente").click(function(event){
    	$("input[name='senha']").addClass("ignore");
    	$("input[name='csenha']").addClass("ignore");
    	$("input[name='nome']").addClass("ignore");
    	$("input[name='cpf']").addClass("ignore");
    	$("input[name='rg']").addClass("ignore");
    	$("input[name='telefoneResidencia']").addClass("ignore");
    	$("input[name='telefoneCelular']").addClass("ignore");
    	$("input[name='matriculaTitular']").addClass("ignore"); 
});
    
    $(".data").datepicker($.datepicker.regional['br'],{maxDate: '+0D'});
});