package com.springbatch.arquivomultiplosformatos.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import com.springbatch.arquivomultiplosformatos.dominio.Cliente;
import com.springbatch.arquivomultiplosformatos.dominio.Transacao;

public class ArquivoClienteTransacaoReader implements ItemStreamReader<Cliente> {

//	Variável do tipo Object pois não se sabe se é um cliente ou transação
	private Object objetoAtual;
	
//	Objeto que faz a leitura efetivamente 
	private ItemStreamReader<Object> delegate;
	
	public ArquivoClienteTransacaoReader(ItemStreamReader<Object> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		delegate.open(executionContext);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		delegate.update(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		delegate.close();
	}

	@Override
	public Cliente read() throws Exception{
		
		//Se ainda não leu, ler o objeto
		if (objetoAtual == null) {
			objetoAtual = delegate.read();
		}
		//Uma vez que li o objeto, sei que ele é um cliente, esse método lê apenas cliente
		
		//Faz um casting do objeto
		Cliente cliente = (Cliente) objetoAtual;
		
		//Zera ele para ser lido novamente por outro método
		objetoAtual = null;
		
		//Não terminou de ler o arquivo
		if(cliente != null) {
			//Se for uma transação, add a transação no cliente
			while (peek() instanceof Transacao)
				cliente.getTransacoes().add((Transacao) objetoAtual);
		}	
		//Retorna o cliente com todas suas transações.
		return cliente;			
	}

	//Método que fica espiando o que está lendo, cliente ou transação
	private Object peek() throws Exception{
		objetoAtual = delegate.read();
		return objetoAtual;
	}

}
