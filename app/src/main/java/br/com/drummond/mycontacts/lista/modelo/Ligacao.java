package br.com.drummond.mycontacts.lista.modelo;

import java.io.Serializable;

public class Ligacao implements Serializable{
    private Long id,idContato;
    private String nome,telefone,foto,opTelein;
    private String horaligacao;

    @Override
    public String toString() {
        return nome;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getOpTelein() {
        return opTelein;
    }

    public void setOpTelein(String opTelein) {
        this.opTelein = opTelein;
    }

    public String getHoraligacao() {
        return horaligacao;
    }

    public void setHoraligacao(String horaligacao) {
        this.horaligacao = horaligacao;
    }
}