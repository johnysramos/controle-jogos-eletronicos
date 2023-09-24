package com.github.johnysramos.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.johnysramos.JogoEletronico;

import java.util.List;

@Dao
public interface JogoEletronicoDao {
    @Insert
    long insert(JogoEletronico jogoEletronico);

    @Delete
    void delete(JogoEletronico jogoEletronico);

    @Update
    void update(JogoEletronico jogoEletronico);

    @Query("SELECT * FROM JogoEletronico WHERE id = :id")
    JogoEletronico queryForId(Long id);

    @Query("SELECT * FROM JogoEletronico ORDER BY id")
    List<JogoEletronico> queryAll();
}
