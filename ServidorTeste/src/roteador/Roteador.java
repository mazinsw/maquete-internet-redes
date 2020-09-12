package roteador;

import java.io.Serializable;

public class Roteador implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6109012330480234820L;
	private String nome;

	public Roteador(String nome2) {
		setNome(nome2);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
