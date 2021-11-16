package dk.kvalitetsit.regsj.testkomponent.dao;

import dk.kvalitetsit.regsj.testkomponent.dao.entity.HelloEntity;

import java.util.List;

public interface HelloDao {
    void insert(HelloEntity helloEntity);

    List<HelloEntity> findAll();
}