package br.com.drummond.mycontacts.lista.modelo;

import android.widget.EditText;

/**
 * Created by Bispo Caike on 13/09/2015.
 */
public class User {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String reset;
    private String isLogado;
    private String foto;
    private String recoverPass;



    public User(String name, String lastName, String email, String password, String reset, String isLogado, String foto, String recoverPass) {

        super();

        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.reset = reset;
        this.isLogado = isLogado;
        this.foto = foto;
        this.recoverPass = recoverPass;

    }


    public String getRecoverPass() {
        return recoverPass;
    }

    public void setRecoverPass(String recoverPass) {
        this.recoverPass = recoverPass;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public String getisLogado() {
        return isLogado;
    }

    public void setIsLogado(String isLogado) {
        this.isLogado = isLogado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
