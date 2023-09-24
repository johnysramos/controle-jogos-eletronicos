package com.github.johnysramos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class JogoEletronico {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    private String nome;

    private int status;

    private boolean emprestado;

    private int consolePlataforma;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEmprestado() {
        return emprestado;
    }

    public void setEmprestado(boolean emprestado) {
        this.emprestado = emprestado;
    }

    public int getConsolePlataforma() {
        return consolePlataforma;
    }

    public void setConsolePlataforma(int consolePlataforma) {
        this.consolePlataforma = consolePlataforma;
    }
}
