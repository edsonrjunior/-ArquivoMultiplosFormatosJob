package com.springbatch.arquivomultiplosformatos.reader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springbatch.arquivomultiplosformatos.dominio.Cliente;
import com.springbatch.arquivomultiplosformatos.dominio.Transacao;

@Configuration
public class ClienteTransacaoLineMapper {

	
	//PatternMatchingCompositeLineMapper = Componente que aplica um padrão para descobrir qual lineMapper vai aplicar
	//Map - Não escolher o tipo no Map pq o tipo vai depender o tipo que será lido.
	//Tokenizers = Captura a linha e divide em palavras.
	//FieldSetMappers = Pega a palavra e mapeia para um objeto de domínio
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public PatternMatchingCompositeLineMapper lineMapper() { 
		PatternMatchingCompositeLineMapper lineMapper = new PatternMatchingCompositeLineMapper();
		lineMapper.setTokenizers(tokenizers());
		lineMapper.setFieldSetMappers(fieldSetMappers());
		return lineMapper;
	}

	//Criação e definição dos Mappers
	// Qual o padrão for "0*" será definido que a linha é do tipo cliente, quando for "1* será uma transação".
	
	@SuppressWarnings("rawtypes")
	private Map<String, FieldSetMapper> fieldSetMappers() {
		Map<String, FieldSetMapper> filedMapper = new HashMap<>();
		filedMapper.put("0*", fieldSetMapper(Cliente.class));
		filedMapper.put("1*", fieldSetMapper(Transacao.class));
		return filedMapper;
	}
		
	//BeanWrapperFieldSetMapper = Componente que faz o mapeamento da classe que está sendo mapeada
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private FieldSetMapper fieldSetMapper(Class classe) {
		BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();
		fieldSetMapper.setTargetType(classe);
		return fieldSetMapper;
	}
	
	
	//Criação e definição dos Tokenizers
	private Map<String, LineTokenizer> tokenizers() {
		Map<String, LineTokenizer> tokenizers = new HashMap<>();
		tokenizers.put("0*", clienteLineTokenizer());
		tokenizers.put("1*", transacaoLineTokenizer());
		return tokenizers;
	}
	
	
	//Tokenizer do cliente
	//Não incluir o 0 pois identifica o tipo do registro (0 cliente e 1 transação)
	private LineTokenizer clienteLineTokenizer() {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("nome", "sobrenome", "idade", "email");
		lineTokenizer.setIncludedFields(1, 2, 3, 4);
		return lineTokenizer;
	}
	
	//Tokenizer da Transacao
	//Não incluir o 0 pois identifica o tipo do registro (0 cliente e 1 transação)
	private LineTokenizer transacaoLineTokenizer() {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("id", "descricao", "valor");
		lineTokenizer.setIncludedFields(1, 2, 3);
		return lineTokenizer;
	}
	


	
}
