package com.github.johnysramos;

public enum ConsolePlataforma {
    PLAYSTATION_5(R.string.playstation_5),
    XBOX_ONE(R.string.xbox_one),
    COMPUTADOR(R.string.computador),
    PLAYSTATION_1(R.string.playstation_1),
    PLAYSTATION_2(R.string.playstation_2);

    private int stringResourceDescricao;

    ConsolePlataforma(int stringResourceDescricao) {
        this.stringResourceDescricao = stringResourceDescricao;
    }

    public int getstringResourceDescricao() {
        return stringResourceDescricao;
    }

    public void setstringResourceDescricao(int stringResourceDescricao) {
        this.stringResourceDescricao = stringResourceDescricao;
    }
}
