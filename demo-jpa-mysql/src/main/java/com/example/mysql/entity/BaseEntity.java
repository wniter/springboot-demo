package com.example.mysql.entity;

public interface BaseEntity<D> extends ToData<D> {

    Long getId();

    void setId(Long id);
}
