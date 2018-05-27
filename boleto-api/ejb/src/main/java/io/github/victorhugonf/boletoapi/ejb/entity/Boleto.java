package io.github.victorhugonf.boletoapi.ejb.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.victorhugonf.boletoapi.ejb.useful.Constantes;

@Entity
@Table(name = Boleto.TABELA)
public class Boleto implements EntityIdentifiable{

	private static final long serialVersionUID = 2124815335333460885L;
	
	protected static final String TABELA = "boletos";
	private static final String DUE_DATE = "due_date";
	private static final String TOTAL_IN_CENTS = "total_in_cents";
	private static final String CUSTOMER = "customer";
	private static final String FINE = "fine";

	@Id
	@GeneratedValue(generator = Constantes.UUID)
	@GenericGenerator(name = Constantes.UUID, strategy = Constantes.ORG_HIBERNATE_ID_UUIDGENERATOR)
	@Column(updatable = false, nullable = false)
    private UUID id;

	@Version
	private long version;

	@Column(name = DUE_DATE, nullable = false)
	@NotNull(message = "Data de vencimento deve ser informada.")
	@JsonProperty(DUE_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dataVencimento;

	@Column(name = TOTAL_IN_CENTS, nullable = false)
	@NotNull(message = "Valor deve ser informado.")
	@DecimalMin(value = "1", message = "Valor deve ser maior que zero.")
	@Digits(integer = 10, fraction = 0, message = "Valor em centavos, não informar fracionado.")
	@JsonProperty(TOTAL_IN_CENTS)
	private BigDecimal valorTotalEmCentavos;
	
	@Column(name = CUSTOMER, nullable = false, length = 100)
	@NotNull(message = "Cliente deve ser informado.")
	@Size(min = 1, max = 100, message = "Cliente deve possuir de 1 até 100 caracteres.")
	@JsonProperty(CUSTOMER)
	private String nomeCliente;
	
	@Column(nullable = false)
	@NotNull(message = "Status deve ser informado.")
	private String status;
	
	@Transient
	@JsonProperty(FINE)
	private BigDecimal multa;

	@Override
	public UUID getId() {
        return id;
    }

	@Override
    public void setId(UUID id) {
        this.id = id;
    }

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValorTotalEmCentavos() {
		return valorTotalEmCentavos;
	}

	public void setValorTotalEmCentavos(BigDecimal valorTotalEmCentavos) {
		this.valorTotalEmCentavos = valorTotalEmCentavos;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public StatusEnum getStatus() {
		return StatusEnum.getStatus(status);
	}

	public void setStatus(StatusEnum status) {
		this.status = status == null ? null : status.getStatus();
	}
	
	public BigDecimal getMulta() {
		return multa;
	}
	
	public void setMulta(BigDecimal multa) {
		this.multa = multa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boleto other = (Boleto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
