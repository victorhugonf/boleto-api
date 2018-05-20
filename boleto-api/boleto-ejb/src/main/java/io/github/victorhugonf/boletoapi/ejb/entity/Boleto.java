package io.github.victorhugonf.boletoapi.ejb.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.victorhugonf.boletoapi.ejb.utils.CONSTANTES;

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
	@GeneratedValue(generator = CONSTANTES.UUID)
	@GenericGenerator(name = CONSTANTES.UUID, strategy = CONSTANTES.ORG_HIBERNATE_ID_UUIDGENERATOR)
	@Column(updatable = false, nullable = false)
    private UUID id;

	@Version
	private long version;

	@Column(name = DUE_DATE, nullable = false)
	@NotEmpty(message = "Data de vencimento deve ser informada.")
	@JsonProperty(DUE_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dataVencimento;

	@Column(name = TOTAL_IN_CENTS, nullable = false)
	@NotEmpty(message = "Valor deve ser informado.")
	@JsonProperty(TOTAL_IN_CENTS)
	private BigDecimal valor;
	
	@Column(name = CUSTOMER, nullable = false)
	@NotEmpty(message = "Cliente deve ser informado.")
	@JsonProperty(CUSTOMER)
	private String nomeCliente;
	
	@Column(nullable = false)
	@NotEmpty(message = "Status deve ser informado.")
	private String status;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
	
	@JsonGetter(FINE)
	public BigDecimal getMulta(){
		return null;
	}
}
