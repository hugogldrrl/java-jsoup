package com.hgr.jsoup.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * Filtros de la búsqueda de comisiones.
 */
public class CommissionCriteria {

    /** Filtro "Abierto a" */
    @Pattern(regexp = "$|F|L", message = "invalid value ('F' oficials, 'L' work staff)")
    final String finalUsers;

    /** Filtro "Consellería" */
    final String counselling;

    /** Filtro "Admisión de solicitudes desde" */
    @NotNull(message = "must be not null (pattern 'yyyy-MM-dd')")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate startDate;

    /** Filtro "Ata" */
    @NotNull(message = "must be not null (pattern 'yyyy-MM-dd')")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate endDate;

    /** Filtro "Grupo" */
    @Pattern(regexp = "$|A1|A2|C1|C2|AP|I|II|III|IV|V", message = "invalid value (A1, A2, C1, C2, AP, I, II, III, IV, V)")
    final String group;

    /** Filtro "Nivel" */
    final String level;

    /** Filtro "Provincia" */
    @Pattern(regexp = "$|15|27|28|32|36", message = "invalid value ('15' A Coruña, '27' Lugo, '28' Madrid, '32' Ourense, '36' Pontevedra")
    final String provinceCode;

    /** Filtro "Concello" */
    final String district;

    /** Filtro "Descrición" */
    final String description;

    /**
     * Contructor.
     * @param finalUsers   Usuario final.
     * @param counselling  Consejería.
     * @param startDate    Fecha de inicio.
     * @param endDate      Fecha de fin.
     * @param group        Código de grupo.
     * @param level        Nivel.
     * @param provinceCode Código de provincia.
     * @param district     Concello.
     * @param description  Descripción.
     */
    public CommissionCriteria(String finalUsers, String counselling, LocalDate startDate, LocalDate endDate, String group,
                              String level, String provinceCode, String district, String description) {

        this.finalUsers = finalUsers;
        this.counselling = counselling;
        this.startDate = startDate;
        this.endDate = endDate;
        this.group = group;
        this.level = level;
        this.provinceCode = provinceCode;
        this.district = district;
        this.description = description;
    }

    /**
     * @return Valor del filtro "Abierto a".
     */
    public String getFinalUsers() {
        return finalUsers;
    }

    /**
     * @return Valor del filtro "Consellería".
     */
    public String getCounselling() {
        return counselling;
    }

    /**
     * @return Valor del filtro "Desde".
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @return Valor del filtro "Ata".
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @return Valor del filtro "Grupo".
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return Valor del filtro "Nivel".
     */
    public String getLevel() {
        return level;
    }

    /**
     * @return Valor del filtro "Provincia".
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    /**
     * @return Valor del filtro "Concello".
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @return Valor del filtro "Descrición".
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}