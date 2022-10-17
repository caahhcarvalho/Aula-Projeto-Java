/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.dao;

import br.com.curso.model.Cidade;
import br.com.curso.model.Fornecedor;
import br.com.curso.utils.SingleConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jgcoc
 */
public class FornecedorDAO implements GenericDAO{
    
    private Connection conexao;

    public FornecedorDAO() throws Exception {
        conexao = SingleConnection.getConnection();
    }

    @Override
    public Boolean cadastrar(Object objeto) {
        Boolean retorno = false;
        try {
            Fornecedor oFornecedor = (Fornecedor) objeto;
            if (oFornecedor.getIdFornecedor() == 0) {
                int idFornecedor = this.verificarCpf(oFornecedor.getCpfCnpj());
                if (idFornecedor == 0) {
                    retorno = this.inserir(oFornecedor);
                } else {
                    oFornecedor.setIdFornecedor(idFornecedor);
                    retorno = this.alterar(oFornecedor);
                }
            } else {
                retorno = this.alterar(oFornecedor);
            }
        } catch (Exception ex) {
            System.out.println("Problemas ao Cadastrar Fornecedor na DAO! Erro: " + ex.getMessage());
        }
        return retorno;
    }

    @Override
    public Boolean inserir(Object objeto) {
        Fornecedor oFornecedor = (Fornecedor) objeto;
        PreparedStatement stmt = null;
        String sql = "insert into fornecedor (idPessoa, enderecoweb, situacao, permitelogin) values (?, ?, ?, ?)";

        try {
            PessoaDAO oPessoaDAO = new PessoaDAO();
            int idPessoa = oPessoaDAO.cadastrar(oFornecedor);
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPessoa);
            stmt.setString(2, oFornecedor.getEnderecoWeb());
            stmt.setString(3, "A");
            stmt.setString(4, oFornecedor.getPermiteLogin());
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception e) {
            try {
                System.out.println("Problemas ao Inserir Fornecedor na DAO! Erro: " + e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            } catch (SQLException ex) {
                System.out.println("Problemas ao executar rollback" + ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Boolean alterar(Object objeto) {
        Fornecedor oFornecedor = (Fornecedor) objeto;
        PreparedStatement stmt = null;
        String sql = "update fornecedor set enderecoweb = ?, permitelogin = ? where idFornecedor = ?";

        try {
            PessoaDAO oPessoaDAO = new PessoaDAO();
            oPessoaDAO.cadastrar(oFornecedor);
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oFornecedor.getEnderecoWeb());
            stmt.setString(2, oFornecedor.getPermiteLogin());
            stmt.setInt(3, oFornecedor.getIdFornecedor());
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception e) {
            try {
                System.out.println("Problemas ao Alterar Fornecedor na DAO! Erro: " + e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            } catch (SQLException ex) {
                System.out.println("Problemas ao executar rollback" + ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Boolean excluir(int numero) {
        PreparedStatement stmt = null;
        try {
            FornecedorDAO oFornecedorDAO = new FornecedorDAO();
            Fornecedor oFornecedor = (Fornecedor) oFornecedorDAO.carregar(numero);
            String situacao = "A";
            if (oFornecedor.getSituacao().equals(situacao)) {
                situacao = "I";
            } else {
                situacao = "A";
            }

            String sql = "update fornecedor set situacao = ? where idFornecedor = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, situacao);
            stmt.setInt(2, oFornecedor.getIdFornecedor());
            stmt.execute();
            conexao.commit();
            return true;
        } catch (Exception e) {
            try {
                System.out.println("Problemas ao Excluir Fornecedor na DAO! Erro: " + e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            } catch (SQLException ex) {
                System.out.println("Problemas ao executar rollback" + ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Object carregar(int numero) {
        int idFornecedor = numero;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Fornecedor oFornecedor = null;
        String sql = "select * from fornecedor c, pessoa p where c.idpessoa = p.idpessoa and c.idfornecedor = ?";

        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idFornecedor);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cidade oCidade = null;
                try {
                    CidadeDAO oCidadeDAO = new CidadeDAO();
                    oCidade = (Cidade) oCidadeDAO.carregar(rs.getInt("idcidade"));
                } catch (Exception ex) {
                    System.out.println("Problemas ao Carregar Cidade na ClienteDAO! Erro: " + ex.getMessage());
                }

                oFornecedor = new Fornecedor(rs.getInt("idfornecedor"),
                        rs.getString("enderecoweb"),   
                        rs.getString("situacao"),
                        rs.getString("permitelogin"),                        
                        rs.getInt("idpessoa"),
                        rs.getString("cpfcnpj"),
                        rs.getString("nome"),
                        rs.getDate("datanascimento"),
                        oCidade,
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("foto"));
            }
        } catch (SQLException e) {
            System.out.println("Problemas ao Carregar Fornecedor na DAO! Erro: " + e.getMessage());
            e.printStackTrace();
        }
        return oFornecedor;
    }

    @Override
    public List<Object> listar() {
        List<Object> resultado = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select p.*, c.idfornecedor, c.enderecoweb, c.situacao, c.permitelogin from fornecedor c, pessoa p "
                + "where c.idpessoa = p.idpessoa order by idPessoa";

        try {
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cidade oCidade = null;
                try {
                    CidadeDAO oCidadeDAO = new CidadeDAO();
                    oCidade = (Cidade) oCidadeDAO.carregar(rs.getInt("idcidade"));
                } catch (Exception ex) {
                    System.out.println("Problemas ai Listar Cidade na FornecedorDAO! Erro: " + ex.getMessage());
                }

                Fornecedor oFornecedor = new Fornecedor(rs.getInt("idfornecedor"),
                        rs.getString("enderecoweb"),
                        rs.getString("situacao"),
                        rs.getString("permitelogin"),                        
                        rs.getInt("idpessoa"),
                        rs.getString("cpfcnpj"),
                        rs.getString("nome"),
                        rs.getDate("datanascimento"),
                        oCidade,
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("foto"));
                resultado.add(oFornecedor);
            }
        } catch (SQLException ex) {
            System.out.println("Problemas ai Listar Fornecedor na DAO! Erro: " + ex.getMessage());
        }
        return resultado;
    }
    
    public int verificarCpf(String cpf) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idFornecedor = 0;
        String sql = "select c.* from fornecedor c, pessoa p where c.idpessoa = p.idPessoa and p.cpfcnpj = ?;";

        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            while (rs.next()) {
                idFornecedor = rs.getInt("idfornecedor");
            }
            return idFornecedor;
        } catch (SQLException ex) {
            System.out.println("Problemas ao VerificarCPF Fornecedor na DAO! Erro: " + ex.getMessage());
            return idFornecedor;
        }
    }
}
