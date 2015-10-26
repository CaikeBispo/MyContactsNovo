package br.com.drummond.mycontacts.lista.modelo;

import java.io.Serializable;

public class Contato implements Serializable{
    private Long id;
    private String nome, telefone, email, endereco, foto,  opTelein;
    int operadora;
    private int tipoendereco;
    private int tipoemail;
    private Double favorito,latitude,longitude;

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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Double getFavorito() {
        return favorito;
    }
    public void setFavorito(Double favorito) {
        this.favorito = favorito;
    }

    public int getOperadora() {
        return operadora;
    }
    public void setOperadora(int operadora) {
        this.operadora = operadora;
    }

    public int getTipoendereco() {
        return tipoendereco;
    }
    public void setTipoendereco(int tipoendereco) {
        this.tipoendereco = tipoendereco;
    }

    public int getTipoemail() {
        return tipoemail;
    }
    public void setTipoemail(int tipoemail) {
        this.tipoemail = tipoemail;
    }

    public String getStrOp() {
        return opTelein;
    }
    public void setStrOp(String opTelein) {
        this.opTelein = opTelein;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}