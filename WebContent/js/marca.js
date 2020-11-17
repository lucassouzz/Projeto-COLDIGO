COLDIGO.marca = new Object();

$(document).ready(function(){
	
	
	//******************* CADASTRANDO MARCAS ******************************
	
	
	
	
	COLDIGO.marca.cadastrar = function(){
		
		
		var marca = new Object();
		marca.nome = document.frmAddMarca.nome.value;
		
		if(marca.nome==""){
			COLDIGO.exibirAviso("Preencha todos os campos");
		} else{
			
			$.ajax({
				
				
				type: "POST",
				url: COLDIGO.PATH + "marca/inserir",
				data:JSON.stringify(marca),
				success: function (msg){
					
					COLDIGO.exibirAviso(msg);
					COLDIGO.marca.buscar();
					$("#addMarca").trigger("reset");
					
					
				},
				error: function(info){
					COLDIGO.exibirAviso("Erro ao cadastrar nova marca: "+ info.status + " - " + info.statusText);
					
				}
				
				
			});
			
		}
		
		
	}
	
	//*************  Filtrar Busca **********
	
	
	COLDIGO.marca.buscar = function(){
		
		var valorBusca = $("#campoBuscaMarca").val();
		
		$.ajax({
			
			type: "GET",
			url: COLDIGO.PATH + "marca/buscarMarca",
			data: "valorBusca="+valorBusca,
			success: function(dados){
				
				dados = JSON.parse(dados);
				console.log(dados);
				$("#listaMarcas").html(COLDIGO.marca.exibir(dados));
			},
			
			error: function(info){
				COLDIGO.exibirAviso("Erro ao consultar as marcas: "+ info.status + " - " + info.statusText);
			}
			
			
		});
		
				
	};
	
	
	

	
	

	// ***************************************** Exibição dos botões *******************
	
	
	COLDIGO.marca.exibir = function(listaDeMarcas){
		
		var tabela =
			
			"<table>" +
				"<tr>"    +
					"<th>Id</th>" +
					
						"<th>Marca</th>" +
						
						"<th> Status </th>" +  //Aqui insiro a coluna do status conforme Orientação Tecnica 18
					
							"<th class='acoes'>Acoes </th>" +
				"</tr>";
		
				
		if((listaDeMarcas != undefined)&&(listaDeMarcas.length > 0)){
			
			
			for (var i=0 ; i<listaDeMarcas.length; i++){
				
				tabela += "<tr>"+
							"<td id='"+i+"'>"+listaDeMarcas[i].id+"</td>" +
							"<td>" +listaDeMarcas[i].nomeMarca  + "</td>" + 
							"<td>" + COLDIGO.marca.exibirBotao(listaDeMarcas,i) +"</td>" + 
							"<td>" +
								"<a onclick=\"COLDIGO.marca.exibirEdicao('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/edit.png' alt='Edita'>  </a> " + 
								"<a onclick=\"COLDIGO.marca.excluir('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/delete.png' alt='Exclui'> </a> " +
							"</td>" +        
							"</tr>"
				
			}
			
			
			
			
			
		}else if(listaDeMarcas==""){
			tabela += "<tr><td colspan='2'> Nenhuma marca encontrada</td></tr>";
		}
		tabela += "</table>";
		
		return tabela;
	};
	
	
	
	
	COLDIGO.marca.buscar();
	
//*********************************************************************** (  Exibe Botão   ) ****************************************//
	
	COLDIGO.marca.exibirBotao = function(listaDeMarcas,i){
		
		var status = '';
		
		if (listaDeMarcas[i].status === 1) {
			status = "checked";
		}
		
		var botaoStatus = "<label class='switch'> <input name = 'botStatusMarca' type='checkbox' onclick=COLDIGO.marca.editarStatus('"+i+"') "+status+">" +
				"<span class='slider round'></span>" +
				"</label>"; 
		
		return botaoStatus;
		
	};	
	
	//*********************************************************************** (   Editar STATUS     ) ****************************************//
	
	COLDIGO.marca.editarStatus = function(i){
		
		var marca = new Object();
		
		// Ao clicar o evento chama a função trazendo o o valor do texto contido dentro do td
		marca.id = $("#"+i+"").text();
		
		var statusMarca = document.getElementsByName('botStatusMarca'); 
		
		// Verifica se esta checked e injeta no marca.stats = 1 ou 0;
		
		if (statusMarca[i].checked) {
			 marca.status = 1;
		} else {
			 marca.status = 0;
		}
				
		$.ajax({
			
			type: "PUT",
			url: COLDIGO.PATH + "marca/alterarStatus",
			data: JSON.stringify(marca),
			success: function(mensagem){	
				
				COLDIGO.marca.verificarErro(mensagem,i); //Verifica se houver erro para retornar status do botão
				COLDIGO.marca.buscar();
				COLDIGO.exibirAviso(mensagem);
				
				$("#modalEditaProduto").dialog("close");
				
				
			},
			error: function(info){
				
				COLDIGO.exibirAviso("Erro ao editar status: "+info.status+" - "+info.statusText);

			}
					
		});
			
	};	
	
	
	//****************************************************************** Função que verifica o erro *******************************************//
		COLDIGO.marca.verificarErro = function(dados,i){
		
		
		
		var classes = document.getElementsByName('botStatusMarca');
		
		var confirme = dados.search("Erro ao alterar status da marca!");
		
			if(confirme === 1){
					classes[i].checked = !classes[i].checked;
			}
		
		};
	
	
	//******************** Excluir ************
	
		COLDIGO.marca.excluir = function(id){
			
		 $.ajax({
			
			 type: "DELETE",
			 url: COLDIGO.PATH + "marca/excluir/"+id,
			 success: function(msg){
				
				 	COLDIGO.exibirAviso(msg);
				 	COLDIGO.marca.buscar();
				 
			 },
			 error: function(info){
				 COLDIGO.exibirAviso("Erro ao excluir a marca: "+ info.status + " - " + info.statusText);
			 }
			 
			 
		 });
		 
		 
			
			
		};
	
		//******************* EDITAR *********************
		
				COLDIGO.marca.exibirEdicao = function(id){
					
					$.ajax({
						
						
							type: "GET",
							url: COLDIGO.PATH + "marca/buscarPorId",
							data: "id="+id,
							success: function(marca){
								
								//** --------------------------------------------------------------------
								//esse frmEditaMarca vem do nome do formulario do index marcas
								document.frmEditaMarca.idMarca.value = marca.id;
								document.frmEditaMarca.nome.value = marca.nome;
								
								var modalEditaMarca = {
										title: "Edita Marca",
										height: 400,
										width: 550,
										modal: true,
										buttons:{
											"Salvar": function(){
												COLDIGO.marca.editar();
											},
											"Cancelar": function(){
												$(this).dialog("close");
											}
										},
										close: function(){
											
										}
										
								};
								
								$("#modalEditaMarca").dialog(modalEditaMarca);
								
							},
							error: function(info){
								COLDIGO.exibirAviso("Erro ao buscar marca para edição: "+ info.status + " - " + info.statusText);
							}
										
						
					});
					
				};
				
				//****************************** Func Botao Salvar *************
				
				
				COLDIGO.marca.editar = function(){
					
					var marca = new Object();
					
					marca.id = document.frmEditaMarca.idMarca.value;
					marca.nome = document.frmEditaMarca.nome.value;
					
					$.ajax({
						
						type:"PUT",
						url: COLDIGO.PATH + "marca/alterar",
						data:JSON.stringify(marca),
						success: function(msg){
							
							COLDIGO.exibirAviso(msg);
							COLDIGO.marca.buscar();
							$("#modalEditaMarca").dialog("close");
						},
						error: function(info){
							COLDIGO.exibirAviso("Erro ao editar marca: "+ info.status + " - " + info.statusText);
						}
						
						
						
					});
					
					
					
					
					
				};
	
	
	
});