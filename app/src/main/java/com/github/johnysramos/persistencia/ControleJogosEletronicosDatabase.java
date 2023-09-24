package com.github.johnysramos.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.johnysramos.JogoEletronico;

@Database(entities = {JogoEletronico.class}, version = 1)
public abstract class ControleJogosEletronicosDatabase extends RoomDatabase {

    public abstract JogoEletronicoDao jogoEletronicoDao();

    private static ControleJogosEletronicosDatabase instance;

    public static ControleJogosEletronicosDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (ControleJogosEletronicosDatabase.class) {
                if (instance == null) {
                    Builder builder =  Room.databaseBuilder(context,
                            ControleJogosEletronicosDatabase.class,
                            "controleJogosEletronicos.db").allowMainThreadQueries();

                    instance = (ControleJogosEletronicosDatabase) builder.build();
                }
            }
        }

        return instance;
    }
}
