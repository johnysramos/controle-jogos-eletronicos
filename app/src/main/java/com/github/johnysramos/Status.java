package com.github.johnysramos;

public enum Status {
    NOVO(0),
    USADO(1);

    private int numeroStatus;

    Status(int numeroStatus) {
        this.numeroStatus = numeroStatus;
    }

    public int getNumeroStatus() {
        return numeroStatus;
    }

    public void setNumeroStatus(int numeroStatus) {
        this.numeroStatus = numeroStatus;
    }
}
