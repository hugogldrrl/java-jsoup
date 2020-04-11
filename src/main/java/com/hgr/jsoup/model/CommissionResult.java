package com.hgr.jsoup.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Resultados de la búsqueda de comisiones.
 */
public class CommissionResult {

    /** Código hash (Válido solamente mientras todos los atributos son final) */
    private transient int hashCode;

    /** Centro de destino. */
    final String destinyCenter;

    /** Código del puesto */
    final String jobCode;

    /** Denominación */
    final String denomination;

    /** Provisión */
    final String provision;

    /** Grupo */
    final String group;

    /** Nivel */
    final String level;

    /**
     * Constructor.
     * @param destinyCenter Centro de destino.
     * @param jobCode       Código del puesto.
     * @param denomination  Denominación.
     * @param provision     Provisión.
     * @param group         Grupo.
     * @param level         Nivel.
     */
    public CommissionResult(String destinyCenter, String jobCode, String denomination, String provision, String group, String level) {
        this.destinyCenter = destinyCenter;
        this.jobCode = jobCode;
        this.denomination = denomination;
        this.provision = provision;
        this.group = group;
        this.level = level;
    }

    /**
     * @return Centro de destino.
     */
    public String getDestinyCenter() {
        return destinyCenter;
    }

    /**
     * @return Código del puesto.
     */
    public String getJobCode() {
        return jobCode;
    }

    /**
     * @return Denominación.
     */
    public String getDenomination() {
        return denomination;
    }

    /**
     * @return Provisión.
     */
    public String getProvision() {
        return provision;
    }

    /**
     * @return Grupo.
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return Nivel.
     */
    public String getLevel() {
        return level;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof CommissionResult)) {
            return false;
        }

        CommissionResult castOther = (CommissionResult) other;
        return new EqualsBuilder()
                .append(destinyCenter, castOther.destinyCenter)
                .append(jobCode, castOther.jobCode)
                .append(denomination, castOther.denomination)
                .append(provision, castOther.provision)
                .append(group, castOther.group)
                .append(level, castOther.level)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = new HashCodeBuilder()
                    .append(destinyCenter)
                    .append(jobCode)
                    .append(denomination)
                    .append(provision)
                    .append(group)
                    .append(level)
                    .toHashCode();
        }

        return hashCode;
    }

}