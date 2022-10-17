/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.model;

import br.com.curso.utils.Conversao;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author Aluno
 */
public class Cliente extends Pessoa {
    
    private int idCliente;
    private String observacao;
    private String permiteLogin;
    private String situacao;

    public Cliente(int idCliente, String observacao, String permiteLogin, String situacao, int idPessoa, String cpfCnpj, String nome, Date dataNascimento, Cidade cidade, String login, String senha, String foto) {
        super(idPessoa, cpfCnpj, nome, dataNascimento, cidade, login, senha, foto);
        this.idCliente = idCliente;
        this.observacao = observacao;
        this.permiteLogin = permiteLogin;
        this.situacao = situacao;
    }
    
    public static Cliente clienteVazio () throws ParseException{
        Cidade oCidade = new Cidade();
        Date dataNascimento = Conversao.dataAtual();
        Cliente oCliente = new Cliente(0, "", "S", "A", 0, "", "", dataNascimento, oCidade, "", "", null);
        return oCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getPermiteLogin() {
        return permiteLogin;
    }

    public void setPermiteLogin(String permiteLogin) {
        this.permiteLogin = permiteLogin;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    
}
